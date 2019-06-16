package br.com.dbreplicador.view;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.dbreplicador.enums.ReplicationEvents;
import br.com.dbreplicador.model.ConnectionModel;
import br.com.dbreplicador.model.DirectionModel;
import br.com.dbreplicador.model.TableModel;
import br.com.dbreplicador.observers.contracts.IReplicationObserver;
import br.com.dbreplicador.observers.contracts.IReplicationSubject;
import br.com.dbreplicador.pojos.ReplicationQueueItem;
import br.com.dbreplicador.view.contracts.IReplicationExecutor;
import br.com.dbreplicador.view.contracts.IReplicationProcessingInfo;
import br.com.replicator.Replicator;
import br.com.replicator.contracts.IReplicator;
import br.com.replicator.contracts.IReplicatorProvider;
import br.com.replicator.database.ConnectionInfo;
import br.com.replicator.database.query.contracts.IQuery;
import br.com.replicator.enums.SupportedTypes;
import br.com.replicator.exceptions.InvalidDatabaseTypeException;
import br.com.replicator.exceptions.InvalidQueryAttributesException;

public class ReplicationExecutor implements IReplicationExecutor, IReplicationProcessingInfo, IReplicationSubject {
	private static int EXECUTOR_TIMEOUT = 50;
	
	private Timestamp toDate;
	
	private volatile Thread executionThread;
	private boolean threadSuspended = true;
	
	private Collection<IReplicationObserver> observers = new HashSet<IReplicationObserver>();
	
	private Map<Integer, ReplicationQueueItem> queue = new HashMap<Integer, ReplicationQueueItem>();
	private Map<String, TableModel> processedTables = new HashMap<String, TableModel>();
	private Integer currentQueueIndex = 0;
	
	private String currentOriginDirection = "";
	private String currentDestinationDirection = "";
	private String currentProcess = "";
	private String currentTable = "";
	private Integer totalOfTables = 0;
	private Integer totalOfErrors = 0;
	private Integer processingProgress = 0;
	
	public ReplicationExecutor() {
		// Guardo a conexão.

		// Instancia os DAO de controle.
	}

	public void start(Timestamp fromDate) {
		//Inicializa processo
		initializeProcessing(fromDate);
		
		//O processo é retirado da suspensão
		threadSuspended = false;
	}
	
	public void resume() {
		if (executionThread != null) {
			//Caso o processo exista, o mesmo é retirado da suspensão
			threadSuspended = false;
		}
		
		//Notifica evento
		notifyObservers(ReplicationEvents.RESUMED);
	}
	
	public void pause() {
		//Suspende o processo previamente inciado
		if (executionThread != null) {
			threadSuspended = true;
		}
		
		//Notifica evento
		notifyObservers(ReplicationEvents.PAUSED);
	}
	
	public void stop() {
		//Ao parar, a thread se torna nula
		threadSuspended = true;
		executionThread = null;
		
		//Reseta estatísticas anteriores
		resetStatistics();
		
		//Notifica evento
		notifyObservers(ReplicationEvents.STOPPED);
	}
	
	public boolean isRunning() {
		if (executionThread == null) {
			return false;
		}
		
		return executionThread.isAlive() && !threadSuspended;
	}
	
	public boolean isClosed() {
		return executionThread == null;
	}
	
