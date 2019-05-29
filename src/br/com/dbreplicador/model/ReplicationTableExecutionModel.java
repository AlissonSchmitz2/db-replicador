package br.com.dbreplicador.model;

import java.util.Date;

public class ReplicationTableExecutionModel extends AbstractModel {
	private Integer codigo_tabela;
	private Date data_atual;
	private String processo;
	private String database_origem;
	private String usuario_origem;
	private String database_destino;
	private String usuario_destino;
	private Date execucao_inicio_data_hora;
	private Integer ordem;
	private Date inicio_data_hora;
	private Date fim_data_hora;
	private Date data_atual_ate;
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
	public Date getCurrentDate() {
		return data_atual;
	}
	
	/**
	 * data_atual
	 * 
	 * @param currentDate
	 */
	public void setCurrentDate(Date currentDate) {
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
	 * execucao_inicio_data_hora
	 */
	public Date getExecutionStartDateTime() {
		return execucao_inicio_data_hora;
	}
	
	/**
	 * execucao_inicio_data_hora
	 * 
	 * @param executionStartDateTime
	 */
	public void setExecutionStartDateTime(Date executionStartDateTime) {
		this.execucao_inicio_data_hora = executionStartDateTime;
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
	public Date getStartDateTime() {
		return inicio_data_hora;
	}
	
	/**
	 * inicio_data_hora
	 * 
	 * @param startDateTime
	 */
	public void setStartDateTime(Date startDateTime) {
		this.inicio_data_hora = startDateTime;
	}
	
	/**
	 * fim_data_hora
	 */
	public Date getFinishDateTime() {
		return fim_data_hora;
	}
	
	/**
	 * fim_data_hora
	 * 
	 * @param finishDateTime
	 */
	public void setFinishDateTime(Date finishDateTime) {
		this.fim_data_hora = finishDateTime;
	}
	
	/**
	 * data_atual_ate
	 */
	public Date getCurrentDateUntil() {
		return data_atual_ate;
	}
	
	/**
	 * data_atual_ate
	 * 
	 * @param currentDateUntil
	 */
	public void setCurrentDateUntil(Date currentDateUntil) {
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
