package br.com.dbreplicador.dao.contracts;

import java.sql.SQLException;

public interface ICrud<T> {
	public T insert(T model) throws SQLException;
	
	public boolean update(T model) throws SQLException;
	
	public boolean delete(T model) throws SQLException;
	
	public boolean deleteById(Integer id) throws SQLException;
}
