package br.com.dbreplicador.view.tableModel;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import br.com.dbreplicador.model.ProcessModel;

public class ProcessTableModel extends AbstractTableModel<ProcessModel> {
	private static final long serialVersionUID = 1242843030000425873L;

	public ProcessTableModel() {
		super(new String[] { "Processo", "Descrição", "Data" });
	}

	@Override
	protected void setObjectValueAt(int columnIndex, ProcessModel model, Object aValue) {
		switch (columnIndex) {
		case 0:
			model.setProcess(aValue.toString());
		case 1:
			model.setDescription(aValue.toString());
		case 2:
			model.setCurrentDateOf((Timestamp) aValue);	
		default:
			System.err.println("Índice da coluna inválido");
		}
	}

	@Override
	protected Object getObjectValueAt(int columnIndex, ProcessModel model) {
		String valueObject = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		switch (columnIndex) {
		case 0:
			valueObject = model.getProcess();
			break;
		case 1:
			valueObject = model.getDescription();
			break;
		case 2:
			if (model.getCurrentDateOf() != null) {
				valueObject = dateFormat.format(model.getCurrentDateOf());
			} else{
				valueObject = null;
			}
			break;	
		default:
			System.err.println("Índice da coluna inválido");
		}

		return valueObject;
	}
}