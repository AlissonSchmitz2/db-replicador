package br.com.dbreplicador.observers.contracts;

import br.com.dbreplicador.enums.ReplicationEvents;

public interface IReplicationSubject {
	public void addObserver(IReplicationObserver observer);
    public void removeObserver(IReplicationObserver observer);
    public void notifyObservers(ReplicationEvents event);
}
