package br.com.dbreplicador.view;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.dbreplicador.dao.DirectionDAO;
import br.com.dbreplicador.dao.ExecutionDAO;
import br.com.dbreplicador.dao.ProcessDAO;
import br.com.dbreplicador.dao.TableDAO;
import br.com.dbreplicador.dao.TableExecutionDAO;
import br.com.dbreplicador.enums.ReplicationEvents;
import br.com.dbreplicador.enums.ReplicationProcessingStatuses;
import br.com.dbreplicador.model.DirectionModel;
import br.com.dbreplicador.model.ExecutionModel;
import br.com.dbreplicador.model.ProcessModel;
import br.com.dbreplicador.model.TableExecutionModel;
import br.com.dbreplicador.model.TableModel;
import br.com.dbreplicador.observers.contracts.IReplicationObserver;
import br.com.dbreplicador.observers.contracts.IReplicationSubject;
import br.com.dbreplicador.pojos.ReplicationProcessedProcess;
import br.com.dbreplicador.pojos.ReplicationProcessedTable;
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
	private static int EXECUTOR_TIMEOUT = 0;
	
	private Timestamp toDate;
	
	private volatile Thread executionThread;
	private boolean threadSuspended = true;
	
	private Collection<IReplicationObserver> observers = new HashSet<IReplicationObserver>();
	
	private Map<Integer, ReplicationQueueItem> queue = new HashMap<Integer, ReplicationQueueItem>();
	private Map<String, ReplicationProcessedTable> processedTables = new HashMap<String, ReplicationProcessedTable>();
	private Map<String, ReplicationProcessedProcess> processedProcesses = new HashMap<String, ReplicationProcessedProcess>();
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
	private ExecutionDAO executionDao;
	private TableExecutionDAO tableExecutionDao;
	
	public ReplicationExecutor(Connection connection) {
		try {
			directionDao = new DirectionDAO(connection);
			processDao = new ProcessDAO(connection);
			tableDao = new TableDAO(connection);
			executionDao = new ExecutionDAO(connection);
			tableExecutionDao = new TableExecutionDAO(connection);
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
		
		//Limpa filas anteriores
		queue.clear();
		
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
	
	private List<Thread> arrThreads = new ArrayList<Thread>();
	
	private void generateQueue(Map<Integer, DirectionModel> directions) {
		try {
			Thread thread = new Thread(new Runnable() {
				public void run() {
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
			});
			thread.start();
			arrThreads.add(thread);
			
			for (int i = 0; i < arrThreads.size(); i++) {
				arrThreads.get(i).join(); 
            }
		} catch (Exception e) {
            System.out.println(e.getMessage());
        }
	}
	
	private void processQueue() {
		currentQueueIndex = 0;
		processedTables.clear();
		processedProcesses.clear();
		
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
							processQueueInterval(indexLimit);
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
	
	private void processQueueInterval(Integer indexLimit) {
		IReplicatorProvider provider = null;
		
		for (int i = currentQueueIndex; i <= indexLimit; i++) {
			ReplicationQueueItem currentItem = queue.get(currentQueueIndex);
			ProcessModel currentProcessModel = currentItem.getDirectionModel().getProcessModel();
			
			//Controles de itens já processados
			if (setProcessToProcessedQueueIfNew(currentProcessModel)) {
				//Persiste logs do processamento da direção anterior, considerada finalizada
				if (queue.get(i - 1) != null) {
					handleDirectionProcessingInfo(queue.get(i - 1).getDirectionModel(), ReplicationProcessingStatuses.SUCCESS);
				}
			}
			
			//Caso retornado true, o item foi recém adicionado
			if (setTableToProcessedQueueIfNew(currentProcessModel, currentItem.getTableModel())) {
				//Persiste logs do processamento da tabela anterior, considerada finalizada
				if (queue.get(i - 1) != null) {
					handleTableProcessingInfo(currentProcessModel, queue.get(i - 1).getTableModel(), ReplicationProcessingStatuses.SUCCESS);
				}
			}
			
			//Incrementa o total de queries processadas
			ReplicationProcessedTable currentProcessedTable = processedTables.get(getProcessedTablesKey(currentItem.getDirectionModel().getProcessModel(), currentItem.getTableModel()));
			
			//Seta estatísticas
			setStatistics(currentItem);
			
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
				
				currentProcessedTable.updateQueryTotals(queue.get(i).getQuery());
				
				int result = provider.getProcessor().executeUpdate(queue.get(i).getQuery());
				
				if (result > 0) {
					processingProgress = (currentQueueIndex * 100) / queue.size();
				} else {
					totalOfErrors++;
					
					notifyObservers(ReplicationEvents.ON_ERROR);
					
					//Se for retornado true, para o processamento
					if (handleProcessingError(currentItem, provider)) {
						break;
					}
				}

				//Caso seja o último item da sequência, faz o commit
				if (i == indexLimit) {
					provider.getConn().commit();
					
					totalOfTables = processedTables.size();
					
					//Notifica evento
					notifyObservers(ReplicationEvents.ON_PROCESS);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				
				totalOfErrors++;
				
				notifyObservers(ReplicationEvents.ON_ERROR);
				
				//Se for retornado true, para o processamento
				if (handleProcessingError(currentItem, provider)) {
					break;
				}
			}
			
			currentQueueIndex++;
		}
	}
	
	private String getProcessedTablesKey(ProcessModel processModel, TableModel tableModel) {
		return processModel.getProcessCode() + "_" + tableModel.getOriginTable();
	}
	
	private boolean handleProcessingError(ReplicationQueueItem item, IReplicatorProvider provider) {
		if (!item.getDirectionModel().getProcessModel().isErrorIgnore() || !item.getTableModel().isErrorIgnore()) {
			stopThread();
			
			try {
				provider.getConn().commit();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			handleDirectionProcessingInfo(item.getDirectionModel(), ReplicationProcessingStatuses.ERROR);
			
			handleTableProcessingInfo(item.getDirectionModel().getProcessModel(), item.getTableModel(), ReplicationProcessingStatuses.ERROR);
			
			notifyObservers(ReplicationEvents.FINISHED_BY_ERROR);
			
			return true;
		}
		
		return false;
	}
	
	private boolean setProcessToProcessedQueueIfNew(ProcessModel processModel) {
		//Adiciona o processo a lista de processados
		if (!processedProcesses.containsKey(processModel.getProcess())) {
			ReplicationProcessedProcess processed = new ReplicationProcessedProcess(
					processModel,
					new Timestamp(System.currentTimeMillis())
			);
			
			processedProcesses.put(processModel.getProcess(), processed);
			
			return true;
		}
		
		return false;
	}
	
	private boolean setTableToProcessedQueueIfNew(ProcessModel processModel, TableModel tableModel) {
		String identifierKey = getProcessedTablesKey(processModel, tableModel);
		
		//Adiciona o processo a lista de processados
		if (!processedTables.containsKey(identifierKey)) {
			ReplicationProcessedTable processed = new ReplicationProcessedTable(
					tableModel,
					new Timestamp(System.currentTimeMillis())
			);
			
			processedTables.put(identifierKey, processed);
		
			return true;
		}
		
		return false;
	}
	
	private void setStatistics(ReplicationQueueItem item) {
		currentOriginDirection = item.getDirectionModel().getOriginConnectionModel().getDatabase();
		currentDestinationDirection = item.getDirectionModel().getDestinationConnectionModel().getDatabase();
		currentProcess = item.getDirectionModel().getProcessModel().getDescription();
		currentTable = item.getTableModel().getOriginTable();
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
		handleTableProcessingInfo(lastItem.getDirectionModel().getProcessModel(), lastItem.getTableModel(), ReplicationProcessingStatuses.SUCCESS);
		
		//Persiste logs do processamento da direção
		handleDirectionProcessingInfo(lastItem.getDirectionModel(), ReplicationProcessingStatuses.SUCCESS);
		
		//Notifica evento
		notifyObservers(ReplicationEvents.FINISHED);
	}
	
	private void handleTableProcessingInfo(ProcessModel processModel, TableModel tableModel, ReplicationProcessingStatuses status) {
		new Thread(new Runnable() {
			public void run() {
				try {
					//Em caso de sucesso, salva a última data de replicação na tabela
					if (status == ReplicationProcessingStatuses.SUCCESS) {
						TableModel updatedTableModel = tableDao.findById(tableModel.getReplicationCode());
						if (updatedTableModel != null) {
							updatedTableModel.setCurrentDateOf(toDate);
							tableDao.update(updatedTableModel);
						}
					}
					
					String identifierKey = getProcessedTablesKey(processModel, tableModel);

					//Salva log do processamento da tabela
					ReplicationProcessedTable processedItem = processedTables.get(identifierKey);
					
					TableExecutionModel log = new TableExecutionModel();
					log.setProcess(tableModel.getProcess());
					log.setOriginDatabase(tableModel.getOriginTable());
					log.setOriginUser("---");
					log.setDestinationDatabase(tableModel.getDestinationTable());
					log.setDestinationUser("---");
					log.setOrder(tableModel.getOrder());
					log.setStartDateTime(processedItem.getStartDate());
					log.setFinishDateTime(new Timestamp(System.currentTimeMillis()));
					log.setCurrentDateUntil(toDate);
					log.setProcessedLines(processedItem.getTotalsOfQueries());
					log.setSucess(status == ReplicationProcessingStatuses.SUCCESS);
					log.setMessage("Inserted: " + processedItem.getTotalOfInsertQueries() + ", Updated: " + processedItem.getTotalOfUpdateQueries() + ", Deleted: " + processedItem.getTotalOfDeleteQueries());

					tableExecutionDao.insert(log);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void handleDirectionProcessingInfo(DirectionModel directionModel, ReplicationProcessingStatuses status) {
		new Thread(new Runnable() {
			public void run() {
				try {
					//Em caso de sucesso, salva a última data de replicação no processo
					if (status == ReplicationProcessingStatuses.SUCCESS) {
						ProcessModel updatedProcessModel = processDao.findById(directionModel.getProcessModel().getProcessCode());
						
						if (updatedProcessModel != null) {
							updatedProcessModel.setCurrentDateOf(toDate);
							processDao.update(updatedProcessModel);	
						}
					}
					
					//Salva o log do processo concluído
					ReplicationProcessedProcess processedItem = processedProcesses.get(directionModel.getProcessModel().getProcess());
					
					ExecutionModel log = new ExecutionModel();
					log.setOriginDatabase(directionModel.getOriginDatabase());
					log.setOriginUser(directionModel.getOriginUser());
					log.setDestinatioDatabase(directionModel.getDestinationDatabase());
					log.setDestinationUser(directionModel.getDestinationUser());
					log.setDateHourInitial(processedItem.getStartDate());
					log.setDateHourFinal(new Timestamp(System.currentTimeMillis()));
					log.setCurrenteDateTo(toDate);
					log.setProcess(directionModel.getProcess());
					log.setOccurrence(status.getCode());
					
					executionDao.insert(log);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}).start();
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
