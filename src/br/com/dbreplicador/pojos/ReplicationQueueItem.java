package br.com.dbreplicador.pojos;

import br.com.dbreplicador.model.DirectionModel;
import br.com.dbreplicador.model.TableModel;
import br.com.replicator.contracts.IReplicatorProvider;
import br.com.replicator.database.query.contracts.IQuery;

public class ReplicationQueueItem {
	private IQuery query;
	private IReplicatorProvider provider;
	private DirectionModel directionModel;
	private TableModel tableModel;

	public ReplicationQueueItem(IQuery query, IReplicatorProvider provider, DirectionModel directionModel, TableModel tableModel) {
		this.query = query;
		this.provider = provider;
		this.directionModel = directionModel;
		this.tableModel = tableModel;
	}

	public IQuery getQuery() {
		return query;
	}
	
	public IReplicatorProvider getProvider() {
		return provider;
	}
	
	public DirectionModel getDirectionModel() {
		return directionModel;
	}

	public TableModel getTableModel() {
		return tableModel;
	}
}
