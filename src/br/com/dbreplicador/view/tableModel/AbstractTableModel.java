package br.com.dbreplicador.view.tableModel;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTableModel<T> extends javax.swing.table.AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private List<T> modelsList;
	private String[] columns;

	public AbstractTableModel(List<T> modelsList, String[] columns) {
		this.modelsList = modelsList;
		this.columns = columns;
	}

	public AbstractTableModel(String[] columns) {
		this.modelsList = new ArrayList<T>();
		this.columns = columns;
	}

	protected abstract void setObjectValueAt(int columnIndex, T model, Object aValue);

	protected abstract Object getObjectValueAt(int columnIndex, T model);

	public int getRowCount() {
		return modelsList.size();
	}

	public int getColumnCount() {
		return columns.length;
	}

	public String getColumnName(int columnIndex) {
		return columns[columnIndex];
	}

	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		T model = modelsList.get(rowIndex);

		setObjectValueAt(columnIndex, model, aValue);

		fireTableCellUpdated(rowIndex, columnIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		T selectedModel = modelsList.get(rowIndex);

		return getObjectValueAt(columnIndex, selectedModel);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public T getModel(int rowIndex) {
		return modelsList.get(rowIndex);
	}

	public List<T> getModelsList() {
		return modelsList;
	}

	public void addModel(T model) {
		modelsList.add(model);

		int lastIndex = getRowCount() - 1;

		fireTableRowsInserted(lastIndex, lastIndex);
	}

	public void addModel(T model, int index) {
		modelsList.set(index, model);

		fireTableRowsInserted(index, index);
	}

	public void removeModel(int rowIndex) {
		modelsList.remove(rowIndex);

		fireTableRowsDeleted(rowIndex, rowIndex);
	}

	public void addModelsList(List<T> models) {

		int oldSize = getRowCount();
		modelsList.addAll(models);
		fireTableRowsInserted(oldSize, getRowCount() - 1);
	}

	public void clear() {
		modelsList.clear();
		fireTableDataChanged();
	}

	public boolean isEmpty() {
		return modelsList.isEmpty();
	}
}
