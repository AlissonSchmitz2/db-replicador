package br.com.dbreplicador.view.contracts;

public interface IReplicationProcessingInfo {
	public String getCurrentOriginDirection();
	
	public String getCurrentDestinationDirection();

	public String getCurrentTable();

	public Integer getTotalOfTables();

	public Integer getTotalOfErrors();

	public String getCurrentProcess();

	public Integer getProcessingProgress();
}
