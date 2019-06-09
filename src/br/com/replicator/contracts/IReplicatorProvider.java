package br.com.replicator.contracts;

import java.sql.Connection;

import br.com.replicator.database.query.contracts.IQueryBuilder;
import br.com.replicator.database.query.contracts.IQueryProcessor;

public interface IReplicatorProvider {
	public Connection getConn();

	public IQueryBuilder getQueryBuilder();

	public IQueryProcessor getProcessor();
}
