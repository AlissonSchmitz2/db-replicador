package br.com.dbreplicador.view.contracts;

public interface IReplicationInfoControl {

	public void setCurrentDirections(final String directionOrigin, final String directionDestiny);

	public void setCurrentTable(final String table);

	public void setTotalOfTables(final int count);

	public void setTotalOfErrors(final int count);

	public void setCurrentProcess(final String process);

	public void setProgressBarValue(final int value);

	public void setProgressIndeterminate(final boolean start);
}
