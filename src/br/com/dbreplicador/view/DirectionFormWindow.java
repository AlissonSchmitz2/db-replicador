
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
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;

public class DirectionFormWindow extends AbstractWindowFrame {
	private static final long serialVersionUID = 2082839251104219643L;

	// Guarda os fields em uma lista para facilitar manipulação em massa
	private List<Component> formFields = new ArrayList<Component>();
	private List<Component> formFieldsAutomatic = new ArrayList<Component>();
	
	// Componentes
	private JButton btnSearch, btnAdd, btnRemove, btnSave;
	private JPanel panelDiretion, panelOrigin, panelDestiny, panelPeriod;
	private JLabel lblProcess, lblDuration, lblRetention, lblDBOrigin, lblUserOrigin, lblPasswordOrigin, lblDBDestiny,
			lblPasswordDestiny, lblUserDestiny, lblYear, lblDay, lblMonth, lblSecond, lblMinute, lblHour;
	private JTextField txfProcess, txfDuration, txfRetention, txfDBOrigin, txfUserOrigin, txfPasswordOrigin,
			txfDBDestiny, txfPasswordDestiny, txfUserDestiny, txfYear, txfDay, txfMonth, txfSecond, txfMinute, txfHour;
	private JCheckBox cbxEnable, cbxAutomatic;

	public DirectionFormWindow(JDesktopPane desktop) {
		super("Cadastro da Direção", 555, 510, desktop);

		setFrameIcon(MasterImage.direction_16x16);

		createComponents();

		// Por padrão campos são desabilitados ao iniciar
		disableComponents(formFields);

		setButtonsActions();
	}

