package br.com.dbreplicador.view.tableModel;

import br.com.dbreplicador.model.TableModel;

public class TablesTableModel extends AbstractTableModel<TableModel> {
	private static final long serialVersionUID = -5509448423018221784L;

	public TablesTableModel() {
		super(new String[] { "Processo", "Ordem", "Tabela Origem", "Tabela Destino" });
	}

	@Override
	protected void setObjectValueAt(int columnIndex, TableModel model, Object aValue) {
		switch (columnIndex) {
		case 0:
			model.setProcess(aValue.toString());
		case 1:
			model.setOrder((Integer)aValue);
		case 2:
			model.setOriginTable(aValue.toString());
		case 3:
			model.setDestinationTable(aValue.toString());
		default:
			System.err.println("Índice da coluna inválido");
		}
	}

	@Override
	protected Object getObjectValueAt(int columnIndex, TableModel model) {
		String valueObject = null;
		
		switch (columnIndex) {
		case 0:
			valueObject = model.getProcess();
			break;
		case 1:
			valueObject = model.getOrder().toString();
			break;
		case 2:
			valueObject = model.getOriginTable();
			break;	
		case 3:
			valueObject = model.getDestinationTable();
			break;		
		default:
			System.err.println("Índice da coluna inválido");
		}

		return valueObject;
	}

}
