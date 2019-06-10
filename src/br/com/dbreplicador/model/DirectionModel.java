package br.com.dbreplicador.model;

import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.sql.Timestamp;

public class DirectionModel extends AbstractModel{
	
	private int codigo_direcao;
	private Timestamp data_atual;
	private String usuario;
	private String processo;
	private String database_origem;
	private String usuario_origem;
	private String senha_origem;
	private String database_destino;
	private String usuario_destino;
	private String senha_destino;
	private boolean automatico_manual;
	private int periodo_ano;
	private int periodo_mes;
	private int periodo_dia;
	private int periodo_hora;
	private int periodo_minuto;
	private int periodo_segundo;
	private int executar_dia_de;
	private int executar_dia_ate;
	private int executar_hora_de;
	private int executar_hora_ate;
	private int duracao_maximo;
	private Date execucao_ultima;
	private int retencao;					
	private boolean habilitado;
	
	/*
	 * Relacionamentos
	 */
	private ConnectionModel originConnectionModel;
	private ConnectionModel destinationConnectionModel;
	private AbstractMap<Integer, TableModel> tables = new HashMap<Integer, TableModel>();
	
	/**
	 * @return codigo_direcao
	 */
	public int getDirectionCode() {
		return codigo_direcao;
	}
	
	/**
	 * @param directionCode 
	 */
	public void setDirectionCode(int directionCode) {
		this.codigo_direcao = directionCode;
	}
	
	/**
	 * @return the data_atual
	 */
	public Timestamp getCurrentDate() {
		return data_atual;
	}
	
	/**
	 * @param currentDate
	 */
	public void setCurrentDate(Timestamp currentDate) {
		this.data_atual = currentDate;
	}
	
	/**
	 * @return the usuario
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
	 * @return the processo
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
	
	/**
	 * @return the database_origem
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
	 * @return originConnectionModel
	 */
	public ConnectionModel getOriginConnectionModel() {
		return originConnectionModel;
	}
	
	/**
	 * @param connectionModel
	 */
	public void setOriginConnectionModel(ConnectionModel connectionModel) {
		this.originConnectionModel = connectionModel;
	}
	
	/**
	 * @return the usuario_origem
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
	 * @return the senha_origem
	 */
	public String getOriginPassword() {
		return senha_origem;
	}
	
	/**
	 * @param originPassword
	 */
	public void setOriginPassword(String originPassword) {
		this.senha_origem = originPassword;
	}
	
	/**
	 * @return the database_destino
	 */
	public String getDestinationDatabase() {
		return database_destino;
	}
	
	/**
	 * @param destinationDatabase
	 */
	public void setDestinationDatabase(String destinationDatabase) {
		this.database_destino = destinationDatabase;
	}
	
	/**
	 * @return destinationConnectionModel
	 */
	public ConnectionModel getDestinationConnectionModel() {
		return destinationConnectionModel;
	}
	
