package br.com.dbreplicador.model;

import java.sql.Timestamp;

public class ReplicationProcessModel extends AbstractModel {
	private Integer codigo_processo;
	private Timestamp data_atual;
	private String usuario;
	private String processo;
	private String descricao;
	private Timestamp data_atual_de;
	private boolean erro_ignorar;
	private boolean habilitado;
	
	/**
	 * codigo_processo
	 */
	public Integer getProcessCode() {
		return codigo_processo;
	}
	
	/**
	 * codigo_processo
	 * 
	 * @param processCode
	 */
	public void setProcessCode(Integer processCode) {
		this.codigo_processo = processCode;
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
	 * usuario
	 */
	public String getUser() {
		return usuario;
	}
	
	/**
	 * usuario
	 * 
	 * @param user
	 */
	public void setUser(String user) {
		this.usuario = user;
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
	 * descricao
	 */
	public String getDescription() {
		return descricao;
	}
	
	/**
	 * descricao
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.descricao = description;
	}
	
	/**
	 * data_atual_de
	 */
	public Timestamp getCurrentDateOf() {
		return data_atual_de;
	}
	
	/**
	 * data_atual_de
	 * 
	 * @param currentDateOf
	 */
	public void setCurrentDateOf(Timestamp currentDateOf) {
		this.data_atual_de = currentDateOf;
	}
	
	/**
	 * erro_ignorar
	 */
	public boolean isErrorIgnore() {
		return erro_ignorar;
	}
	
	/**
	 * erro_ignorar
	 * 
	 * @param errorIgnore
	 */
	public void setErrorIgnore(boolean errorIgnore) {
		this.erro_ignorar = errorIgnore;
	}
	
	/**
	 * habilitado
	 */
	public boolean isEnable() {
		return habilitado;
	}
	
	/**
	 * habilitado
	 * 
	 * @param enable
	 */
	public void setEnable(boolean enable) {
		this.habilitado = enable;
	}		

}
