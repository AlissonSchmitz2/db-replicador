package br.com.replicator.database.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryProcessor implements br.com.replicator.database.query.contracts.IQueryProcessor {
	private Connection connection;
	
	public QueryProcessor(Connection connection) throws SQLException {
		this.connection = connection;

		this.connection.setAutoCommit(false);
	}

	public ResultSet executeQuery(String query) throws SQLException {
		PreparedStatement pst = connection.prepareStatement(query);

		return pst.executeQuery();
	}
	
	public int executeUpdate(String query) throws SQLException {
		PreparedStatement pst = connection.prepareStatement(query);

		return pst.executeUpdate();
	}
}
