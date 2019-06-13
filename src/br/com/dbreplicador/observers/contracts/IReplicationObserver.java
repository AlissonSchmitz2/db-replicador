package br.com.dbreplicador.observers.contracts;

import br.com.dbreplicador.enums.ReplicationEvents;

public interface IReplicationObserver {
	public void update(IReplicationSubject subject, ReplicationEvents event);
}
