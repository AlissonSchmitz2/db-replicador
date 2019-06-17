
package br.com.dbreplicador.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import br.com.dbreplicador.enums.ReplicationEvents;
import br.com.dbreplicador.image.MasterImage;
import br.com.dbreplicador.observers.contracts.IReplicationObserver;
import br.com.dbreplicador.observers.contracts.IReplicationSubject;
import br.com.dbreplicador.view.contracts.IReplicationExecutor;
import br.com.dbreplicador.view.contracts.IReplicationProcessingInfo;

import javax.swing.JProgressBar;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;

public class ReplicationWindow extends AbstractWindowFrame implements IReplicationObserver {
	private static final long serialVersionUID = -4888464460307835343L;

	// Guarda os fields em uma lista para facilitar manipulação em massa
	private List<Component> formFields = new ArrayList<Component>();

	// Componentes
	private JTextField txfOrigin, txfDestiny, txfProcess, txfError, txfTables, txfTable;
	private JLabel lblDestiny, lblProcess, lblOrigin, lblTable, lblTables, lblError;
	private JProgressBar progressBarIndeterminate, progressBarValue;
	private JButton btnReplicate;
	private JPanel panelConnections, paneLog;
	
	private IReplicationExecutor replicationExecutor = null;

	public ReplicationWindow(JDesktopPane desktop, Connection connection) {
		super("Replicar", 625, 345, desktop);

		setFrameIcon(MasterImage.replicator_16x16);

		createComponents();

		// Por padrão campos são desabilitados ao iniciar
		disableComponents(formFields);
		
		//Executor da replicação
		replicationExecutor = new ReplicationExecutor(connection);
		
		//Adiciona esta classe para ser notificada pelo executor
		((IReplicationSubject) replicationExecutor).addObserver(this);
		
		setButtonsActions();
	}

