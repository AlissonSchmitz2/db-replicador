package br.com.dbreplicador.view;

import java.util.AbstractMap;
import java.util.HashMap;

import br.com.dbreplicador.model.DirectionModel;
import br.com.dbreplicador.view.contracts.IReplicationInfoControl;

public class ReplicationExecutor {
	private static int EXECUTOR_TIMEOUT = 1000;
	
	private IReplicationInfoControl window;
	
	private volatile Thread executionThread;
	private boolean threadSuspended = true;
	
	private AbstractMap<Integer, DirectionModel> queue = new HashMap<Integer, DirectionModel>();
	
	public ReplicationExecutor(IReplicationInfoControl window) {
		//Guarda a refer�ncia da tela de replica��o
		this.window = window;

		// Guardo a conex�o.

		// Instancia os DAO de controle.
	}

	public void start() {
		//Se a thread for nula, um novo processo � inicializado
		//Ou seja, o processo de replica��o � iniciado do zero
		if (executionThread == null) {
			initializeProcessing();
			
			executionThread.start();
		}
		
		//Caso o processo j� exista, o mesmo � retirado da suspens�o
		threadSuspended = false;
		
		//Ativa indicador de processamento
		window.setProgressIndeterminate(true);
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
		
		//Reseta estat�sticas
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
	
	private void initializeProcessing() {
		//TODO: Iterar para recuperar os registros a serem replicados
		
		// Conex�o: host, porta, banco e tipo
		
				// Processo: processo, data de, ignorar erro e habilitado
				
				// Dire��o: (processo), dura��o, reten��o, automatico, habilitado
				   // Origem: (Conex�o: database), usuario e senha
				   // Destino: (Conex�o: database), usuario e senha
				   // Per�odo: Data e hora
				
				// Tabela: processo, ordem, tabela origem, operacao, tabela destino, salvar ap�s, ignorar erro, habilitado, coluna chave
				
				//Dire��o
				//a -> b (Cont�m os dados para a conex�o)
				//Lista de tabelas para replica��o
				/*
				[ 'Dire��o 1' =>
					'tabelas' => [
					    'tabela 1',
					    'tabela 2',
					    'tabela 3'
					]
				],
				
				[ 'Dire��o 2' =>
					'tabelas' => [
					    'tabela 1',
					    'tabela 2',
					    'tabela 3'
					]
				]
				
				*/
		
		createThread();
	}
	
	private void createThread() {
		executionThread = new Thread(new Runnable() {
			public void run() {
				try {
					Thread thisThread = Thread.currentThread();
					
					while (!Thread.currentThread().isInterrupted() && executionThread == thisThread) {
						System.out.println("...");
						
						if (!threadSuspended) {
							System.out.println("Runninnnnnnnnnnnnnnng!!!");
							
							window.setProgressBarValue(66);
							// TODO: Realizar replicacao
							
							//TESTE
							/*window.showDirection("master", "masterCopy");
							window.showProcess("Manual");
							window.showTable("Alunos");
							window.showCountTables(4);
							window.showErrors(1);
							window.startProgressIndeterminate(true);
							window.progressValue(i+=25);
							if(i == 100) {
								i = 0;
								window.showCountTables(3);
							}*/
							////
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
