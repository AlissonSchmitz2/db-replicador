package br.com.replicator;

import java.sql.Connection;
import java.sql.SQLException;

import br.com.replicator.contracts.IReplicatorProvider;
import br.com.replicator.database.ConnectionFactory;
import br.com.replicator.database.ConnectionInfo;
import br.com.replicator.database.query.QueryBuilderFactory;
import br.com.replicator.database.query.QueryProcessor;
import br.com.replicator.database.query.contracts.IQueryBuilder;
import br.com.replicator.database.query.contracts.IQueryProcessor;
import br.com.replicator.exceptions.InvalidDatabaseTypeException;

public class ReplicatorProvider implements IReplicatorProvider {
	private Connection conn;
	
	private IQueryBuilder queryBuilder;
	
	private IQueryProcessor processor;
	
	public ReplicatorProvider(ConnectionInfo connInfo) throws SQLException, InvalidDatabaseTypeException {
		conn = ConnectionFactory.getConnection(connInfo);
		queryBuilder = QueryBuilderFactory.getQueryBuilder(connInfo.getDbType());
		processor = new QueryProcessor(conn);
	}

	public Connection getConn() {
		return conn;
	}

	public IQueryBuilder getQueryBuilder() {
		return queryBuilder;
	}

	public IQueryProcessor getProcessor() {
		return processor;
	}
}
