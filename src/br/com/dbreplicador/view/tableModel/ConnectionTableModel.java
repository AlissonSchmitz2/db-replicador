package br.com.dbreplicador.view.tableModel;

import br.com.dbreplicador.model.ReplicationModel;

public class ConnectionTableModel extends AbstractTableModel<ReplicationModel> {
	private static final long serialVersionUID = 3218796435367615745L;

	public ConnectionTableModel() {
		super(new String[] { "Banco", "Descrição", "Modelo" });
	}
	
	@Override
	protected void setObjectValueAt(int columnIndex, ReplicationModel model, Object aValue) {
		switch (columnIndex) {
		case 0:
			model.setDatabase(aValue.toString());
		case 1:
			model.setName(aValue.toString());
		case 2:
			model.setDatebaseType(aValue.toString());	
		default:
			System.err.println("Índice da coluna inválido");
		}
	}

	@Override
	protected Object getObjectValueAt(int columnIndex, ReplicationModel model) {
		String valueObject = null;

		switch (columnIndex) {
		case 0:
			valueObject = model.getDatabase();
			break;
		case 1:
			valueObject = model.getName();
			break;
		case 2:
			valueObject = model.getDatebaseType();
			break;	
		default:
			System.err.println("Índice da coluna inválido");
		}

		return valueObject;
	}
}
