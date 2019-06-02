package br.com.replicator.database.query;

import br.com.replicator.database.query.contracts.IQueryBuilder;
import br.com.replicator.exceptions.InvalidQueryAttributesException;

public abstract class AbstractQueryBuilder implements IQueryBuilder {
	public String createInsert(String tableName, String[] columns, String[] values) throws InvalidQueryAttributesException {
		if (columns.length == 0) {
			throw new InvalidQueryAttributesException("O total de colunas não pode ser zero");
		}
		
		if (columns.length != values.length) {
			throw new InvalidQueryAttributesException("Número de colunas é diferente do número de colunas");
		}
		
		String query = "INSERT INTO " + tableName + " (";
		
		// Concatena colunas	
		query += String.join(",", columns);
		
		query += ") VALUES (";
		
		// Concatena valores
		String concatenedValues = "";
		for (int i = 0; i < columns.length; i++) {
			if (!concatenedValues.isEmpty()) {
				concatenedValues += ", ";
			}
			
			if(values.length > i && values[i] != null) {
				concatenedValues += "'" + values[i] + "'";
			} else {
				concatenedValues += "''";
			}
        }
		
		query += concatenedValues + ")";
		
		return query;
	}
	
	public String createUpdate(String tableName, String[] columns, String[] values, String identifierColumn, String identifierValue) throws InvalidQueryAttributesException {
		if (columns.length == 0) {
			throw new InvalidQueryAttributesException("O total de colunas não pode ser zero");
		}
		
		if (columns.length != values.length) {
			throw new InvalidQueryAttributesException("Número de colunas é diferente do número de colunas");
		}
		
		if (identifierColumn.isEmpty() || identifierValue.isEmpty()) {
			throw new InvalidQueryAttributesException("Valor do identificador é inválido");
		}
		
		String query = "UPDATE " + tableName + " SET ";
		
		String[] columnsValuesPairs = new String[columns.length];
		for (int i = 0; i < columns.length; i++) {
			columnsValuesPairs[i] = columns[i] + "='" + values[i] + "'";
        }
		
		// Concatena colunas
		query += String.join(", ", columnsValuesPairs);
		query += " ";
		query += createWhereCondition(identifierColumn, identifierValue);
		
		return query;
	}
	
	public String createDelete(String tableName, String identifierColumn, String identifierValue) throws InvalidQueryAttributesException {
		if (identifierColumn.isEmpty() || identifierValue.isEmpty()) {
			throw new InvalidQueryAttributesException("Valor do identificador é inválido");
		}
		
		return "DELETE FROM " + tableName + " " + createWhereCondition(identifierColumn, identifierValue);
	}
	
	public String createFind(String tableName, String identifierColumn, String identifierValue, String columns) throws InvalidQueryAttributesException {
		if (identifierColumn.isEmpty() || identifierValue.isEmpty()) {
			throw new InvalidQueryAttributesException("Valor do identificador é inválido");
		}
		
		return "SELECT " + columns + " FROM " + tableName + " " + createWhereCondition(identifierColumn, identifierValue);	
	}
	
	private String createWhereCondition(String identifierColumn, String identifierValue) {
		return "WHERE " + identifierColumn + "=" + identifierValue;
	}
}
