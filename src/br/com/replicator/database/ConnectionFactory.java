package br.com.replicator.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	
	public static Connection getConnection(
			final String dbType,
			final String dbHost,
			final int dbPort,
			final String dbName,
			final String username,
			final String password) {
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:" + dbType + "://" + dbHost + ":" + dbPort + "/" + dbName, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return conn;
	}
}
