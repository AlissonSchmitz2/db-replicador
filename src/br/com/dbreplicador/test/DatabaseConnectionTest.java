package br.com.dbreplicador.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.replicator.database.ConnectionFactory;
import br.com.replicator.database.query.QueryBuilderFactory;
import br.com.replicator.database.query.QueryProcessor;
import br.com.replicator.database.query.contracts.IQuery;
import br.com.replicator.database.query.contracts.IQueryBuilder;
import br.com.replicator.database.query.contracts.IQueryProcessor;
import br.com.replicator.enums.SupportedTypes;


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
				
				//INSERT
				IQuery insertQuery = queryBuilder.insert(
						"alunos",
						new String[] {
							"codigo_aluno",
							"aluno"
						}, 
						new String[] {
							"2",
							"Aluno 2"
						});
				
				Integer resultInsert = processor.executeUpdate(insertQuery);
				System.out.println("Inserted: " + resultInsert);
				
				//UPDATED
				IQuery updateQuery = queryBuilder.update("alunos",
						new String[] {
							"aluno"
						},
						new String[] {
							"Aluno 2 - Updated"
						},
						"codigo_aluno", "2");
				Integer resultUpdate = processor.executeUpdate(updateQuery);
				System.out.println("Updated: " + resultUpdate);
				
				//FIND
				IQuery findQuery = queryBuilder.find("alunos", "codigo_aluno", "2", "*");
				
				ResultSet rst = processor.execute(findQuery);
				while (rst.next()) {
					System.out.println("Find: " + rst.getString("aluno"));
				}
				
				//DELETE
				IQuery deleteQuery = queryBuilder.delete("alunos", "codigo_aluno", "2");
				Integer resultDelete = processor.executeUpdate(deleteQuery);
				System.out.println("Delete: " + resultDelete);
				
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
