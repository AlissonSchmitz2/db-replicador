package br.com.replicator.database.query;

import java.util.TreeMap;

import br.com.replicator.database.query.contracts.IQuery;
import br.com.replicator.database.query.contracts.IQueryBuilder;
import br.com.replicator.enums.QueryTypes;
import br.com.replicator.exceptions.InvalidQueryAttributesException;

public abstract class AbstractQueryBuilder implements IQueryBuilder {
	public IQuery insert(String tableName, String[] columns, String[] values) throws InvalidQueryAttributesException {
		if (columns.length == 0) {
			throw new InvalidQueryAttributesException("O total de colunas n�o pode ser zero");
		}
		
		if (columns.length != values.length) {
			throw new InvalidQueryAttributesException("N�mero de colunas � diferente do n�mero de colunas");
		}
		
		TreeMap<String, String> columnsValuesPairs = new TreeMap<String, String>();
		for (int i = 0; i < columns.length; i++) {
			if(values.length > i && values[i] != null) {
				columnsValuesPairs.put(columns[i], "'" + values[i] + "'");
			}
        }
		
		String query = "INSERT INTO " + tableName + " (";
		
		// Concatena colunas	
		query += String.join(",", columnsValuesPairs.keySet());
		
		query += ") VALUES (";
		
		// Concatena valores
		query += String.join(",", columnsValuesPairs.values());
		
		query += ")";
		
		return new Query(query, QueryTypes.INSERT);
	}
	
	public IQuery update(String tableName, String[] columns, String[] values, String identifierColumn, String identifierValue) throws InvalidQueryAttributesException {
		if (columns.length == 0) {
			throw new InvalidQueryAttributesException("O total de colunas n�o pode ser zero");
		}
		
		if (columns.length != values.length) {
			throw new InvalidQueryAttributesException("N�mero de colunas � diferente do n�mero de colunas");
		}
		
		if (identifierColumn.isEmpty() || identifierValue.isEmpty()) {
			throw new InvalidQueryAttributesException("Valor do identificador � inv�lido");
		}
		
		String query = "UPDATE " + tableName + " SET ";
		
		String[] columnsValuesPairs = new String[columns.length];
		for (int i = 0; i < columns.length; i++) {
			if (values.length > i && values[i] != null) {
				columnsValuesPairs[i] = columns[i] + "='" + values[i] + "'";
			} else {
				columnsValuesPairs[i] = columns[i] + "=" + values[i];
			}
        }
		
		// Concatena colunas
		query += String.join(", ", columnsValuesPairs);
		query += " ";
		query += createWhereCondition(identifierColumn, "=", identifierValue);
		
		return new Query(query, QueryTypes.UPDATE);
	}
	
	public IQuery delete(String tableName, String identifierColumn, String identifierValue) throws InvalidQueryAttributesException {
		if (identifierColumn.isEmpty() || identifierValue.isEmpty()) {
			throw new InvalidQueryAttributesException("Valor do identificador � inv�lido");
		}
		
		String query = "DELETE FROM " + tableName + " " + createWhereCondition(identifierColumn, "=", identifierValue);
		
		return new Query(query, QueryTypes.DELETE);
	}
	
	public IQuery find(String tableName, String identifierColumn, String operator, String identifierValue, String columns) throws InvalidQueryAttributesException {
		if (identifierColumn.isEmpty() || identifierValue.isEmpty()) {
			throw new InvalidQueryAttributesException("Valor do identificador � inv�lido");
		}
		
		String query = "SELECT " + columns + " FROM " + tableName + " " + createWhereCondition(identifierColumn, operator, identifierValue);
		
		return new Query(query, QueryTypes.SELECT);
	}
	
	public IQuery select(String tableName, String columns) throws InvalidQueryAttributesException {
		String query = "SELECT " + columns + " FROM " + tableName;
		
		return new Query(query, QueryTypes.SELECT);
	}
	
	private String createWhereCondition(String identifierColumn, String operator, String identifierValue) {
		return "WHERE " + identifierColumn + operator + "'" + identifierValue +"'";
	}
}
