package br.com.dbreplicador.test;

import java.sql.Connection;
import java.sql.SQLException;

import br.com.replicator.database.ConnectionFactory;
import br.com.replicator.database.ConnectionInfo;
import br.com.replicator.enums.SupportedTypes;

public class DatabaseConnectionTest {

	public DatabaseConnectionTest(){
		
		ConnectionInfo ConnInfo = new ConnectionInfo(SupportedTypes.MYSQL, "localhost", 3306, "banco?useSSL=false", "root", "admin");
		
		
		try {
			Connection connOrigin = ConnectionFactory.getConnection(ConnInfo);
			connOrigin.setAutoCommit(false);
			
			System.out.println("*** Conectado ao banco de dados com sucesso");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		new DatabaseConnectionTest();
	}
}
