package br.com.dbreplicador.model;

import java.sql.Timestamp;

public class ReplicationExecuteModel extends AbstractModel{
	private int codigo_execucao;
	private Timestamp data_atual;
	private String usuario;
	private String database_origem;
	private String usuario_origem;
	private String database_destino;
	private String usuario_destino;
	private Timestamp inicio_data_hora;
	private Timestamp fim_data_hora;
	private Timestamp data_atual_ate;
	private String ocorrencia;
	private String processo;
	
	/**
	 * codigo_execucao
	 */
	public int getExecuteCode() {
		return codigo_execucao;
	}
	
	/**
	 * codigo_execucao
	 * 
	 * @param executeCode
	 */
	public void setExecuteCode(int executeCode) {
		codigo_execucao = executeCode;
	}
	
	/**
	 * codigo_execucao
	 */
	public Timestamp getCurrentDate() {
		return data_atual;
	}
	
	/**
	 * data_atual
	 * 
	 * @param currencyDate
	 */
	public void setCurrentDate(Timestamp currencyDate) {
		data_atual = currencyDate;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return usuario;
	}

	/**
	 * @param user
	 */
	public void setUser(String user) {
		this.usuario = user;
	}

	/**
	 * @return database_origem
	 */
	public String getOriginDatabase() {
		return database_origem;
	}

	/**
	 * @param originDatabase
	 */
	public void setOriginDatabase(String originDatabase) {
		this.database_origem = originDatabase;
	}

	/**
	 * @return usuario_origem
	 */
	public String getOriginUser() {
		return usuario_origem;
	}

	/**
	 * @param originUser
	 */
	public void setOriginUser(String originUser) {
		this.usuario_origem = originUser;
	}

	/**
	 * @return the database_destino
	 */
	public String getDestinatioDatabase() {
		return database_destino;
	}

	/**
	 * @param destinatioDatabase
	 */
	public void setDestinatioDatabase(String destinatioDatabase) {
		this.database_destino = destinatioDatabase;
	}

	/**
	 * @return the usuario_destino
	 */
	public String getDestinationUser() {
		return usuario_destino;
	}

	/**
	 * @param destinationUser
	 */
	public void setDestinationUser(String destinationUser) {
		this.usuario_destino = destinationUser;
	}

	/**
	 * @return the inicio_data_hora
	 */
	public Timestamp getDateHourInitial() {
		return inicio_data_hora;
	}

	/**
	 * @param dateHourInitial
	 */
	public void setDateHourInitial(Timestamp dateHourInitial) {
		this.inicio_data_hora = dateHourInitial;
	}

	/**
	 * @return fim_data_hora
	 */
	public Timestamp getDateHourFinal() {
		return fim_data_hora;
	}

	/**
	 * @param dateHourFinal
	 */
	public void setDateHourFinal(Timestamp dateHourFinal) {
		this.fim_data_hora = dateHourFinal;
	}

	/**
	 * @return data_atual_ate
	 */
	public Timestamp getCurrenteDateTo() {
		return data_atual_ate;
	}

	/**
	 * @param currenteDateTo 
	 */
	public void setCurrenteDateTo(Timestamp currenteDateTo) {
		this.data_atual_ate = currenteDateTo;
	}

	/**
	 * @return ocorrencia
	 */
	public String getOccurrence() {
		return ocorrencia;
	}

	/**
	 * @param ocurrence 
	 */
	public void setOccurrence(String ocurrence) {
		this.ocorrencia = ocurrence;
	}
	
	/**
	 * @return process
	 */
	public String getProcess() {
		return processo;
	}

	/**
	 * @param process 
	 */
	public void setProcess(String process) {
		this.processo = process;
	}
}
