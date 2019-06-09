package br.com.dbreplicador.test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import br.com.replicator.Replicator;
import br.com.replicator.contracts.IReplicator;
import br.com.replicator.contracts.IReplicatorProvider;
import br.com.replicator.database.ConnectionInfo;
import br.com.replicator.database.query.contracts.IQuery;
import br.com.replicator.enums.SupportedTypes;
import br.com.replicator.exceptions.InvalidDatabaseTypeException;
import br.com.replicator.exceptions.InvalidQueryAttributesException;

public class ReplicatorTest {
	public ReplicatorTest() {
		ConnectionInfo originConnInfo = new ConnectionInfo(SupportedTypes.POSTGRESQL, "127.0.0.1", 5432, "master", "admin", "admin");
		ConnectionInfo destinationConnInfo = new ConnectionInfo(SupportedTypes.POSTGRESQL, "localhost", 5432, "nocaute2", "admin", "admin");

		try {
			//Primeira sincronização de uma tabela deve-se passar 0 como timestamp
			Timestamp timestamp = new Timestamp(0);
			
			//Salva System.currentTimeMillis() no banco de dados como última data de atualização
			//TODO
			
			//Sincroniza as alterações feitas apartir da última data salva no banco de dados
			//TODO
			
			//Timestamp timestamp = new Timestamp(System.currentTimeMillis());

			IReplicator replicator = new Replicator(originConnInfo, destinationConnInfo);

			List<IQuery> queries = replicator.getQueriesForReplication("cidades", "id_cidade", "ultima_atualizacao", timestamp);
			
			IReplicatorProvider provider = replicator.getDestinationProvider();
			
			//Desabilita auto commit
			provider.getConn().setAutoCommit(false);

			//Processa cada query
			for (int i = 0; i < queries.size(); i++) {
				Integer result = provider.getProcessor()
						.executeUpdate(queries.get(i), "id_cidade");
				
				if (result > 0) {
					System.out.println("Sucesso: " + queries.get(i).toString());
				} else {
					System.out.println("Erro: " + queries.get(i).toString());
				}
				
				if (i % 15 == 0) {
					provider.getConn().commit();
				}
			}
			
			//Comita mudanças
			provider.getConn().commit();
			
		} catch (SQLException | InvalidDatabaseTypeException | InvalidQueryAttributesException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new ReplicatorTest();
	}
}
