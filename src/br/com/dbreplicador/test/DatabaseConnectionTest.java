package br.com.dbreplicador.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.replicator.SupportedTypes;
import br.com.replicator.database.ConnectionFactory;
import br.com.replicator.database.query.QueryBuilderFactory;
import br.com.replicator.database.query.QueryProcessor;
import br.com.replicator.database.query.contracts.IQueryBuilder;
import br.com.replicator.database.query.contracts.IQueryProcessor;


public class DatabaseConnectionTest {

	public DatabaseConnectionTest(){
		Connection conn = br.com.dbreplicador.database.ConnectionFactory.getConnection("replicador", "admin", "admin");
		
		Connection connOrigin = ConnectionFactory.getConnection("postgresql", "localhost", 5432, "master", "admin", "admin");
		Connection connDestination = ConnectionFactory.getConnection("postgresql", "localhost", 5432, "nocaute2", "admin", "admin");
		
		System.out.println(SupportedTypes.POSTGRESQL);
		
		try {
			conn.setAutoCommit(false);
			
			System.out.println("*** Conectado ao banco de dados com sucesso");
			
			try {
				IQueryBuilder queryBuilder = QueryBuilderFactory.getQueryBuilder(SupportedTypes.POSTGRESQL);
				IQueryProcessor processor = new QueryProcessor(connOrigin);
				
				String query = queryBuilder.createFind("alunos", "codigo_aluno", "1", "*");
				
				ResultSet rst = processor.executeQuery(query);
				
				while (rst.next()) {
					System.out.println(rst.getString("aluno"));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		new DatabaseConnectionTest();
	}
}
