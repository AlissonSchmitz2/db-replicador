
package br.com.dbreplicador.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import br.com.dbreplicador.image.MasterImage;
import br.com.dbreplicador.view.contracts.IReplicationInfoControl;

import javax.swing.JProgressBar;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;

public class ReplicationWindow extends AbstractWindowFrame implements IReplicationInfoControl {
	private static final long serialVersionUID = -4888464460307835343L;

	// Guarda os fields em uma lista para facilitar manipulação em massa
	private List<Component> formFields = new ArrayList<Component>();

	// Componentes
	private JTextField txfOrigin, txfDestiny, txfProcess, txfError, txfTables, txfTable;
	private JLabel lblDestiny, lblProcess, lblOrigin, lblTable, lblTables, lblError;
	private JProgressBar progressBarIndeterminate, progressBarValue;
	private JButton btnReplicate;
	private JPanel panelConnections, paneLog;
	
	//Referência da janela
	private ReplicationExecutor replicationExecutor = null;

	public ReplicationWindow(JDesktopPane desktop) {
		super("Replicar", 625, 345, desktop);

		setFrameIcon(MasterImage.replicator_16x16);

		createComponents();

		// Por padrão campos são desabilitados ao iniciar
		disableComponents(formFields);
		
		//Executor da replicação
		replicationExecutor = new ReplicationExecutor(ReplicationWindow.this);
		
		setButtonsActions();
	}

	private void setButtonsActions() {
		btnReplicate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!replicationExecutor.isRunning()) {
					btnReplicate.setText("PAUSAR");
					
					replicationExecutor.start();
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
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
						.createSequentialGroup().addGap(20)
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(progressBarValue, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(panelConnections, GroupLayout.PREFERRED_SIZE, 263, Short.MAX_VALUE))
						.addGap(37)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(progressBarIndeterminate, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(paneLog, GroupLayout.PREFERRED_SIZE, 263, Short.MAX_VALUE)))
						.addGroup(groupLayout.createSequentialGroup().addGap(255).addComponent(btnReplicate,
								GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap(26, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addGap(20)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panelConnections, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)
						.addComponent(paneLog, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE))
				.addGap(36)
				.addGroup(
						groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(progressBarValue, GroupLayout.PREFERRED_SIZE, 26,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(progressBarIndeterminate, GroupLayout.PREFERRED_SIZE, 26,
										GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
				.addComponent(btnReplicate, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE).addGap(31)));
		getContentPane().setLayout(groupLayout);
	}

	@Override
	public void setCurrentDirections(String directionOrigin, String directionDestiny) {
		txfOrigin.setText(directionOrigin);
		txfDestiny.setText(directionDestiny);
	}

	@Override
	public void setCurrentTable(String table) {
		txfTable.setText(table);
	}

	@Override
	public void setTotalOfTables(int count) {
		txfTables.setText(String.valueOf(count));
	}

	@Override
	public void setTotalOfErrors(int count) {
		txfError.setText(String.valueOf(count));
	}

	@Override
	public void setCurrentProcess(String process) {
		txfProcess.setText(process);
	}

	@Override
	public void setProgressBarValue(int value) {
		progressBarValue.setValue(value);
	}

	@Override
	public void setProgressIndeterminate(boolean start) {
		progressBarIndeterminate.setIndeterminate(start);
	}
}
