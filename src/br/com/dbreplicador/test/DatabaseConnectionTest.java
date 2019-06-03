package br.com.dbreplicador.test;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionTest {

	public DatabaseConnectionTest(){
		Connection conn = br.com.dbreplicador.database.ConnectionFactory.getConnection("replicador", "admin", "admin");

		try {
			conn.setAutoCommit(false);
			
			System.out.println("*** Conectado ao banco de dados com sucesso");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		new DatabaseConnectionTest();
	}
}
