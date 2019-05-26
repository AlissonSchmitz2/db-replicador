package br.com.dbreplicador.view;

import javax.swing.JButton;
import javax.swing.JDesktopPane;

import br.com.dbreplicador.image.MasterImage;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class ConnectionFormWindow extends AbstractWindowFrame {
	private static final long serialVersionUID = 3721635335554059099L;

	//Componentes
	private JButton btnSearch, btnAdd, btnRemove, btnSave;
	private JLabel lblDescription, lblAddressIP, lblPort, lblNameDB, lblModelDB, lblCompany;
	private JTextField txfDescription, txfAddressIP, txfPort, txfNameDB, txfCompany;
	private JComboBox<String> cbxModelDB;
	private JButton btnTestarConexo;

	// Guarda os fields em uma lista para facilitar manipulação em massa
	private List<Component> formFields = new ArrayList<Component>();

	public ConnectionFormWindow(JDesktopPane desktop) {
		super("Cadastro de Conexões", 455, 330, desktop);

		setFrameIcon(MasterImage.aplication_16x16);
		
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
		lblDescription = new JLabel("Descri\u00E7\u00E3o:");
		lblAddressIP = new JLabel("Endere\u00E7o (IP):");
		lblPort = new JLabel("Porta:");
		lblNameDB = new JLabel("Nome do Banco:");
		lblModelDB = new JLabel("Modelo do Banco:");
		lblCompany = new JLabel("Estabelecimento:");

		// TextFields
		txfDescription = new JTextField();
		txfDescription.setColumns(10);
		formFields.add(txfDescription);
		txfAddressIP = new JTextField();
		txfAddressIP.setColumns(10);
		formFields.add(txfAddressIP);
		txfPort = new JTextField();
		txfPort.setColumns(10);
		formFields.add(txfPort);
		txfNameDB = new JTextField();
		txfNameDB.setColumns(10);
		formFields.add(txfNameDB);
		cbxModelDB = new JComboBox<String>();
		formFields.add(cbxModelDB);
		txfCompany = new JTextField();
		txfCompany.setColumns(10);
		formFields.add(txfCompany);

		btnTestarConexo = new JButton("Testar Conex\u00E3o");
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(15)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
									.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
									.addComponent(btnRemove, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
									.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(lblCompany)
										.addComponent(lblDescription)
										.addComponent(lblAddressIP)
										.addComponent(lblPort)
										.addComponent(lblNameDB)
										.addComponent(lblModelDB))
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(txfCompany, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
										.addComponent(txfPort, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
											.addComponent(txfAddressIP, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
											.addComponent(txfDescription))
										.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
											.addComponent(cbxModelDB, Alignment.LEADING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
											.addComponent(txfNameDB, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(165)
							.addComponent(btnTestarConexo)))
					.addContainerGap(14, Short.MAX_VALUE))
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
						.addComponent(lblDescription)
						.addComponent(txfDescription, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblAddressIP)
						.addComponent(txfAddressIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblPort)
						.addComponent(txfPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txfNameDB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNameDB))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(cbxModelDB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblModelDB))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txfCompany, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCompany))
					.addGap(21)
					.addComponent(btnTestarConexo, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(26, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
	}
	
//	private boolean validateFields() {
//		//TODO: Validar campos
//		return true;
//	}
}