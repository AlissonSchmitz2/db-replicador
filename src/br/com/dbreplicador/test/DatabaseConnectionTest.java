package br.com.dbreplicador.test;

import java.sql.Connection;
import java.sql.SQLException;

import br.com.dbreplicador.dao.TableDAO;
import br.com.dbreplicador.database.ConnectionFactory;

public class DatabaseConnectionTest {

	public DatabaseConnectionTest(){
		Connection conn = ConnectionFactory.getConnection("replicador", "root", "root");
		
		try {
			conn.setAutoCommit(false);
			
			System.out.println("*** Conectado ao banco de dados com sucesso");
			
			TableDAO dao = new TableDAO(conn);
			
			dao.selectAll().forEach(item -> System.out.println("..." + item.getOrder()));
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		new DatabaseConnectionTest();
	}
}
