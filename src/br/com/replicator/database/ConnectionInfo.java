package br.com.replicator.database;

import br.com.replicator.enums.SupportedTypes;

public class ConnectionInfo {
	private SupportedTypes dbType;
	private String dbHost;
	private int dbPort;
	private String dbName;
	private String username;
	private String password;
	
	public ConnectionInfo(
			SupportedTypes dbType,
			String dbHost,
			int dbPort,
			String dbName,
			String username,
			String password
	) {
		this.dbType = dbType;
		this.dbHost = dbHost;
		this.dbPort = dbPort;
		this.dbName = dbName;
		this.username = username;
		this.password = password;
	}
	
	public SupportedTypes getDbType() {
		return dbType;
	}

	public String getDbHost() {
		return dbHost;
	}

	public int getDbPort() {
		return dbPort;
	}

	public String getDbName() {
		return dbName;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
