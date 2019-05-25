package br.com.dbreplicador.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	private static String DB_HOST = "localhost";
	private static String DB_PORT = "5432";
	
	private static String DEFAULT_DB_NAME = "replicador";
	
	public static Connection getConnection(
			final String username,
			final String password) {
		return getConnection(DB_HOST, DB_PORT, DEFAULT_DB_NAME, username, password);
	}
	
	public static Connection getConnection(
			final String dbName,
			final String username,
			final String password) {
		return getConnection(DB_HOST, DB_PORT, dbName, username, password);
	}

	public static Connection getConnection(
			final String dbHost,
			final String port,
			final String dbName,
			final String username,
			final String password) {
		Connection conn = null;

		try {
			conn = DriverManager.getConnection("jdbc:postgresql://" + dbHost + ":" + port + "/" + dbName, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return conn;
	}
}
