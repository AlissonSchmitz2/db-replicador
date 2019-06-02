package br.com.dbreplicador.dao.contracts;

import java.sql.SQLException;

public interface IInsertable<T> {
	public T insert(T model) throws SQLException;
}
