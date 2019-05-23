package br.com.dbreplicador.dao;

import br.com.dbreplicador.dao.contracts.Crud;
import br.com.dbreplicador.dao.contracts.Selectable;
import br.com.dbreplicador.model.contracts.Model;

public abstract class AbstractCrudDAO<T extends Model> extends AbstractDAO<T> implements Crud<T>, Selectable<T> {
}