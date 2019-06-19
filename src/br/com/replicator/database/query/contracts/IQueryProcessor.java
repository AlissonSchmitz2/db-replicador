package br.com.replicator.database.query.contracts;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IQueryProcessor {
	public ResultSet executeQuery(IQuery query) throws SQLException;
	
	public ResultSet execute(IQuery query) throws SQLException;
	
	public int executeUpdate(IQuery query) throws SQLException;
	
	public <T> T executeUpdate(IQuery query, String uniqueKeyColumn) throws SQLException;
}
