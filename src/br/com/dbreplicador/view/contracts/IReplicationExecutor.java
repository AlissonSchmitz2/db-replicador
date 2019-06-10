package br.com.dbreplicador.view.contracts;

import java.sql.Timestamp;

public interface IReplicationExecutor {
	public void start(Timestamp fromDate);
	
	public void resume();
	
	public void pause();
	
	public void stop();
	
	public boolean isRunning();
	
	public boolean isClosed();
}
