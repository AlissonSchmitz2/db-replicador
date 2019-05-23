package br.com.dbreplicador.dao.contracts;

import java.sql.SQLException;
import java.util.List;

public interface Searchable<T> {
	public List<T> search(String word) throws SQLException;
}