	/**
	 * @param connectionModel
	 */
	public void setDestinationConnectionModel(ConnectionModel connectionModel) {
		this.destinationConnectionModel = connectionModel;
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
	 * @return the senha_destino
	 */
	public String getDestinationPassword() {
		return senha_destino;
	}
	
	/**
	 * @param destinationPassword
	 */
	public void setDestinationPassword(String destinationPassword) {
		this.senha_destino = destinationPassword;
	}
	
	/**
	 * @return the automatico_manual
	 */
	public boolean isAutomaticManual() {
		return automatico_manual;
	}
	
	/**
	 * @param automaticManual
	 */
	public void setAutomaticManual(boolean automaticManual) {
		this.automatico_manual = automaticManual;
	}
	
	/**
	 * @return the periodo_ano
	 */
	public int getYearPeriod() {
		return periodo_ano;
	}
	
	/**
	 * @param yearPeriod
	 */
	public void setYearPeriod(int yearPeriod) {
		this.periodo_ano = yearPeriod;
	}
	
	/**
	 * @return the periodo_mes
	 */
	public int getMounthPeriod() {
		return periodo_mes;
	}
	
	/**
	 * @param mounthPeriod
	 */
	public void setMonthPeriod(int mounthPeriod) {
		this.periodo_mes = mounthPeriod;
	}
	
	/**
	 * @return the periodo_dia
	 */
	public int getDayPeriod() {
		return periodo_dia;
	}
	
	/**
	 * @param dayPeriod
	 */
	public void setDayPeriod(int dayPeriod) {
		this.periodo_dia = dayPeriod;
	}
	
	/**
	 * @return the periodo_hora
	 */
	public int getHourPeriod() {
		return periodo_hora;
	}
	
	/**
	 * @param hourPeriod the periodo_hora to set
	 */
	public void setHourPeriod(int hourPeriod) {
		this.periodo_hora = hourPeriod;
	}
	
	/**
	 * @return the periodo_minuto
	 */
	public int getMinutePeriod() {
		return periodo_minuto;
	}
	
	/**
	 * @param minutePeriod 
	 */
	public void setMinutePeriod(int minutePeriod) {
		this.periodo_minuto = minutePeriod;
	}
	
	/**
	 * @return the periodo_segundo
	 */
	public int getSecondPeriod() {
		return periodo_segundo;
	}
	
	/**
	 * @param secundPeriod
	 */
	public void setSecondPeriod(int secundPeriod) {
		this.periodo_segundo = secundPeriod;
	}
	
	/**
	 * @return the executar_dia_de
	 */
	public int getExecuteDayOf() {
		return executar_dia_de;
	}
	
	/**
	 * @param executeDayOf
	 */
	public void setExecuteDayOf(int executeDayOf) {
		this.executar_dia_de = executeDayOf;
	}
	
	/**
	 * @return the executar_dia_ate
	 */
	public int getExecuteDayTo() {
		return executar_dia_ate;
	}
	
	/**
	 * @param executeDayTo
	 */
	public void setExecuteDayTo(int executeDayTo) {
		this.executar_dia_ate = executeDayTo;
	}
	
	/**
	 * @return the executar_hora_de
	 */
	public int getExecuteHourOf() {
		return executar_hora_de;
	}
	
	/**
	 * @param executeHourTo
	 */
	public void setExecuteHourOf(int executeHourOf) {
		this.executar_hora_de = executeHourOf;
	}
	
	/**
	 * @return the executar_hora_ate
	 */
	public int getExecuteHourTo() {
		return executar_hora_ate;
	}
	
	/**
	 * @param executar_hora_ate the executar_hora_ate to set
	 */
	public void setExecuteHourTo(int executeHourTo) {
		this.executar_hora_ate = executeHourTo;
	}
	
	/**
	 * @return the duracao_maximo
	 */
	public int getMaxDuration() {
		return duracao_maximo;
	}
	
	/**
	 * @param maxDuration 
	 */
	public void setMaxDuration(int maxDuration) {
		this.duracao_maximo = maxDuration;
	}
	
	/**
	 * @return the execucao_ultima
	 */
	public Date getExecuteLast() {
		return execucao_ultima;
	}
	
	/**
	 * @param executeLast 
	 */
	public void setExecuteLast(Date executeLast) {
		this.execucao_ultima = executeLast;
	}
	
	/**
	 * @return the retencao
	 */
	public int getRetention() {
		return retencao;
	}
	
	/**
	 * @param retention
	 */
	public void setRetention(int retention) {
		this.retencao = retention;
	}
	
	/**
	 * @return habilitado
	 */
	public boolean isEnabled() {
		return habilitado;
	}
	
	/**
	 * @param enable 
	 */
	public void setEnabled(boolean enable) {
		this.habilitado = enable;
	}
	
	/**
	 * @return tables
	 */
	public AbstractMap<Integer, TableModel> getTables() {
		return tables;
	}
	
	/**
	 * @param tables 
	 */
	public void setTables(AbstractMap<Integer, TableModel> tables) {
		this.tables = tables;
	}
}
