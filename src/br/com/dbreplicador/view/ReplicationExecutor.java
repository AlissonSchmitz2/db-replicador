package br.com.dbreplicador.view;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.dbreplicador.dao.DirectionDAO;
import br.com.dbreplicador.dao.ProcessDAO;
import br.com.dbreplicador.dao.TableDAO;
import br.com.dbreplicador.enums.ReplicationEvents;
import br.com.dbreplicador.model.DirectionModel;
import br.com.dbreplicador.model.ProcessModel;
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
	
	private DirectionDAO directionDao;
	private ProcessDAO processDao;
	private TableDAO tableDao;
	
	public ReplicationExecutor(Connection connection) {
		try {
			directionDao = new DirectionDAO(connection);
			processDao = new ProcessDAO(connection);
			tableDao = new TableDAO(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
		stopThread();
		
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
	
	private void stopThread() {
		threadSuspended = true;
		executionThread = null;
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
					Map<Integer, DirectionModel> directions = directionDao.getProcessesToReplication();
					
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
					Timestamp tableToDate = table.isIncrementalBackup() && table.getCurrentDateOf() != null ? table.getCurrentDateOf() : new Timestamp(0);
					
					List<IQuery> queries = replicator.getQueriesForReplication(
						table.getOriginTable(),
						table.getDestinationTable(),
						table.getKeyColumn(),
						table.getControlColumn(),
						tableToDate
					);
					
					//Adiciona as queries a fila
					queries.forEach(query -> {
						queue.put(
								(queue.size() + 1), 
								new ReplicationQueueItem(
										query, 
										replicator.getDestinationProvider(), 
										direction, 
										table
								));
					});
				}
			} catch (SQLException | InvalidDatabaseTypeException | InvalidQueryAttributesException e) {
				e.printStackTrace();
			} catch (Exception e) {
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
				
				//Se não existir itens, não inicia o processamento
				if (queue.size() == 0) {
					//Notifica evento
					notifyObservers(ReplicationEvents.FINISHED);
					
					stopThread();
				}
				
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
							Integer indexLimit = currentQueueIndex + (queue.get(currentQueueIndex).getTableModel().getMaximumLines() - 1);
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
	
	private void processingQueueInterval(Integer indexLimit) {
		IReplicatorProvider provider = null;
		
		for (int i = currentQueueIndex; i <= indexLimit; i++) {
			ReplicationQueueItem currentItem = queue.get(currentQueueIndex);
			
			//Armazena a tabela atual para processados
			if (!processedTables.containsKey(currentItem.getTableModel().getOriginTable())) {
				if (processedTables.size() > 0) {
					//Persiste logs do processamento da tabela
					persistTableProcessingInfo(queue.get(i - 1).getTableModel());
				}
				
				processedTables.put(currentItem.getTableModel().getOriginTable(), currentItem.getTableModel());
			}
			
			//Seta estatísticas
			currentOriginDirection = currentItem.getDirectionModel().getOriginConnectionModel().getDatabase();
			currentDestinationDirection = currentItem.getDirectionModel().getDestinationConnectionModel().getDatabase();
			currentProcess = currentItem.getDirectionModel().getProcessModel().getDescription();
			currentTable = currentItem.getTableModel().getOriginTable();
			
			try {

				if (provider != null && currentItem.getProvider() != provider) {
					//Antes da troca do provider, comita valores não salvos
					provider.getConn().commit();
				}
				
				provider = queue.get(currentQueueIndex).getProvider();
				
				//Força auto commit para falso
				if (provider.getConn().getAutoCommit()) {
					provider.getConn().setAutoCommit(false);
				}
				
				Integer result = provider.getProcessor()
						.executeUpdate(queue.get(i).getQuery(), queue.get(i).getTableModel().getKeyColumn());
	
				if (result > 0) {
					processingProgress = (currentQueueIndex * 100) / queue.size();
				} else {
					totalOfErrors++;
					
					notifyObservers(ReplicationEvents.ON_ERROR);
					
					//Se for diferente de ignorar erro, para o processamento
					if (!currentItem.getDirectionModel().getProcessModel().isErrorIgnore() || !currentItem.getTableModel().isErrorIgnore()
					) {
						stopThread();
						
						provider.getConn().commit();
						
						notifyObservers(ReplicationEvents.FINISHED_BY_ERROR);
						
						break;
					}
				}
				
				totalOfTables = processedTables.size();

				//Caso seja o último item da sequência, faz o commit
				if (i == indexLimit) {
					provider.getConn().commit();
					
					//Notifica evento
					notifyObservers(ReplicationEvents.ON_PROCESS);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				
				totalOfErrors++;
				
				notifyObservers(ReplicationEvents.ON_ERROR);
				
				//Se for diferente de ignorar erro, para o processamento
				if (!currentItem.getDirectionModel().getProcessModel().isErrorIgnore() || !currentItem.getTableModel().isErrorIgnore()
				) {
					stopThread();
					
					try {
						provider.getConn().commit();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
					notifyObservers(ReplicationEvents.FINISHED_BY_ERROR);
					
					break;
				}
			}
			
			currentQueueIndex++;
		}
	}
	
	private void finishQueueProcessing(Integer index) throws SQLException {
		//Recupera o último item da fila
		ReplicationQueueItem lastItem = queue.get(index);
		
		//Comita dados pendentes
		if (lastItem == null) {
			//Notifica evento
			notifyObservers(ReplicationEvents.FATAL_ERROR);
			
			return;
		}
		
		lastItem.getProvider().getConn().commit();

		//Encerra a thread
		stopThread();
		
		//Persiste logs do processamento da tabela
		persistTableProcessingInfo(lastItem.getTableModel());
		
		//Persiste logs do processamento do processo
		persistProcessProcessingInfo(lastItem.getDirectionModel());
		
		//Notifica evento
		notifyObservers(ReplicationEvents.FINISHED);
	}
	
	private void persistTableProcessingInfo(TableModel table) {
		try {
			//Salva data da última replicação da tabela
			TableModel tableModel = tableDao.findById(table.getReplicationCode());
			if (tableModel != null) {
				tableModel.setCurrentDateOf(toDate);
				tableDao.update(tableModel);	
			}
					
			//Salva log do processamento da tabela
			//TODO
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void persistProcessProcessingInfo(DirectionModel direction) {
		try {
			//Salva a última data de replicação no processo
			ProcessModel processModel;
			
			processModel = processDao.findById(direction.getProcessModel().getProcessCode());
			
			if (processModel != null) {
				processModel.setCurrentDateOf(toDate);
				processDao.update(processModel);	
			}
			
			//Salva o log do processo concluído
			//TODO
			System.out.println("Salva log do processamento geral");
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
		System.out.println("Event: " + event.getCode() + " - " + LocalDateTime.now());
		
		Iterator<IReplicationObserver> it = observers.iterator();
		
		while (it.hasNext()) {
			IReplicationObserver observer = (IReplicationObserver) it.next();
			observer.update(this, event);
		}
	}
}
