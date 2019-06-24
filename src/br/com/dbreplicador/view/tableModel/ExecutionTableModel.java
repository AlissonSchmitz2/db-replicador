package br.com.dbreplicador.view.tableModel;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import br.com.dbreplicador.model.ExecutionModel;

public class ExecutionTableModel extends AbstractTableModel<ExecutionModel>{

	public ExecutionTableModel() {
		super(new String[] {"Código da Execução","Usuário","Processo",
							"Database Origem","Database Destino","Inicio","Fim","Ocorrencia"});
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void setObjectValueAt(int columnIndex, ExecutionModel model, Object aValue) {
		switch (columnIndex) {
		case 0:
			model.setExecuteCode(Integer.parseInt(aValue.toString()));
		case 1:
			model.setUser(aValue.toString());
		case 2:
			model.setProcess(aValue.toString());	
		case 3:
			model.setOriginDatabase(Integer.parseInt(aValue.toString()));
		case 4:
			model.setDestinatioDatabase(Integer.parseInt(aValue.toString()));
		case 5:
			model.setDateHourInitial((Timestamp) aValue);
		case 6:
			model.setDateHourFinal((Timestamp) aValue);
		case 7:
			model.setOccurrence(aValue.toString());
				
		default:
			System.err.println("Índice da coluna inválido");
		}
	}

	@Override
	protected Object getObjectValueAt(int columnIndex, ExecutionModel model) {
		String valueObject = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		switch (columnIndex) {
		case 0:
			valueObject = String.valueOf(model.getExecuteCode());
			break;
		case 1:
			valueObject = model.getUser();
			break;
		case 2:
			valueObject = model.getProcess();
			break;
		case 3:
			valueObject = String.valueOf(model.getOriginDatabase());
			break;
		case 4:
			valueObject = String.valueOf(model.getDestinatioDatabase());
			break;
		case 5:
			if (model.getDateHourInitial() != null) {
				valueObject = dateFormat.format(model.getDateHourInitial());
			} else{
				valueObject = null;
			}
			break;	
		case 6:
			if (model.getDateHourFinal() != null) {
				valueObject = dateFormat.format(model.getDateHourFinal());
			} else{
				valueObject = null;
			}
			break;
		case 7:
			valueObject = model.getProcess();
			break;
		default:
			System.err.println("Índice da coluna inválido");
		}

		return valueObject;
	}

}
