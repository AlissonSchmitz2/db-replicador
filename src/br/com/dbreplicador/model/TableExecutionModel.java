package br.com.dbreplicador.model;

import java.sql.Timestamp;

public class TableExecutionModel extends AbstractModel {
	private Integer codigo_tabela;
	private Timestamp data_atual;
	private String processo;
	private String database_origem;
	private String usuario_origem;
	private String database_destino;
	private String usuario_destino;
	private Integer ordem;
	private Timestamp inicio_data_hora;
	private Timestamp fim_data_hora;
	private Timestamp data_atual_ate;
	private int linhas_processadas;
	private boolean sucesso;
	private String mensagem;
	
	/**
	 * codigo_tabela
	 */
	public Integer getTableCode() {
		return codigo_tabela;
	}
	
	/**
	 * codigo_tabela
	 * 
	 * @param tableCode
	 */
	public void setTableCode(Integer tableCode) {
		this.codigo_tabela = tableCode;
	}
	
	/**
	 * data_atual
	 */
	public Timestamp getCurrentDate() {
		return data_atual;
	}
	
	/**
	 * data_atual
	 * 
	 * @param currentDate
	 */
	public void setCurrentDate(Timestamp currentDate) {
		this.data_atual = currentDate;
	}
	
	/**
	 * processo
	 */
	public String getProcess() {
		return processo;
	}
	
	/**
	 * processo
	 * 
	 * @param process
	 */
	public void setProcess(String process) {
		this.processo = process;
	}
	
	/**
	 * database_origem
	 */
	public String getOriginDatabase() {
		return database_origem;
	}
	
	/**
	 * database_origem
	 * 
	 * @param originDatabase
	 */
	public void setOriginDatabase(String originDatabase) {
		this.database_origem = originDatabase;
	}
	
	/**
	 * usuario_origem
	 */
	public String getOriginUser() {
		return usuario_origem;
	}
	
	/**
	 * usuario_origem
	 * 
	 * @param originUser
	 */
	public void setOriginUser(String originUser) {
		this.usuario_origem = originUser;
	}
	
	/**
	 * database_destino
	 */
	public String getDestinationDatabase() {
		return database_destino;
	}
	
	/**
	 * database_destino
	 * 
	 * @param destinationDatabase
	 */
	public void setDestinationDatabase(String destinationDatabase) {
		this.database_destino = destinationDatabase;
	}
	
	/**
	 * usuario_destino
	 */
	public String getDestinationUser() {
		return usuario_destino;
	}
	
	/**
	 * usuario_destino
	 * 
	 * @param destinationUser
	 */
	public void setDestinationUser(String destinationUser) {
		this.usuario_destino = destinationUser;
	}
	
	/**
	 * ordem
	 */
	public Integer getOrder() {
		return ordem;
	}
	
	/**
	 * ordem
	 * 
	 * @param order
	 */
	public void setOrder(Integer order) {
		this.ordem = order;
	}
	
	/**
	 * inicio_data_hora
	 */
	public Timestamp getStartDateTime() {
		return inicio_data_hora;
	}
	
	/**
	 * inicio_data_hora
	 * 
	 * @param startDateTime
	 */
	public void setStartDateTime(Timestamp startDateTime) {
		this.inicio_data_hora = startDateTime;
	}
	
	/**
	 * fim_data_hora
	 */
	public Timestamp getFinishDateTime() {
		return fim_data_hora;
	}
	
	/**
	 * fim_data_hora
	 * 
	 * @param finishDateTime
	 */
	public void setFinishDateTime(Timestamp finishDateTime) {
		this.fim_data_hora = finishDateTime;
	}
	
	/**
	 * data_atual_ate
	 */
	public Timestamp getCurrentDateUntil() {
		return data_atual_ate;
	}
	
	/**
	 * data_atual_ate
	 * 
	 * @param currentDateUntil
	 */
	public void setCurrentDateUntil(Timestamp currentDateUntil) {
		this.data_atual_ate = currentDateUntil;
	}
	
	/**
	 * linhas_processadas
	 */
	public int getProcessedLines() {
		return linhas_processadas;
	}
	
	/**
	 * linhas_processadas
	 * 
	 * @param processedLines
	 */
	public void setProcessedLines(int processedLines) {
		this.linhas_processadas = processedLines;
	}
	
	/**
	 * sucesso
	 */
	public boolean isSucess() {
		return sucesso;
	}
	
	/**
	 * sucesso
	 * 
	 * @param sucess
	 */
	public void setSucess(boolean sucess) {
		this.sucesso = sucess;
	}
	
	/**
	 * mensagem
	 */
	public String getMessage() {
		return mensagem;
	}
	
	/**
	 * mensagem
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.mensagem = message;
	}	

}
