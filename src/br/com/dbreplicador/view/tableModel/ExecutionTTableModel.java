package br.com.dbreplicador.view.tableModel;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import br.com.dbreplicador.model.ExecutionModel;
import br.com.dbreplicador.model.TableExecutionModel;
import br.com.dbreplicador.model.TableModel;

public class ExecutionTTableModel  extends AbstractTableModel<TableExecutionModel>{

	public ExecutionTTableModel() {
		super(new String[] {"Data","Processo","Database Origem",
							"Database Destino","Ordem","Linhas Processadas","Sucesso","Descrição"});
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void setObjectValueAt(int columnIndex, TableExecutionModel model, Object aValue) {
		switch (columnIndex) {
		case 0:
			model.setCurrentDate((Timestamp) aValue);
		case 1:
			model.setProcess(aValue.toString());
		case 2:
			model.setOriginDatabase(aValue.toString());
		case 3:
			model.setDestinationDatabase(aValue.toString());
		case 4:
			model.setOrder(Integer.parseInt(aValue.toString()));
		case 5:
			model.setProcessedLines(Integer.parseInt(aValue.toString()));
		case 6:
			model.setSucess(aValue.toString() == "S" ? true : false);
		case 7:
			model.setMessage(aValue.toString());
		default:
			System.err.println("Índice da coluna inválido");
		}
	}

	@Override
	protected Object getObjectValueAt(int columnIndex, TableExecutionModel model) {
		String valueObject = null;
		//SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		switch (columnIndex) {
		case 0:
			valueObject = String.valueOf(model.getCurrentDate());
			break;
		case 1:
			valueObject = model.getProcess();
			break;
		case 2:
			valueObject = model.getOriginDatabase();
			break;
		case 3:
			valueObject = model.getDestinationDatabase();
			break;
		case 4:
			valueObject = String.valueOf(model.getOrder());
			break;
		case 5:
			valueObject = String.valueOf(model.getProcessedLines());
			break;	
		case 6:
			valueObject = String.valueOf(model.isSucess());
			break;
		case 7:
			valueObject = model.getMessage();
			break;
		default:
			System.err.println("Índice da coluna inválido");
		}

		return valueObject;
	}

}
