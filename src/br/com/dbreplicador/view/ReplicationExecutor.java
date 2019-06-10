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
		//Guarda a referência da tela de replicação
		this.window = window;

		// Guardo a conexão.

		// Instancia os DAO de controle.
	}

	public void start() {
		//Se a thread for nula, um novo processo é inicializado
		//Ou seja, o processo de replicação é iniciado do zero
		if (executionThread == null) {
			initializeProcessing();
			
			executionThread.start();
		}
		
		//Caso o processo já exista, o mesmo é retirado da suspensão
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
	
	private void initializeProcessing() {
		//TODO: Iterar para recuperar os registros a serem replicados
		
		// Conexão: host, porta, banco e tipo
		
				// Processo: processo, data de, ignorar erro e habilitado
				
				// Direção: (processo), duração, retenção, automatico, habilitado
				   // Origem: (Conexão: database), usuario e senha
				   // Destino: (Conexão: database), usuario e senha
				   // Período: Data e hora
				
				// Tabela: processo, ordem, tabela origem, operacao, tabela destino, salvar após, ignorar erro, habilitado, coluna chave
				
				//Direção
				//a -> b (Contém os dados para a conexão)
				//Lista de tabelas para replicação
				/*
				[ 'Direção 1' =>
					'tabelas' => [
					    'tabela 1',
					    'tabela 2',
					    'tabela 3'
					]
				],
				
				[ 'Direção 2' =>
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
