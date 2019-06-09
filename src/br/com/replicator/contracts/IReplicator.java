package br.com.replicator.contracts;

import java.sql.SQLException;
import java.util.List;

import br.com.replicator.database.query.contracts.IQuery;
import br.com.replicator.exceptions.InvalidQueryAttributesException;

public interface IReplicator {
	public List<IQuery> getQueriesForReplication(
			String tableName,
			String tableUniqueKey,
			String replicationColumnControl,
			Object replicationControlValue
	) throws InvalidQueryAttributesException, SQLException;
	
	public IReplicatorProvider getOriginProvider();
	
	public IReplicatorProvider getDestinationProvider();
}
