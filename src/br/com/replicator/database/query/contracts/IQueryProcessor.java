package br.com.replicator.database.query.contracts;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IQueryProcessor {
	public ResultSet executeQuery(String query) throws SQLException;
	
	public int executeUpdate(String query) throws SQLException;
}
