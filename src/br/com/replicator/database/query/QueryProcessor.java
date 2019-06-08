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

		this.connection.setAutoCommit(true);
	}

	public ResultSet executeQuery(IQuery query) throws SQLException {
		PreparedStatement pst = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);

		return pst.executeQuery();
	}
	
	public Integer executeUpdate(IQuery query) throws SQLException {
		PreparedStatement pst = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);

		int result = pst.executeUpdate();
		if(result > 0) {
			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				return rs.getInt(1);
			}
		}
		
		return null;
	}
	
	public ResultSet execute(IQuery query) throws SQLException {
		PreparedStatement pst = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
		
		pst.execute();
		
		return pst.getResultSet();
	}
}
