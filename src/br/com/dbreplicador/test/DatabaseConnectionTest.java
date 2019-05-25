package br.com.dbreplicador.test;

import java.sql.Connection;
import java.sql.SQLException;

import br.com.dbreplicador.database.ConnectionFactory;

public class DatabaseConnectionTest {

	public DatabaseConnectionTest(){
		Connection conn = ConnectionFactory.getConnection("replicador", "admin", "admin");
		
		try {
			conn.setAutoCommit(false);
			
			System.out.println("*** Conectado ao banco de dados com sucesso");
			

		} catch (SQLException e) {
			e.getErrorCode();
		}
	}
	
	public static void main(String[] args) {
		new DatabaseConnectionTest();
	}
}
