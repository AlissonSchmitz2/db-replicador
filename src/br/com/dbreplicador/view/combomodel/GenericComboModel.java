package br.com.dbreplicador.view.combomodel;

import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

public class GenericComboModel<E> extends AbstractListModel<E> implements ComboBoxModel<E> {
	private static final long serialVersionUID = 1L;

	private List<E> itemList;
	private E selection;

	public GenericComboModel(List<E> list) {
		this.itemList = list;
	}

	@Override
	public int getSize() {
		return this.itemList.size();
	}

	@Override
	public E getElementAt(int index) {
		return this.itemList.get(index);
	}

	@Override
	public E getSelectedItem() {
		return (E) this.selection;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setSelectedItem(Object anItem) {
		this.selection = (E) anItem;
	}
}
