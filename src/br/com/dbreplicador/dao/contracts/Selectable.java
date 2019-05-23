package br.com.dbreplicador.dao.contracts;

import java.sql.SQLException;
import java.util.List;

public interface Selectable<T> {
	public List<T> selectAll() throws SQLException;
	
	public T findById(Integer id) throws SQLException;
}
