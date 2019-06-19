package br.com.dbreplicador.pojos;

import java.sql.Timestamp;

import br.com.dbreplicador.model.TableModel;
import br.com.replicator.database.query.contracts.IQuery;

public class ReplicationProcessedTable {
	private TableModel tableModel;
	private Timestamp startDate;
	private Timestamp finishDate;
	private int totalOfQueries = 0;
	private int totalOfInsertQueries = 0;
	private int totalOfUpdateQueries = 0;
	private int totalOfDeleteQueries = 0;
	
	public ReplicationProcessedTable(TableModel tableModel, Timestamp startDate) {
		this.tableModel = tableModel;
		this.startDate = startDate;
	}
	
	public TableModel getTableModel() {
		return tableModel;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public Timestamp getFinishDate() {
		return finishDate;
	}
	
	public int getTotalsOfQueries() {
		return totalOfQueries;
	}

	public int getTotalOfInsertQueries() {
		return totalOfInsertQueries;
	}

	public int getTotalOfUpdateQueries() {
		return totalOfUpdateQueries;
	}

	public int getTotalOfDeleteQueries() {
		return totalOfDeleteQueries;
	}

	public void setFinishDate(Timestamp finishDate) {
		this.finishDate = finishDate;
	}
	
	public void updateQueryTotals(IQuery query) {
		totalOfQueries++;
		
		switch (query.getType()) {
		case INSERT:
			totalOfInsertQueries++;
			break;
		case UPDATE:
			totalOfUpdateQueries++;
			break;
		case DELETE:
			totalOfDeleteQueries++;
			break;
		default:
			break;
		}
	}
}
