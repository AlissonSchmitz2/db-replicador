
package br.com.dbreplicador.view;

import java.awt.Color;
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
import javax.swing.SwingConstants;
import javax.swing.JComboBox;

public class TableFormWindow extends AbstractWindowFrame {
	private static final long serialVersionUID = 3242592994592829458L;
	private JLabel lblProcess, lblOrder;
	private JTextField txfProcess;

	// Guarda os fields em uma lista para facilitar manipulação em massa
	private List<Component> formFields = new ArrayList<Component>();

	// Componentes
	private JButton btnSearch, btnAdd, btnRemove, btnSave;
	private JTextField txfOrder, txfTableOrigin, txfOperation, txfTableDestiny, txfSaveAfter, txfColumnKey;
	private JLabel lblTableOrigin, lblOperation, lblTableDestiny, lblSaveAfter, lblColumnKey, lblColumnType;
	private JCheckBox cbxEnable, cbxIgnoreError;
	private JComboBox<String> cbxColumnType;

	public TableFormWindow(JDesktopPane desktop) {
		super("Cadastro de Tabelas", 470, 420, desktop);

		setFrameIcon(MasterImage.details_16x16);

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
				if(!validateFields()) {
					return;
				}
				
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
		lblOrder = new JLabel("Ordem:");
		lblTableOrigin = new JLabel("Tabela Origem:");
		lblOperation = new JLabel("Opera\u00E7\u00E3o:");
		lblTableDestiny = new JLabel("Tabela Destino:");
		lblSaveAfter = new JLabel("Salvar Ap\u00F3s:");
		lblColumnKey = new JLabel("Coluna Chave:");
		lblColumnType = new JLabel("Coluna Tipo:");

		// TextFields
		txfProcess = new JTextField("Teclar F9");
		txfProcess.setColumns(10);
		txfProcess.setEnabled(false);
		txfProcess.setBackground(Color.yellow);
		txfOrder = new JTextField();
		txfOrder.setColumns(10);
		formFields.add(txfOrder);
		txfTableOrigin = new JTextField();
		txfTableOrigin.setHorizontalAlignment(SwingConstants.LEFT);
		txfTableOrigin.setColumns(10);
		formFields.add(txfTableOrigin);
		txfOperation = new JTextField();
		txfOperation.setColumns(10);
		formFields.add(txfOperation);
		txfTableDestiny = new JTextField();
		txfTableDestiny.setColumns(10);
		formFields.add(txfTableDestiny);
		txfSaveAfter = new JTextField();
		txfSaveAfter.setColumns(10);
		formFields.add(txfSaveAfter);
		cbxEnable = new JCheckBox("Habilitado");
		formFields.add(cbxEnable);
		cbxIgnoreError = new JCheckBox("Ignorar Erro");
		formFields.add(cbxIgnoreError);
		txfColumnKey = new JTextField();
		formFields.add(txfColumnKey);
		cbxColumnType = new JComboBox<String>();
		formFields.add(cbxColumnType);

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addGap(15).addGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnRemove, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(lblTableDestiny).addComponent(lblOperation).addComponent(lblOrder)
										.addComponent(lblProcess).addComponent(lblTableOrigin)
										.addComponent(lblSaveAfter)
										.addComponent(lblColumnKey, GroupLayout.PREFERRED_SIZE, 75,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblColumnType, GroupLayout.PREFERRED_SIZE, 75,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(txfColumnKey, GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
										.addComponent(txfSaveAfter, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(txfOperation, GroupLayout.PREFERRED_SIZE, 120,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(cbxIgnoreError, GroupLayout.PREFERRED_SIZE, 94,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(cbxEnable, GroupLayout.PREFERRED_SIZE, 94,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(cbxColumnType, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(txfTableOrigin)
										.addComponent(txfTableDestiny, GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
										.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
												.addComponent(txfProcess, Alignment.LEADING).addComponent(txfOrder,
														Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 118,
														Short.MAX_VALUE)))))
						.addContainerGap(29, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnRemove, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
						.addGap(18)
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(lblProcess)
								.addComponent(txfProcess, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
								.createSequentialGroup().addGap(193)
								.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblColumnKey)
										.addComponent(
												txfColumnKey, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)))
								.addGroup(
										groupLayout.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED)
												.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
														.addComponent(txfOrder, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(lblOrder))
												.addPreferredGap(ComponentPlacement.RELATED)
												.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
														.addComponent(txfTableOrigin, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(lblTableOrigin, GroupLayout.PREFERRED_SIZE, 14,
																GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(ComponentPlacement.RELATED)
												.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
														.addComponent(txfOperation, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(lblOperation))
												.addPreferredGap(ComponentPlacement.RELATED)
												.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
														.addComponent(txfTableDestiny, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(lblTableDestiny))
												.addPreferredGap(ComponentPlacement.RELATED)
												.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
														.addComponent(lblSaveAfter)
														.addComponent(txfSaveAfter, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(cbxIgnoreError)
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(cbxEnable)))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(lblColumnType)
								.addComponent(cbxColumnType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(113)));
		getContentPane().setLayout(groupLayout);
	}

	private boolean validateFields() {
		if (txfProcess.getText().isEmpty() || txfProcess.getText().equals("Teclar F9")) {
			bubbleWarning("Selecione o processo!");
			return false;
		} else if (txfOrder.getText().isEmpty() || txfOrder.getText() == null) {
			bubbleWarning("Informe a ordem da tabela!");
			return false;
		} else if (txfTableOrigin.getText().isEmpty() || txfTableOrigin.getText() == null) {
			bubbleWarning("Informe a tabela origem!");
			return false;
		} else if (txfOperation.getText().isEmpty() || txfOperation.getText() == null) {
			bubbleWarning("Informe a operação!");
			return false;
		} else if (txfTableDestiny.getText().isEmpty() || txfTableDestiny.getText() == null) {
			bubbleWarning("Informe a tabela destino!");
			return false;
		} else if (txfSaveAfter.getText().isEmpty() || txfSaveAfter.getText() == null) {
			bubbleWarning("Informe após quantos registro devem ser salvos!");
			return false;
		} else if (txfColumnKey.getText().isEmpty() || txfColumnKey.getText() == null) {
			bubbleWarning("Informe a chave da coluna!");
			return false;
		} else if (cbxColumnType.getSelectedItem().equals("--- Selecione ---") || cbxColumnType.getSelectedItem() == null) {
			bubbleWarning("Selecione o tipo da coluna!");
			return false;
		}

		return true;
	}
}
