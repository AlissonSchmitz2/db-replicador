package br.com.dbreplicador.view;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;

import br.com.dbreplicador.model.ConnectionModel;
import br.com.dbreplicador.model.DirectionModel;
import br.com.dbreplicador.model.TableModel;
import br.com.dbreplicador.pojos.ReplicationQueueItem;
import br.com.dbreplicador.view.contracts.IReplicationExecutor;
import br.com.dbreplicador.view.contracts.IReplicationInfoControl;
import br.com.replicator.Replicator;
import br.com.replicator.contracts.IReplicator;
import br.com.replicator.contracts.IReplicatorProvider;
import br.com.replicator.database.ConnectionInfo;
import br.com.replicator.database.query.contracts.IQuery;
import br.com.replicator.enums.SupportedTypes;
import br.com.replicator.exceptions.InvalidDatabaseTypeException;
import br.com.replicator.exceptions.InvalidQueryAttributesException;

public class ReplicationExecutor implements IReplicationExecutor {
	private static int EXECUTOR_TIMEOUT = 50;
	
	private IReplicationInfoControl window;
	
	private Timestamp toDate;
	
	private volatile Thread executionThread;
	private boolean threadSuspended = true;
	
	private AbstractMap<Integer, ReplicationQueueItem> queue = new HashMap<Integer, ReplicationQueueItem>();
	private AbstractMap<String, TableModel> processedTables = new HashMap<String, TableModel>();
	private Integer currentQueueIndex = 0;
	private Integer totalErrors = 0;
	
	public ReplicationExecutor(IReplicationInfoControl window) {
		//Guarda a referência da tela de replicação
		this.window = window;

		// Guardo a conexão.

		// Instancia os DAO de controle.
	}

	public void start(Timestamp fromDate) {
		//Se a thread for nula, um novo processo é inicializado
		//Ou seja, o processo de replicação é iniciado do zero
		initializeProcessing(fromDate);
		
		//O processo é retirado da suspensão
		threadSuspended = false;
		
		//Ativa indicador de processamento
		window.setProgressIndeterminate(true);
	}
	
	public void resume() {
		if (executionThread != null) {
			//Caso o processo exista, o mesmo é retirado da suspensão
			threadSuspended = false;
			
			//Ativa indicador de processamento
			window.setProgressIndeterminate(true);
		}
	}
	
	public void pause() {
		//Suspende o processo previamente inciado
		if (executionThread != null) {
			threadSuspended = true;
		}
		
		//Desativa indicador de processamento
		window.setProgressIndeterminate(false);
	}
	
	public void stop() {
		//Ao parar, a thread se torna nula
		threadSuspended = true;
		executionThread = null;
		
		//Reseta estatísticas
		resetStatistics();
		
		//Desativa indicador de processamento
		window.setProgressIndeterminate(false);
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
		resetStatistics();
		
		this.toDate = toDate;
		
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
		cityTable.setMaximumLines(50);
		cityTable.setErrorIgnore(true);
		cityTable.setEnable(true);
		
		AbstractMap<Integer, TableModel> tables = new HashMap<Integer, TableModel>();
		tables.put(1, cityTable);
		direction1.setTables(tables);
		//End Tables
		
		AbstractMap<Integer, DirectionModel> daoReturn = new HashMap<Integer, DirectionModel>();
		
		//Adiciona a conexão a fila para processamento
		daoReturn.put(1, direction1);
		
		//---------------- final - refatoração ----------------------//
		
		//Apartir das conexões retornadas do banco de dados, gera a fila de queries para processamento
		generateQueue(daoReturn);

		//Cria a thread responsável pelo processamento
		createThread();
	}
	
	private void generateQueue(AbstractMap<Integer, DirectionModel> directions) {
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
						"id_cidade",//TODO
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
	
	private void createThread() {
		System.out.println("Create");
		
		executionThread = new Thread(new Runnable() {
			public void run() {
				try {
					Thread thisThread = Thread.currentThread();
					
					while (!Thread.currentThread().isInterrupted() && executionThread == thisThread) {
						System.out.println("...");
						if (!threadSuspended) {
							currentQueueIndex = currentQueueIndex == 0 ? 1 : currentQueueIndex;
							
							//Se o index controlador for maior que tamanho da fila, o processamento é considerado finalizado
							if (currentQueueIndex > queue.size()) {
								System.out.println("Finished");
								
								//Recupera o último item da fila
								ReplicationQueueItem lastItem = queue.get(queue.size());
								
								//Comita dados pendentes
								if (lastItem != null) {
									lastItem.getProvider().getConn().commit();
								}

								//Encerra a thread
								threadSuspended = true;
								executionThread = null;
								
								//Desativa indicador de processamento
								window.setProgressIndeterminate(false);
								
								//Reseta controles
								currentQueueIndex = 0;
								totalErrors = 0;
								processedTables.clear();
								
								//Salva o log do processo concluído
								//TODO: deve-se levar em consideração toDate
								
								//Informa subscribers que o processo acabou
								//TODO
								
								continue;
							}
							
							//Usa o primeiro elemento da sequência para descobrir a quantidade de itens a serem salvos
							Integer indexLimit = currentQueueIndex + queue.get(currentQueueIndex).getTableModel().getMaximumLines();
							indexLimit = indexLimit > queue.size() ? queue.size() : indexLimit;
							
							IReplicatorProvider provider = null;
							
							for (int i = currentQueueIndex; i <= indexLimit; i++) {
								ReplicationQueueItem currentItem = queue.get(currentQueueIndex);
								
								processedTables.put(currentItem.getTableModel().getOriginTable(), currentItem.getTableModel());
								
								//Seta direções
								window.setCurrentDirections(
									currentItem.getDirectionModel().getOriginConnectionModel().getDatabase(),
									currentItem.getDirectionModel().getDestinationConnectionModel().getDatabase()
								);
								window.setCurrentProcess("anything"); //TODO
								window.setCurrentTable(currentItem.getTableModel().getOriginTable());
								
								if (provider != null && currentItem.getProvider() != provider) {
									//Antes da troca do provider, comita valores não salvos
									provider.getConn().commit();
								}
								
								provider = queue.get(currentQueueIndex).getProvider();
								
								Integer result = provider.getProcessor()
										.executeUpdate(queue.get(i).getQuery(), "id_cidade");
								//TODO: Pegar do item ao invés do valor chumbado

								if (result > 0) {
									Integer progressValue = (currentQueueIndex * 100) / queue.size();
									window.setProgressBarValue(progressValue);
								} else {
									totalErrors++;
									
									window.setTotalOfTables(totalErrors);
								}
								
								currentQueueIndex++;
							}
							
							window.setTotalOfTables(processedTables.size());

							provider.getConn().commit();
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
		window.setCurrentDirections("", "");
		window.setCurrentProcess("");
		window.setCurrentTable("");
		window.setTotalOfTables(0);
		window.setTotalOfErrors(0);
		window.setProgressBarValue(0);
	}
}
