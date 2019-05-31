package br.com.dbreplicador.view;

public class ReplicatorExecute {

	private ReplicatorWindow window;

	int i = 0;
	
	public ReplicatorExecute(ReplicatorWindow window) {

		// Guardo a referencia.
		this.window = window;

		// Guardo a conexão.

		// Instancia os DAO de controle.
	}

	public void ReplicacaoIniciar() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					
					while (!Thread.currentThread().isInterrupted()) {

						// TODO: Realizar replicacao
						
						//TESTE
						window.showDirection("master", "masterCopy");
						window.showProcess("Manual");
						window.showTable("Alunos");
						window.showCountTables(4);
						window.showErrors(1);
						window.startProgressIndeterminate(true);
						window.progressValue(i+=25);
						if(i == 100) {
							i = 0;
							window.showCountTables(3);
						}
						////
						
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

}
