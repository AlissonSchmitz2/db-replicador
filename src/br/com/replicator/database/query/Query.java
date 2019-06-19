package br.com.replicator.database.query;

import br.com.replicator.database.query.contracts.IQuery;
import br.com.replicator.enums.QueryTypes;

public class Query implements IQuery {
	private String query;
	private QueryTypes type;

	public Query(String query, QueryTypes type) {
		this.query = query;
		this.type = type;
	}

	public String toString() {
		return query;
	}

	public QueryTypes getType() {
		return type;
	}
}
