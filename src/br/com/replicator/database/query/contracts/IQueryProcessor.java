package br.com.replicator.database.query.contracts;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IQueryProcessor {
	public ResultSet executeQuery(IQuery query) throws SQLException;
	
	public Integer executeUpdate(IQuery query) throws SQLException;
	
	public ResultSet execute(IQuery query) throws SQLException;
}
