package br.com.replicator.database.query.contracts;

import br.com.replicator.enums.QueryTypes;

public interface IQuery {
	public String toString();
	
	public QueryTypes getType();
}