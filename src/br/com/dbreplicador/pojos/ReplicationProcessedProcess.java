package br.com.dbreplicador.pojos;

import java.sql.Timestamp;

import br.com.dbreplicador.model.ProcessModel;

public class ReplicationProcessedProcess {
	private ProcessModel processModel;
	private Timestamp startDate;
	private Timestamp finishDate;
	
	public ReplicationProcessedProcess(ProcessModel processModel, Timestamp startDate) {
		this.processModel = processModel;
		this.startDate = startDate;
	}

	public ProcessModel getProcessModel() {
		return processModel;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public Timestamp getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Timestamp finishDate) {
		this.finishDate = finishDate;
	}
}
