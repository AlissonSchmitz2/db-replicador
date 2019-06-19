package br.com.dbreplicador.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;

import br.com.replicator.database.ConnectionFactory;
import br.com.replicator.database.ConnectionInfo;
import br.com.replicator.database.query.QueryBuilderFactory;
import br.com.replicator.database.query.QueryProcessor;
import br.com.replicator.database.query.contracts.IQuery;
import br.com.replicator.database.query.contracts.IQueryBuilder;
import br.com.replicator.database.query.contracts.IQueryProcessor;
import br.com.replicator.enums.SupportedTypes;

public class ReplicatorCRUDTest {
	public ReplicatorCRUDTest() {
		try {
			ConnectionInfo originConnInfo = new ConnectionInfo(SupportedTypes.POSTGRESQL, "localhost", 5432, "master", "admin", "admin");
			Connection connOrigin = ConnectionFactory.getConnection(originConnInfo);
		
		
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
					},
					new int[] {
						Types.VARCHAR,
						Types.VARCHAR
					});
			
			Object resultInsert = processor.executeUpdate(insertQuery);
			System.out.println("Inserted: " + resultInsert);
			
			//UPDATED
			IQuery updateQuery = queryBuilder.update("alunos",
					new String[] {
						"aluno"
					},
					new String[] {
						"Aluno 2 - Updated"
					},
					new int[] {
						Types.VARCHAR
					},
					"codigo_aluno", "2");
			Object resultUpdate = processor.executeUpdate(updateQuery);
			System.out.println("Updated: " + resultUpdate);
			
			//FIND
			IQuery findQuery = queryBuilder.find("alunos", "codigo_aluno", "=", "2", "*");
			
			ResultSet rst = processor.execute(findQuery);
			while (rst.next()) {
				System.out.println("Find: " + rst.getString("aluno"));
			}
			
			//DELETE
			IQuery deleteQuery = queryBuilder.delete("alunos", "codigo_aluno", "2");
			Object resultDelete = processor.executeUpdate(deleteQuery);
			System.out.println("Delete: " + resultDelete);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new ReplicatorCRUDTest();
	}
}