	private void initializeProcessing(Timestamp toDate) {
		this.toDate = toDate;
		
		//Reseta estatísticas anteriores
		resetStatistics();
		
		//Notifica evento
		notifyObservers(ReplicationEvents.PREPARING);
		
		new Thread(new Runnable() {
			public void run() {
				try {
					//Pega as direções do DAO para replicação
					Map<Integer, DirectionModel> directions = getDirectionsToReplicate();
					
					//Apartir das conexões retornadas do banco de dados, gera a fila de queries para processamento
					generateQueue(directions);
					
					notifyObservers(ReplicationEvents.READY);

					//Cria a thread responsável pelo processamento
					processQueue();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private Map<Integer, DirectionModel> getDirectionsToReplicate() {
		//---------------- Para ser refatorado ----------------------//
		
		//TODO: Recuperar conexões para processamento através do DAO
		//Nota: Ao criar o DAO, levar em consideração somente os ativos
		ConnectionModel originConnection = new ConnectionModel();
		originConnection.setDatebaseType("PostgreSQL");
		originConnection.setAddress("localhost");
		originConnection.setDatabase("master");
		originConnection.setPort(5432);
		
		ConnectionModel destinationConnection = new ConnectionModel();
		destinationConnection.setDatebaseType("PostgreSQL");
		destinationConnection.setAddress("localhost");
		destinationConnection.setDatabase("nocaute2");
		destinationConnection.setPort(5432);

		DirectionModel direction1 = new DirectionModel();
		direction1.setOriginConnectionModel(originConnection);
		direction1.setOriginUser("admin");
		direction1.setOriginPassword("admin");
		
		direction1.setDestinationConnectionModel(destinationConnection);
		direction1.setDestinationUser("admin");
		direction1.setDestinationPassword("admin");
		
		//Tables
		TableModel cityTable = new TableModel();
		cityTable.setOrder(1);
		cityTable.setOriginTable("cidades");
		cityTable.setDestinationTable("cidades");
		cityTable.setCurrentDate(new Timestamp(0));//TODO: deve vir do banco de dados
		//TODO: setColumnControl,
		//TODO: setUniqueKey
		cityTable.setKeyColumn("id_cidade");
		cityTable.setMaximumLines(50);
		cityTable.setErrorIgnore(true);
		cityTable.setEnable(true);
		
		TableModel studentTable = new TableModel();
		studentTable.setOrder(1);
		studentTable.setOriginTable("alunos");
		studentTable.setDestinationTable("alunos");
		studentTable.setCurrentDate(new Timestamp(0));//TODO: deve vir do banco de dados
		//TODO: setColumnControl,
		//TODO: setUniqueKey
		studentTable.setKeyColumn("codigo_aluno"); //TODO
		studentTable.setMaximumLines(50);
		studentTable.setErrorIgnore(true);
		studentTable.setEnable(true);
		
		Map<Integer, TableModel> tables = new HashMap<Integer, TableModel>();
		tables.put(1, cityTable);
		tables.put(2, studentTable);
		direction1.setTables(tables);
		//End Tables
		
		Map<Integer, DirectionModel> daoReturn = new HashMap<Integer, DirectionModel>();
		
		//Adiciona a conexão a fila para processamento
		daoReturn.put(1, direction1);
		
		//---------------- final - refatoração ----------------------//
		
		return daoReturn;
	}
	
	private void generateQueue(Map<Integer, DirectionModel> directions) {
		for (DirectionModel direction : directions.values()) {
			ConnectionInfo originConnInfo = new ConnectionInfo(
				getDatabaseType(direction.getOriginConnectionModel().getDatebaseType()),
				direction.getOriginConnectionModel().getAddress(),
				direction.getOriginConnectionModel().getPort(),
				direction.getOriginConnectionModel().getDatabase(),
				direction.getOriginUser(),
				direction.getOriginPassword()
			);
			ConnectionInfo destinationConnInfo = new ConnectionInfo(
				getDatabaseType(direction.getDestinationConnectionModel().getDatebaseType()),
				direction.getDestinationConnectionModel().getAddress(),
				direction.getDestinationConnectionModel().getPort(),
				direction.getDestinationConnectionModel().getDatabase(),
				direction.getDestinationUser(),
				direction.getDestinationPassword()
			);
			
			try {
				IReplicator replicator = new Replicator(originConnInfo, destinationConnInfo);
				
				//Desabilita auto commit da conexão de destino
				replicator.getDestinationProvider().getConn().setAutoCommit(false);
					
				for (TableModel table : direction.getTables().values()) {
					List<IQuery> queries = replicator.getQueriesForReplication(
						table.getOriginTable(),
						table.getDestinationTable(),
						table.getKeyColumn(),//TODO
						"ultima_atualizacao",//TODO
						table.getCurrentDate()
					);
					
					//Adiciona as queries a fila
					queries.forEach(query -> {
						queue.put((queue.size() + 1), new ReplicationQueueItem(query, replicator.getDestinationProvider(), direction, table));
					});
				}
			} catch (SQLException | InvalidDatabaseTypeException | InvalidQueryAttributesException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void processQueue() {
		currentQueueIndex = 0;
		processedTables.clear();
		
		executionThread = new Thread(new Runnable() {
			public void run() {
				notifyObservers(ReplicationEvents.EXECUTING);
				
				try {
					Thread thisThread = Thread.currentThread();
					
					while (!Thread.currentThread().isInterrupted() && executionThread == thisThread) {
						if (!threadSuspended) {
							//Atualiza index de controle
							currentQueueIndex = currentQueueIndex == 0 ? 1 : currentQueueIndex;
							
							//ON FINISH QUEUE
							//Se o index controlador for maior que tamanho da fila, o processamento é considerado finalizado
							if (currentQueueIndex > queue.size()) {
								//Aciona método responsável por encerrar processamento
								finishQueueProcessing(queue.size());
								
								continue;
							}
							
							//Usa o primeiro elemento da sequência para descobrir a quantidade de itens a serem salvos
							Integer indexLimit = currentQueueIndex + queue.get(currentQueueIndex).getTableModel().getMaximumLines();
							indexLimit = indexLimit > queue.size() ? queue.size() : indexLimit;
							
							//Processa as queues no intevalo entre o index atual e o limite
							processingQueueInterval(indexLimit);
						}
						
						try {
							Thread.sleep(EXECUTOR_TIMEOUT);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		executionThread.start();
	}
	
	private void processingQueueInterval(Integer indexLimit) throws SQLException {
		IReplicatorProvider provider = null;
		
		for (int i = currentQueueIndex; i <= indexLimit; i++) {
			ReplicationQueueItem currentItem = queue.get(currentQueueIndex);
			
			//Armazena a tabela atual para processados
			processedTables.put(currentItem.getTableModel().getOriginTable(), currentItem.getTableModel());
			
			//Seta estatísticas
			currentOriginDirection = currentItem.getDirectionModel().getOriginConnectionModel().getDatabase();
			currentDestinationDirection = currentItem.getDirectionModel().getDestinationConnectionModel().getDatabase();
			currentProcess = "anything"; //TODO: Pegar do retornado do DAO
			currentTable = currentItem.getTableModel().getOriginTable();
			
			if (provider != null && currentItem.getProvider() != provider) {
				//Antes da troca do provider, comita valores não salvos
				provider.getConn().commit();
			}
			
			provider = queue.get(currentQueueIndex).getProvider();
			
			Integer result = provider.getProcessor()
					.executeUpdate(queue.get(i).getQuery(), queue.get(i).getTableModel().getKeyColumn());

			if (result > 0) {
				processingProgress = (currentQueueIndex * 100) / queue.size();
			} else {
				totalOfErrors++;
				
				//Notifica evento
				notifyObservers(ReplicationEvents.ON_ERROR);
				
				//TODO: Criar a lógica para parar processamento ao ocorrer um erro
				//Caso a configuração esteja setada para isso
			}
			
			currentQueueIndex++;
		}
		
		totalOfTables = processedTables.size();

		provider.getConn().commit();
		
		//Notifica evento
		notifyObservers(ReplicationEvents.ON_PROCESS);
	}
	
	private void finishQueueProcessing(Integer index) throws SQLException {
		//Recupera o último item da fila
		ReplicationQueueItem lastItem = queue.get(index);
		
		//Comita dados pendentes
		if (lastItem != null) {
			lastItem.getProvider().getConn().commit();
		}

		//Encerra a thread
		threadSuspended = true;
		executionThread = null;
		
		//Salva o log do processo concluído
		//TODO: deve-se levar em consideração toDate
		
		//Informa subscribers que o processo acabou
		//TODO
		
		//Notifica evento
		notifyObservers(ReplicationEvents.FINISHED);
	}
	
	private SupportedTypes getDatabaseType(String databaseType) {
		switch(databaseType) {
			case "PostgreSQL":
			return SupportedTypes.POSTGRESQL;
			case "MySQL":
			return SupportedTypes.MYSQL;
		}
		
		return null;
	}
	
	private void resetStatistics() {
		currentOriginDirection = "";
		currentDestinationDirection = "";
		currentProcess = "";
		currentTable = "";
		totalOfTables = 0;
		totalOfErrors = 0;
		processingProgress = 0;
	}
	
	public String getCurrentOriginDirection() {
		return currentOriginDirection;
	}
	
	public String getCurrentDestinationDirection() {
		return currentDestinationDirection;
	}

	public String getCurrentTable() {
		return currentTable;
	}

	public Integer getTotalOfTables() {
		return totalOfTables;
	}

	public Integer getTotalOfErrors() {
		return totalOfErrors;
	}

	public String getCurrentProcess() {
		return currentProcess;
	}

	public Integer getProcessingProgress() {
		return processingProgress;
	}

	public void addObserver(IReplicationObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(IReplicationObserver observer) {
		observers.remove(observer);
	}

	public void notifyObservers(ReplicationEvents event) {
		Iterator<IReplicationObserver> it = observers.iterator();
		
		while (it.hasNext()) {
			IReplicationObserver observer = (IReplicationObserver) it.next();
			observer.update(this, event);
		}
	}
}