	private void setButtonsActions() {
		btnReplicate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!replicationExecutor.isRunning()) {
					if (replicationExecutor.isClosed()) {
						Timestamp fromDate = new Timestamp(System.currentTimeMillis());

						replicationExecutor.start(fromDate);
					} else {
						replicationExecutor.resume();
					}
				} else {
					btnReplicate.setText("REPLICAR");
					
					replicationExecutor.pause();
				}
			}
		});
	}

	private void createComponents() {

		/*** PAINEL CONEXÕES **/
		panelConnections = new JPanel();
		panelConnections
				.setBorder(new TitledBorder(null, "Conex\u00F5es", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		//Componentes
		lblOrigin = new JLabel("Origem:");
		lblDestiny = new JLabel("Destino:");
		lblProcess = new JLabel("Processo:");
		txfOrigin = new JTextField();
		txfOrigin.setDisabledTextColor(Color.DARK_GRAY);
		txfOrigin.setEnabled(false);
		txfOrigin.setColumns(10);
		txfDestiny = new JTextField();
		txfDestiny.setDisabledTextColor(Color.DARK_GRAY);
		txfDestiny.setEnabled(false);
		txfDestiny.setColumns(10);
		txfProcess = new JTextField();
		txfProcess.setDisabledTextColor(Color.DARK_GRAY);
		txfProcess.setEnabled(false);
		txfProcess.setColumns(10);
		
		//Layout
		GroupLayout gl_panelConnections = new GroupLayout(panelConnections);
		gl_panelConnections.setHorizontalGroup(
			gl_panelConnections.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelConnections.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panelConnections.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblOrigin)
						.addComponent(lblDestiny)
						.addComponent(lblProcess))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelConnections.createParallelGroup(Alignment.LEADING, false)
						.addComponent(txfDestiny)
						.addComponent(txfOrigin, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
						.addComponent(txfProcess))
					.addContainerGap())
		);
		gl_panelConnections.setVerticalGroup(
			gl_panelConnections.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelConnections.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelConnections.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblOrigin)
						.addComponent(txfOrigin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelConnections.createParallelGroup(Alignment.BASELINE)
						.addComponent(txfDestiny, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDestiny))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelConnections.createParallelGroup(Alignment.BASELINE)
						.addComponent(txfProcess, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblProcess))
					.addContainerGap(21, Short.MAX_VALUE))
		);
		panelConnections.setLayout(gl_panelConnections);
		
		/*** PAINEL LOG **/
		paneLog = new JPanel();
		paneLog.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Log", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		//Componentes
		lblTable = new JLabel("Tabela:");
		lblTables = new JLabel("Tabelas:");
		lblError = new JLabel("Erros:");
		txfError = new JTextField();
		txfError.setForeground(Color.DARK_GRAY);
		txfError.setEditable(false);
		txfError.setDisabledTextColor(Color.DARK_GRAY);
		txfError.setColumns(10);
		txfTables = new JTextField();
		txfTables.setForeground(Color.DARK_GRAY);
		txfTables.setEditable(false);
		txfTables.setDisabledTextColor(Color.DARK_GRAY);
		txfTables.setColumns(10);
		txfTable = new JTextField();
		txfTable.setForeground(Color.DARK_GRAY);
		txfTable.setEditable(false);
		txfTable.setDisabledTextColor(Color.DARK_GRAY);
		txfTable.setColumns(10);
		
		//Layout
		GroupLayout gl_paneLog = new GroupLayout(paneLog);
		gl_paneLog.setHorizontalGroup(gl_paneLog.createParallelGroup(Alignment.LEADING).addGap(0, 263, Short.MAX_VALUE)
				.addGroup(gl_paneLog.createSequentialGroup().addContainerGap()
						.addGroup(gl_paneLog.createParallelGroup(Alignment.TRAILING).addComponent(lblTable)
								.addComponent(lblTables).addComponent(lblError))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_paneLog.createParallelGroup(Alignment.LEADING, false).addComponent(txfError)
								.addComponent(txfTables)
								.addComponent(txfTable, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl_paneLog.setVerticalGroup(gl_paneLog.createParallelGroup(Alignment.LEADING).addGap(0, 127, Short.MAX_VALUE)
				.addGroup(gl_paneLog.createSequentialGroup().addContainerGap()
						.addGroup(gl_paneLog.createParallelGroup(Alignment.BASELINE).addComponent(lblTable)
								.addComponent(txfTable, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_paneLog.createParallelGroup(Alignment.BASELINE)
								.addComponent(txfTables, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblTables))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_paneLog.createParallelGroup(Alignment.BASELINE)
								.addComponent(txfError, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblError))
						.addContainerGap(21, Short.MAX_VALUE)));
		paneLog.setLayout(gl_paneLog);
		
		/*** DEMAIS COMPONENTES **/
		progressBarIndeterminate = new JProgressBar();
		progressBarValue = new JProgressBar();
		progressBarValue.setStringPainted(true);
		btnReplicate = new JButton("REPLICAR");

		//Layout
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(20)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(progressBarValue, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(panelConnections, GroupLayout.PREFERRED_SIZE, 263, Short.MAX_VALUE))
							.addGap(37)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(progressBarIndeterminate, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(paneLog, GroupLayout.PREFERRED_SIZE, 263, Short.MAX_VALUE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(233)
							.addComponent(btnReplicate, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(26, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(20)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panelConnections, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)
						.addComponent(paneLog, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE))
					.addGap(36)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(progressBarValue, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(progressBarIndeterminate, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
					.addGap(38)
					.addComponent(btnReplicate, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(31, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
	}

	private void setStatistics(IReplicationProcessingInfo replicationExecutor) {
		//Current directions
		txfOrigin.setText(replicationExecutor.getCurrentOriginDirection());
		txfDestiny.setText(replicationExecutor.getCurrentDestinationDirection());

		//Current table
		txfTable.setText(replicationExecutor.getCurrentTable());
		
		//Total of tables
		txfTables.setText(String.valueOf(replicationExecutor.getTotalOfTables()));
		
		//Total of errors
		txfError.setText(String.valueOf(replicationExecutor.getTotalOfErrors()));
		
		//Current process
		txfProcess.setText(replicationExecutor.getCurrentProcess());
		
		//Progress bar value
		progressBarValue.setValue(replicationExecutor.getProcessingProgress());
	}
	
	private void activateIndeterminateBar(boolean start) {
		progressBarIndeterminate.setIndeterminate(start);
	}
	
	@Override
	public void update(IReplicationSubject subject, ReplicationEvents event) {
		switch (event) {
		case PREPARING:
			btnReplicate.setText("PREPARANDO...");
			btnReplicate.setEnabled(false);
			
			setStatistics((IReplicationProcessingInfo) replicationExecutor);
			activateIndeterminateBar(true);
			break;
		case READY:
			//
			break;
		case EXECUTING:
			btnReplicate.setText("PAUSAR");
			btnReplicate.setEnabled(true);
			break;
		case PAUSED:
			btnReplicate.setText("RETOMAR");
			
			activateIndeterminateBar(false);
			break;
		case RESUMED:
			btnReplicate.setText("PAUSAR");
			
			activateIndeterminateBar(true);
			break;
		case STOPPED:
			btnReplicate.setText("REPLICAR");
			
			activateIndeterminateBar(false);
			break;
		case FINISHED:
			activateIndeterminateBar(false);
			
			if (((IReplicationProcessingInfo) replicationExecutor).getTotalOfTables() == 0) {
				bubbleWarning("Não existem novos registros para serem replicados");
			} else {
				bubbleSuccess("Replicação executada com sucesso");
			}
			
			btnReplicate.setText("REPETIR");
			
			setStatistics((IReplicationProcessingInfo) replicationExecutor);
			break;
		case FINISHED_BY_ERROR:
		case FATAL_ERROR:
			activateIndeterminateBar(false);
			
			bubbleError("Houve um erro ao replicar o banco de dados");
			
			btnReplicate.setText("REPETIR");
			
			setStatistics((IReplicationProcessingInfo) replicationExecutor);
			break;
			
		case ON_PROCESS:
		case ON_ERROR:
			setStatistics((IReplicationProcessingInfo) replicationExecutor);
			break;
		default:
			break;
		}
	}
}
