package br.com.dbreplicador.view.tableModel;

import br.com.dbreplicador.model.DirectionModel;

public class DirectionTableModel extends AbstractTableModel<DirectionModel> {
	private static final long serialVersionUID = 4546233939817239919L;

	public DirectionTableModel() {
		super(new String[] { "Processo", "Origem", "Destino" });
	}
	
	@Override
	protected void setObjectValueAt(int columnIndex, DirectionModel model, Object aValue) {
		switch (columnIndex) {
		case 0:
			model.setProcess(aValue.toString());
		case 1:
			model.setOriginDatabase((Integer)aValue);
		case 2:
			model.setDestinationDatabase((Integer)aValue);
		default:
			System.err.println("Índice da coluna inválido");
		}
	}

	@Override
	protected Object getObjectValueAt(int columnIndex, DirectionModel model) {
		String valueObject = null;

		switch (columnIndex) {
		case 0:
			valueObject = model.getProcess();
			break;
		case 1:
			valueObject = ((Integer)model.getOriginDatabase()).toString();
			break;
		case 2:
			valueObject = ((Integer)model.getDestinationDatabase()).toString();
			break;	
		default:
			System.err.println("Índice da coluna inválido");
		}

		return valueObject;
	}

}
