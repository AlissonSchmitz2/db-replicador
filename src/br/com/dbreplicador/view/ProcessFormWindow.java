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
import com.toedter.calendar.JDateChooser;
import javax.swing.JCheckBox;

public class ProcessFormWindow extends AbstractWindowFrame{
	private static final long serialVersionUID = -4888464460307835343L;
	
	//Componentes
	private JButton btnSearch, btnAdd, btnRemove, btnSave;
	private JLabel lblProcess, lblDescription, lblDateOf;
	private JTextField txfProcess, txfDescription;
	private JDateChooser jDateFor;
	private JCheckBox cbxIgnoreError, cbxEnable;

	// Guarda os fields em uma lista para facilitar manipulação em massa
	private List<Component> formFields = new ArrayList<Component>();

	public ProcessFormWindow(JDesktopPane desktop) {
		super("Cadastro de Processos", 455, 270, desktop);

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
				// TODO Ação Salvar
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

		// Labels
		lblProcess = new JLabel("Processo:");
		lblDescription = new JLabel("Descri\u00E7\u00E3o:");
		lblDateOf = new JLabel("Data De:");

		// TextFields
		txfProcess = new JTextField();
		txfProcess.setColumns(10);
		formFields.add(txfProcess);
		txfDescription = new JTextField();
		txfDescription.setColumns(10);
		formFields.add(txfDescription);
		jDateFor = new JDateChooser();
		jDateFor.setDateFormatString("dd/MM/yyyy");
		formFields.add(jDateFor);
		cbxIgnoreError = new JCheckBox("Ignorar Erro");
		formFields.add(cbxIgnoreError);
		cbxEnable = new JCheckBox("Habilitado");
		formFields.add(cbxEnable);
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(15)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnRemove, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblDateOf)
								.addComponent(lblProcess)
								.addComponent(lblDescription))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(txfDescription, GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
								.addComponent(txfProcess, GroupLayout.PREFERRED_SIZE, 169, GroupLayout.PREFERRED_SIZE)
								.addComponent(jDateFor, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
								.addComponent(cbxIgnoreError, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
								.addComponent(cbxEnable, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE))))
					.addGap(14))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnRemove, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txfProcess, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblProcess))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblDescription)
						.addComponent(txfDescription, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblDateOf)
						.addComponent(jDateFor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(cbxIgnoreError)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(cbxEnable)
					.addContainerGap(38, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
	}
	
//	private boolean validateFields() {
//		//TODO: Validar campos
//		return true;
//	}
}
