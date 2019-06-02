package br.com.dbreplicador.dao;

import br.com.dbreplicador.dao.contracts.ICrud;
import br.com.dbreplicador.dao.contracts.ISelectable;
import br.com.dbreplicador.model.contracts.Model;

public abstract class AbstractCrudDAO<T extends Model> extends AbstractDAO<T> implements ICrud<T>, ISelectable<T> {
}