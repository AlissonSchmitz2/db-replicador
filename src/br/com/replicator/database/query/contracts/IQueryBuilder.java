package br.com.replicator.database.query.contracts;

import br.com.replicator.exceptions.InvalidQueryAttributesException;

public interface IQueryBuilder {
	public String createInsert(String tableName, String[] columns, String[] values) throws InvalidQueryAttributesException;
	
	public String createUpdate(String tableName, String[] columns, String[] values, String identifierColumn, String identifierValue) throws InvalidQueryAttributesException;
	
	public String createDelete(String tableName, String identifierColumn, String identifierValue) throws InvalidQueryAttributesException;
	
	public String createFind(String tableName, String identifierColumn, String identifierValue, String columns) throws InvalidQueryAttributesException;
}
