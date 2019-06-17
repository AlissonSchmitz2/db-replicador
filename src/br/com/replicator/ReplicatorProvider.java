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
	
	private ConnectionInfo connInfo;
	
	private IQueryBuilder queryBuilder;
	
	public ReplicatorProvider(ConnectionInfo connInfo) throws SQLException, InvalidDatabaseTypeException {
		this.connInfo = connInfo;
		conn = ConnectionFactory.getConnection(connInfo);
		queryBuilder = QueryBuilderFactory.getQueryBuilder(connInfo.getDbType());
	}

	public Connection getConn() throws SQLException {
		//Garante que sempre exista uma conexão ativa apartir do provider
		if (conn.isClosed()) {
			conn = ConnectionFactory.getConnection(connInfo, true);
		}
		
		return conn;
	}

	public IQueryBuilder getQueryBuilder() {
		return queryBuilder;
	}

	public IQueryProcessor getProcessor() throws SQLException {
		return new QueryProcessor(getConn());
	}
}
