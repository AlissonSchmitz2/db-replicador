package br.com.dbreplicador.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import br.com.replicator.database.ConnectionFactory;
import br.com.replicator.database.query.QueryBuilderFactory;
import br.com.replicator.database.query.QueryProcessor;
import br.com.replicator.database.query.contracts.IQuery;
import br.com.replicator.database.query.contracts.IQueryBuilder;
import br.com.replicator.database.query.contracts.IQueryProcessor;
import br.com.replicator.enums.SupportedTypes;

public class ReplicatorTest {
	public ReplicatorTest() {
		Connection connOrigin = ConnectionFactory.getConnection("postgresql", "localhost", 5432, "master", "admin", "admin");
		Connection connDestination = ConnectionFactory.getConnection("postgresql", "localhost", 5432, "nocaute2", "admin", "admin");
	
		try {
			IQueryBuilder queryBuilder = QueryBuilderFactory.getQueryBuilder(SupportedTypes.POSTGRESQL);
			IQueryProcessor processor = new QueryProcessor(connOrigin);
			IQueryProcessor destinationProcessor = new QueryProcessor(connDestination);
			
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			/*String date = new SimpleDateFormat("dd/MM/yyyy").format(timestamp.getTime());*/
			System.out.println(timestamp.toString());
			
			//FIND
			IQuery findQuery = queryBuilder.find("alunos", "ultima_atualizacao", "<", timestamp.toString(), "*");
			
			ResultSet rst = processor.execute(findQuery);
			while (rst.next()) {
				IQuery findQueryDestination = queryBuilder.find("alunos", "codigo_aluno", "=", rst.getString("codigo_aluno"), "*");
				
				ResultSet rst2 = destinationProcessor.execute(findQueryDestination);
				
				ResultSetMetaData rstmd = rst.getMetaData();
				
				String[] columns = new String[rstmd.getColumnCount()];
				String[] values = new String[rstmd.getColumnCount()];
				
				for (int i = 1; i <= rstmd.getColumnCount(); i++) {
					columns[i-1] = rstmd.getColumnName(i);
					values[i-1] = rst.getString(i);
				}
				
				if (rst2.next()) {
					//Update
					IQuery updateQuery = queryBuilder.update("alunos", columns, values, "codigo_aluno", rst.getString("codigo_aluno"));
					
					System.out.println(updateQuery);
					
					destinationProcessor.execute(updateQuery);
				} else {
					//Insert
					IQuery insertQuery = queryBuilder.insert("alunos", columns, values);
					
					System.out.println(insertQuery);
					
					destinationProcessor.execute(insertQuery);
				}

				System.out.println("Find: " + rst.getString("aluno"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new ReplicatorTest();
	}
}
