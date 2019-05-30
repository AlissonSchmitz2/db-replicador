package br.com.dbreplicador.controller;

public interface ReplicatorController {

	public void showDirection(final String directionOrigin, final String directionDestiny);

	public void showTable(final String table);

	public void showCountTables(final int count);

	public void showErrors(final int count);

	public void showProcess(final String process);

	public void progressValue(final int value);

	public void startProgressIndetermined(final boolean start);
}
