package br.com.replicator.database.query;

import br.com.replicator.SupportedTypes;
import br.com.replicator.database.query.contracts.IQueryBuilder;
import br.com.replicator.exceptions.InvalidDatabaseTypeException;

abstract public class QueryBuilderFactory {
	public static IQueryBuilder getQueryBuilder(SupportedTypes dbType) throws InvalidDatabaseTypeException {
		switch (dbType.getCode()) {
			case "postgresql":
				return new MySqlQueryBuilder();
			default:
				throw new InvalidDatabaseTypeException("Invalid database type");
		}
	}
}
