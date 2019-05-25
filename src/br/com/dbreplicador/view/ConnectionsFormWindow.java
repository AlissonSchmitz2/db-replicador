package br.com.dbreplicador.view;

import javax.swing.JButton;
import javax.swing.JDesktopPane;

import br.com.dbreplicador.image.MasterImage;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class ConnectionsFormWindow extends AbstractWindowFrame {
	private static final long serialVersionUID = 3721635335554059099L;

	//Componentes
	private JButton btnSearch, btnAdd, btnRemove, btnSave;
	private JLabel lblDescription, lblAddressIP, lblPort, lblNameDB, lblModelDB, lblCompany;
	private JTextField txfDescription, txfAddressIP, txfPort, txfNameDB, txfCompany;
	private JComboBox<String> cbxModelDB;
	private JButton btnTestarConexo;
	
	//Conexão
	private Connection CONNECTION;

	// Guarda os fields em uma lista para facilitar manipulação em massa
	private List<Component> formFields = new ArrayList<Component>();

	public ConnectionsFormWindow(JDesktopPane desktop, Connection CONNECTION) {
		super("Cadastro de Conexões", 455, 330, desktop);
		
		this.CONNECTION = CONNECTION;

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
		formFields.add(btnSave);
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
		groupLayout
				.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGroup(groupLayout
								.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup()
										.addGap(15).addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
												.addGroup(groupLayout.createSequentialGroup()
														.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 95,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 110,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(btnRemove, GroupLayout.PREFERRED_SIZE, 110,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 95,
																GroupLayout.PREFERRED_SIZE))
												.addGroup(groupLayout.createSequentialGroup()
														.addPreferredGap(ComponentPlacement.RELATED)
														.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
																.addComponent(lblDescription).addComponent(lblAddressIP)
																.addComponent(lblPort).addComponent(lblNameDB)
																.addComponent(lblCompany).addComponent(lblModelDB))
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
																.addComponent(txfCompany, GroupLayout.DEFAULT_SIZE, 315,
																		Short.MAX_VALUE)
																.addComponent(txfPort, GroupLayout.PREFERRED_SIZE, 111,
																		GroupLayout.PREFERRED_SIZE)
																.addComponent(txfAddressIP, GroupLayout.DEFAULT_SIZE,
																		315, Short.MAX_VALUE)
																.addComponent(txfDescription)
																.addComponent(txfNameDB, GroupLayout.PREFERRED_SIZE,
																		221, GroupLayout.PREFERRED_SIZE)
																.addComponent(cbxModelDB, 0, 315, Short.MAX_VALUE)))))
								.addGroup(
										groupLayout.createSequentialGroup().addGap(165).addComponent(btnTestarConexo)))
								.addContainerGap(14, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addGap(5)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnRemove, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
						.addGap(18)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblDescription)
								.addComponent(txfDescription, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(12)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblAddressIP)
								.addComponent(txfAddressIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblPort)
								.addComponent(txfPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblNameDB)
								.addComponent(txfNameDB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(11)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblModelDB)
								.addComponent(cbxModelDB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblCompany)
								.addComponent(txfCompany, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(18).addComponent(btnTestarConexo, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
						.addContainerGap()));
		getContentPane().setLayout(groupLayout);
	}
	
	private boolean validateFields() {
		//TODO: Validar campos
		return true;
	}
}