	private void setButtonsActions() {
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: Acão Buscar
			}
		});

		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Ativa campos
				enableComponents(formFields);

				btnRemove.setEnabled(false);
				btnSave.setEnabled(true);
			}
		});

		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: Acão Remover
			}
		});

		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!validateFields()) {
					return;
				}

				// TODO Ação Salvar
			}
		});
		
		//Se for replicação automática
		cbxAutomatic.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(txfDay.isEnabled()) {
					disableComponents(formFieldsAutomatic);
				}else {
					enableComponents(formFieldsAutomatic);
				}
				
			}
		});
	}

	private void createComponents() {

		// Toolbar
		btnSearch = new JButton("Buscar", MasterImage.search_22x22);
		btnSearch.setToolTipText("Clique aqui para buscar");
		btnAdd = new JButton("Adicionar", MasterImage.add_22x22);
		btnAdd.setToolTipText("Clique aqui para adicionar");
		btnRemove = new JButton("Remover", MasterImage.remove_22x22);
		btnRemove.setToolTipText("Clique aqui para remover");
		btnRemove.setEnabled(false);
		btnSave = new JButton("Salvar", MasterImage.save_22x22);
		btnSave.setToolTipText("Clique aqui para salvar");
		btnSave.setEnabled(false);

		/********************* PAINEL DIREÇÃO ***********************************/
		panelDiretion = new JPanel();
		panelDiretion.setBorder(
				new TitledBorder(null, "Dire\u00E7\u00E3o", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGap(15)
								.addComponent(btnSearch, GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
								.addComponent(btnAdd, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
								.addComponent(btnRemove, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
								.addComponent(btnSave, GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE).addGap(104))
						.addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(panelDiretion,
								GroupLayout.PREFERRED_SIZE, 519, Short.MAX_VALUE)))
				.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addGap(5)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnRemove, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
						.addGap(18)
						.addComponent(panelDiretion, GroupLayout.PREFERRED_SIZE, 377, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(40, Short.MAX_VALUE)));
		// COMPONENTES
		lblProcess = new JLabel("Processo:");
		lblDuration = new JLabel("Dura\u00E7\u00E3o:");
		lblRetention = new JLabel("Reten\u00E7\u00E3o:");
		txfProcess = new JTextField("Teclar F9");
		txfProcess.setColumns(10);
		txfProcess.setBackground(Color.yellow);
		txfProcess.setEnabled(false);
		txfDuration = new JTextField();
		txfDuration.setColumns(10);
		formFields.add(txfDuration);
		txfRetention = new JTextField();
		txfRetention.setColumns(10);
		formFields.add(txfRetention);
		cbxEnable = new JCheckBox("Habilitado");
		formFields.add(cbxEnable);
		cbxAutomatic = new JCheckBox("Autom\u00E1tico");
		formFields.add(cbxAutomatic);

		/********************* PAINEL ORIGEM ***********************************/
		panelOrigin = new JPanel();
		panelOrigin.setBorder(new TitledBorder(null, "Origem", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		lblDBOrigin = new JLabel("Database:");
		lblUserOrigin = new JLabel("Usu\u00E1rio:");
		lblPasswordOrigin = new JLabel("Senha:");
		txfDBOrigin = new JTextField("Teclar F9");
		txfDBOrigin.setColumns(10);
		txfDBOrigin.setBackground(Color.yellow);
		txfDBOrigin.setEnabled(false);
		txfUserOrigin = new JTextField();
		txfUserOrigin.setColumns(10);
		formFields.add(txfUserOrigin);
		txfPasswordOrigin = new JTextField();
		txfPasswordOrigin.setColumns(10);
		formFields.add(txfPasswordOrigin);

		/********************* PAINEL DESTINO ***********************************/
		panelDestiny = new JPanel();
		panelDestiny.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Destino",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		lblDBDestiny = new JLabel("Database:");
		lblPasswordDestiny = new JLabel("Senha:");
		lblUserDestiny = new JLabel("Usu\u00E1rio:");
		txfDBDestiny = new JTextField("Teclar F9");
		txfDBDestiny.setColumns(10);
		txfDBDestiny.setBackground(Color.yellow);
		txfDBDestiny.setEnabled(false);
		txfPasswordDestiny = new JTextField();
		txfPasswordDestiny.setColumns(10);
		formFields.add(txfPasswordDestiny);
		txfUserDestiny = new JTextField();
		txfUserDestiny.setColumns(10);
		formFields.add(txfUserDestiny);

		GroupLayout gl_panelDestiny = new GroupLayout(panelDestiny);
		gl_panelDestiny.setHorizontalGroup(gl_panelDestiny.createParallelGroup(Alignment.LEADING)
				.addGap(0, 246, Short.MAX_VALUE)
				.addGroup(gl_panelDestiny.createSequentialGroup()
						.addGroup(gl_panelDestiny.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelDestiny.createSequentialGroup().addComponent(lblDBDestiny)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(txfDBDestiny, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
								.addGroup(gl_panelDestiny.createSequentialGroup().addContainerGap()
										.addGroup(gl_panelDestiny.createParallelGroup(Alignment.TRAILING)
												.addComponent(lblPasswordDestiny).addComponent(lblUserDestiny))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_panelDestiny.createParallelGroup(Alignment.LEADING)
												.addComponent(txfPasswordDestiny, GroupLayout.DEFAULT_SIZE, 170,
														Short.MAX_VALUE)
												.addComponent(txfUserDestiny, GroupLayout.DEFAULT_SIZE, 170,
														Short.MAX_VALUE))))
						.addContainerGap()));
		gl_panelDestiny.setVerticalGroup(gl_panelDestiny.createParallelGroup(Alignment.LEADING)
				.addGap(0, 100, Short.MAX_VALUE)
				.addGroup(gl_panelDestiny.createSequentialGroup()
						.addGroup(gl_panelDestiny.createParallelGroup(Alignment.BASELINE).addComponent(lblDBDestiny)
								.addComponent(txfDBDestiny, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panelDestiny.createParallelGroup(Alignment.BASELINE).addComponent(lblUserDestiny)
								.addComponent(txfUserDestiny, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
								gl_panelDestiny.createParallelGroup(Alignment.BASELINE).addComponent(lblPasswordDestiny)
										.addComponent(txfPasswordDestiny, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panelDestiny.setLayout(gl_panelDestiny);

		/********************* PAINEL PERÍODO ***********************************/
		panelPeriod = new JPanel();
		panelPeriod.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Per\u00EDodo",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		lblYear = new JLabel("Ano:");
		lblDay = new JLabel("Dia:");
		lblMonth = new JLabel("M\u00EAs:");
		lblSecond = new JLabel("Segundo:");
		lblMinute = new JLabel("Minuto:");
		lblHour = new JLabel("Hora:");
		txfYear = new JTextField();
		txfYear.setColumns(10);
		txfYear.setEnabled(false);
		formFieldsAutomatic.add(txfYear);
		txfDay = new JTextField();
		txfDay.setColumns(10);
		txfDay.setEnabled(false);
		formFieldsAutomatic.add(txfDay);
		txfMonth = new JTextField();
		txfMonth.setColumns(10);
		txfMonth.setEnabled(false);
		formFieldsAutomatic.add(txfMonth);
		txfSecond = new JTextField();
		txfSecond.setColumns(10);
		txfSecond.setEnabled(false);
		formFieldsAutomatic.add(txfSecond);
		txfMinute = new JTextField();
		txfMinute.setColumns(10);
		txfMinute.setEnabled(false);
		formFieldsAutomatic.add(txfMinute);
		txfHour = new JTextField();
		txfHour.setColumns(10);
		txfHour.setEnabled(false);
		formFieldsAutomatic.add(txfHour);
		

		GroupLayout gl_panelPeriod = new GroupLayout(panelPeriod);
		gl_panelPeriod.setHorizontalGroup(gl_panelPeriod.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelPeriod.createSequentialGroup().addGap(6)
						.addGroup(gl_panelPeriod.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelPeriod.createSequentialGroup().addComponent(lblYear)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(txfYear, GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
								.addGroup(gl_panelPeriod.createSequentialGroup()
										.addGroup(gl_panelPeriod.createParallelGroup(Alignment.TRAILING)
												.addComponent(lblMonth).addComponent(lblDay))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_panelPeriod
												.createParallelGroup(Alignment.LEADING).addComponent(txfMonth)
												.addComponent(txfDay, GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))))
						.addGap(18)
						.addGroup(gl_panelPeriod.createParallelGroup(Alignment.TRAILING).addComponent(lblSecond)
								.addComponent(lblMinute).addComponent(lblHour))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panelPeriod.createParallelGroup(Alignment.LEADING)
								.addComponent(txfHour, GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
								.addComponent(txfMinute, GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
								.addComponent(txfSecond, GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
						.addContainerGap()));
		gl_panelPeriod.setVerticalGroup(gl_panelPeriod.createParallelGroup(Alignment.LEADING).addGroup(gl_panelPeriod
				.createSequentialGroup()
				.addGroup(gl_panelPeriod.createParallelGroup(Alignment.TRAILING).addGroup(gl_panelPeriod
						.createSequentialGroup()
						.addGroup(gl_panelPeriod.createParallelGroup(Alignment.TRAILING).addGroup(gl_panelPeriod
								.createSequentialGroup()
								.addGroup(gl_panelPeriod.createParallelGroup(Alignment.BASELINE)
										.addComponent(txfHour, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblHour))
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(txfMinute,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
								.addComponent(lblMinute))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panelPeriod.createParallelGroup(Alignment.BASELINE).addComponent(lblSecond)
								.addComponent(txfSecond, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panelPeriod.createSequentialGroup()
								.addGroup(gl_panelPeriod.createParallelGroup(Alignment.BASELINE)
										.addComponent(txfYear, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblYear))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_panelPeriod.createParallelGroup(Alignment.BASELINE).addComponent(lblMonth)
										.addComponent(txfMonth, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_panelPeriod.createParallelGroup(Alignment.BASELINE).addComponent(lblDay)
										.addComponent(txfDay, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))))
				.addContainerGap(13, Short.MAX_VALUE)));
		panelPeriod.setLayout(gl_panelPeriod);

		GroupLayout gl_panelDiretion = new GroupLayout(panelDiretion);
		gl_panelDiretion.setHorizontalGroup(gl_panelDiretion.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelDiretion.createSequentialGroup().addGroup(gl_panelDiretion
						.createParallelGroup(Alignment.LEADING)
						.addComponent(panelPeriod, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_panelDiretion.createSequentialGroup()
								.addComponent(panelOrigin, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(panelDestiny, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)))
						.addGap(5))
				.addGroup(
						gl_panelDiretion.createSequentialGroup().addGap(21)
								.addGroup(gl_panelDiretion.createParallelGroup(Alignment.TRAILING)
										.addComponent(lblDuration).addComponent(lblProcess).addComponent(lblRetention))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(
										gl_panelDiretion.createParallelGroup(Alignment.LEADING).addComponent(cbxEnable)
												.addGroup(gl_panelDiretion.createParallelGroup(Alignment.LEADING)
														.addComponent(txfRetention).addComponent(txfDuration)
														.addComponent(txfProcess, GroupLayout.DEFAULT_SIZE, 160,
																Short.MAX_VALUE)
														.addComponent(cbxAutomatic)))
								.addGap(272)));
		gl_panelDiretion
				.setVerticalGroup(gl_panelDiretion.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelDiretion.createSequentialGroup()
								.addGroup(gl_panelDiretion.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblProcess).addComponent(txfProcess, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_panelDiretion.createParallelGroup(Alignment.LEADING)
										.addComponent(lblDuration).addComponent(txfDuration, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_panelDiretion.createParallelGroup(Alignment.BASELINE)
										.addComponent(txfRetention, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblRetention))
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(cbxAutomatic).addGap(3)
								.addComponent(cbxEnable).addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_panelDiretion.createParallelGroup(Alignment.LEADING)
										.addComponent(panelOrigin, GroupLayout.PREFERRED_SIZE, 100,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(panelDestiny, GroupLayout.PREFERRED_SIZE, 100,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(panelPeriod, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		GroupLayout gl_panelOrigin = new GroupLayout(panelOrigin);
		gl_panelOrigin.setHorizontalGroup(gl_panelOrigin.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelOrigin.createSequentialGroup().addComponent(lblDBOrigin)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txfDBOrigin, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE).addContainerGap())
				.addGroup(
						gl_panelOrigin.createSequentialGroup().addContainerGap()
								.addGroup(gl_panelOrigin.createParallelGroup(Alignment.TRAILING)
										.addComponent(lblPasswordOrigin).addComponent(lblUserOrigin))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_panelOrigin.createParallelGroup(Alignment.LEADING)
										.addComponent(txfPasswordOrigin, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
										.addComponent(txfUserOrigin, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
								.addContainerGap()));
		gl_panelOrigin.setVerticalGroup(gl_panelOrigin.createParallelGroup(Alignment.LEADING).addGroup(gl_panelOrigin
				.createSequentialGroup()
				.addGroup(gl_panelOrigin.createParallelGroup(Alignment.BASELINE).addComponent(lblDBOrigin).addComponent(
						txfDBOrigin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panelOrigin
						.createParallelGroup(Alignment.BASELINE).addComponent(lblUserOrigin).addComponent(txfUserOrigin,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panelOrigin.createParallelGroup(Alignment.BASELINE).addComponent(lblPasswordOrigin)
						.addComponent(txfPasswordOrigin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addContainerGap(22, Short.MAX_VALUE)));
		panelOrigin.setLayout(gl_panelOrigin);
		panelDiretion.setLayout(gl_panelDiretion);
		getContentPane().setLayout(groupLayout);
	}

	private boolean validateFields() {
		if (txfProcess.getText().equals("Teclar F9")) {
			bubbleWarning("Selecione o processo!");
			return false;
		} else if (txfDuration.getText().isEmpty() || txfDuration.getText() == null) {
			bubbleWarning("Informe o tempo de duração!");
			return false;
		} else if (txfRetention.getText().isEmpty() || txfRetention.getText() == null) {
			bubbleWarning("Informe a retenção!");
			return false;
		} else if (txfDBOrigin.getText().equals("Teclar F9")) {
			bubbleWarning("Selecione o banco origem!");
			return false;
		} else if (txfUserOrigin.getText().isEmpty() || txfUserOrigin.getText() == null) {
			bubbleWarning("Informe o usuário do banco origem!");
			return false;
		} else if (txfPasswordOrigin.getText().isEmpty() || txfPasswordOrigin.getText() == null) {
			bubbleWarning("Informe a senha do usuário do banco origem!");
			return false;
		}  else if (txfDBDestiny.getText().equals("Teclar F9")) {
			bubbleWarning("Selecione o banco destino!");
			return false;
		} else if (txfUserDestiny.getText().isEmpty() || txfUserDestiny.getText() == null) {
			bubbleWarning("Informe o usuário do banco destino!");
			return false;
		} else if (txfPasswordDestiny.getText().isEmpty() || txfPasswordDestiny.getText() == null) {
			bubbleWarning("Informe a senha do usuário do banco destino!");
			return false;
		} 

		return true;
	}
}
