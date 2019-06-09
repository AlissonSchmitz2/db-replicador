package br.com.replicator.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class ConnectionFactory {
	private static HashMap<String, Connection> conn = new HashMap<String, Connection>();
	
	private ConnectionFactory() {}
	
	public static Connection getConnection(ConnectionInfo connInfo) throws SQLException {
		String url = "jdbc:" + connInfo.getDbType().getCode() + "://" + connInfo.getDbHost() + ":" + connInfo.getDbPort() + "/" + connInfo.getDbName();
		
		//Garante que seja criado somente uma conexão por url
		if (!conn.containsKey(url)) {
			conn.put(url, DriverManager.getConnection(url, connInfo.getUsername(), connInfo.getPassword()));
		}

		return conn.get(url);
	}
}
