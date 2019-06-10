package br.com.dbreplicador.model;

import java.sql.Timestamp;

public class ConnectionModel  extends AbstractModel {
	private Integer codigo_replicacao;
	private Timestamp data_atual;
	private String usuario;
	private String nome;
	private String endereco;
	private Integer porta;
	private String database;
	private String tipo_banco;
	private String url;
	
	/**
	 * codigo_replicacao
	 */
	public Integer getReplicationCode() {
		return codigo_replicacao;
	}
	
	/**
	 * codigo_replicacao
	 * 
	 * @param replicationCode
	 */
	public void setReplicationCode(Integer replicationCode) {
		this.codigo_replicacao = replicationCode;
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
	 * nome
	 */
	public String getName() {
		return nome;
	}
	
	/**
	 * nome
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.nome = name;
	}
	
	/**
	 * endereco
	 */
	public String getAddress() {
		return endereco;
	}
	
	/**
	 * endereco
	 * 
	 * @param address
	 */
	public void setAddress(String address) {
		this.endereco = address;
	}
	
	/**
	 * porta
	 */
	public Integer getPort() {
		return porta;
	}
	
	/**
	 * porta
	 * 
	 * @param port
	 */
	public void setPort(Integer port) {
		this.porta = port;
	}
	
	/**
	 * database
	 */
	public String getDatabase() {
		return database;
	}
	
	/**
	 * database
	 * 
	 * @param database
	 */
	public void setDatabase(String database) {
		this.database = database;
	}
	
	/**
	 * tipo_banco
	 */
	public String getDatebaseType() {
		return tipo_banco;
	}
	
	/**
	 * tipo_banco
	 * 
	 * @param datebaseType
	 */
	public void setDatebaseType(String datebaseType) {
		this.tipo_banco = datebaseType;
	}
	
	/**
	 * url
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * url
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}	
	
}
