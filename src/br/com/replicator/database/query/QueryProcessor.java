package br.com.replicator.database.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.replicator.database.query.contracts.IQuery;
import br.com.replicator.database.query.contracts.IQueryProcessor;

public class QueryProcessor implements IQueryProcessor {
	private Connection connection;
	
	public QueryProcessor(Connection connection) throws SQLException {
		this.connection = connection;
	}

	public ResultSet executeQuery(IQuery query) throws SQLException {
		PreparedStatement pst = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
		
		if (!connection.getAutoCommit()) {
			connection.commit();
		}

		return pst.executeQuery();
	}
	
	public int executeUpdate(IQuery query) throws SQLException {
		PreparedStatement pst = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);

		int result = pst.executeUpdate();
		
		if (!connection.getAutoCommit() && result > 0) {
			connection.commit();
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T executeUpdate(IQuery query, String uniqueKeyColumn) throws SQLException {
		PreparedStatement pst = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);

		int result = pst.executeUpdate();
		
		if (!connection.getAutoCommit() && result > 0) {
			connection.commit();
		}
		
		if(result > 0) {
			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				return (T)rs.getObject(uniqueKeyColumn);
			}
		}
		
		return null;
	}
	
	public ResultSet execute(IQuery query) throws SQLException {
		PreparedStatement pst = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
		
		pst.execute();
		
		if (!connection.getAutoCommit()) {
			connection.commit();
		}
		
		return pst.getResultSet();
	}
}
