package br.com.dbreplicador.view;

public class ReplicatorExecute {

	private ReplicatorWindow window;
	
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
				
				//TODO: Realizar replicacao

				while (!Thread.currentThread().isInterrupted() ) {
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}
		}).start();
		
	}
	
}
