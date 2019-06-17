package br.com.dbreplicador.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.InternalFrameEvent;
import javax.swing.text.DefaultFormatterFactory;

import br.com.dbreplicador.dao.ReplicationDAO;
import br.com.dbreplicador.enums.Databases;
import br.com.dbreplicador.image.MasterImage;
import br.com.dbreplicador.model.ConnectionModel;
import br.com.dbreplicador.pojos.Database;
import br.com.dbreplicador.util.InternalFrameListener;
import br.com.dbreplicador.util.RegexFormatter;
import br.com.dbreplicador.view.combomodel.GenericComboModel;

public class ConnectionFormWindow extends AbstractWindowFrame {
	private static final long serialVersionUID = 3721635335554059099L;

	//Componentes
	private JButton btnSearch, btnAdd, btnRemove, btnSave;
	private JLabel lblDescription, lblAddressIP, lblPort, lblNameDB, lblModelDB;
	private JTextField txfDescription, txfPort, txfNameDB;
	private JFormattedTextField txfAddressIP;
	private JComboBox<Database> cbxModelDB;
	private JButton btnTestarConexo;
	private JDesktopPane desktop;
	
	// Expressão regular para verificar se o IP digitado  é valido
	private String _255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
	private Pattern p = Pattern.compile("^(?:" + _255 + "\\.){3}" + _255 + "$");
	private RegexFormatter ipFormatter = new RegexFormatter(p);
	
	// Guarda os fields em uma lista para facilitar manipulação em massa
	private List<Component> formFields = new ArrayList<Component>();
	
	private ListConnectionFormWindow searchConnectionWindow;
	
	// Banco de dados
	private ConnectionModel replicationModel;
	private ReplicationDAO replicationDAO;
	private Connection connection;
	
	public ConnectionFormWindow(JDesktopPane desktop, Connection connection) {
		super("Cadastro de Conexões", 455, 330, desktop);
		this.desktop = desktop;
		this.connection = connection;
		
		setFrameIcon(MasterImage.aplication_16x16);
		
		try {
			replicationDAO = new ReplicationDAO(connection);
		} catch (Exception error) {
			error.printStackTrace();
		}
		
		createComponents();

		// Por padrão campos são desabilitados ao iniciar
		disableComponents(formFields);

		setButtonsActions();
	}

	private void setButtonsActions() {
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (searchConnectionWindow == null) {
					searchConnectionWindow = new ListConnectionFormWindow(desktop, connection);

					searchConnectionWindow.addInternalFrameListener(new InternalFrameListener() {
						
						@Override
						public void internalFrameClosed(InternalFrameEvent e) {
							ConnectionModel selectedModel = ((ListConnectionFormWindow) e.getInternalFrame())
									.getSelectedModel();

							if (selectedModel != null) {
								// Atribui o model selecionado
								replicationModel = selectedModel;

								// Seta dados do model para os campos
								txfDescription.setText(replicationModel.getName());
								txfAddressIP.setText(replicationModel.getAddress());
								txfPort.setText(replicationModel.getPort().toString());
								txfNameDB.setText(replicationModel.getDatabase());

								if(replicationModel.getDatebaseType().equals("PostgreSQL")) {
									cbxModelDB.setSelectedIndex(2);
								} else {
									cbxModelDB.setSelectedIndex(1);
								}
								
								// Seta form para modo Edição
								setFormMode(UPDATE_MODE);

								// Ativa campos
								enableComponents(formFields);

								// Ativa botão salvar
								btnSave.setEnabled(true);

								// Ativa botão remover
								btnRemove.setEnabled(true);
							}

							// Reseta janela
							searchConnectionWindow = null;
						}
					});
				}
			}
		});

		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Seta form para modo Cadastro
				setFormMode(CREATE_MODE);
				
				// Ativa campos
				enableComponents(formFields);
				
				// Limpar dados dos campos
				clearFormFields(formFields);
				
				// Cria nova entidade model
				replicationModel = new ConnectionModel();
				
				btnRemove.setEnabled(false);
				btnSave.setEnabled(true);
			}
		});

		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(isEditing()) {
						if(replicationDAO.delete(replicationModel)) {
							bubbleSuccess("Conexão excluída com sucesso");
							
							// Seta form para modo Cadastro
							setFormMode(CREATE_MODE);

							// Desativa campos
							disableComponents(formFields);

							// Limpar dados dos campos
							clearFormFields(formFields);
							
							// Cria nova entidade model
							replicationModel = new ConnectionModel();
							
							// Desativa botão salvar
							btnSave.setEnabled(false);

							// Desativa botão remover
							btnRemove.setEnabled(false);
						} else {
							bubbleError("Houve um erro ao tentar excluir a conexão");
						}
					}
				} catch (Exception error) {
					bubbleError(error.getMessage());
					error.printStackTrace();
				}
			}
		});

		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!validateFields()) {
					return;
				}
				
				replicationModel.setAddress(txfAddressIP.getText());
				replicationModel.setCurrentDate(getDateTime(new Date()));
				replicationModel.setUser("admin");
				replicationModel.setDatabase(txfNameDB.getText());
				replicationModel.setDatebaseType(cbxModelDB.getSelectedItem().toString());
				replicationModel.setName(txfDescription.getText());

				try {
					replicationModel.setPort(Integer.parseInt(txfPort.getText()));
				} catch (Exception error) {
					bubbleError("A porta digitada é inválida!");
					return;
				}	
				
				if(cbxModelDB.getSelectedItem().toString().equals("MySQL")) {
					replicationModel.setUrl("mysql://" + replicationModel.getAddress() + ":"
							+ replicationModel.getPort() + "/" + replicationModel.getDatabase());
				} else if (cbxModelDB.getSelectedItem().toString().equals("PostgreSQL")) {					
					replicationModel.setUrl("postgresql://" + replicationModel.getAddress() + ":"
							+ replicationModel.getPort() + "/" + replicationModel.getDatabase());
				}
												
				try {
					// EDIÇÃO CADASTRO
					if(isEditing()) {						
						if(replicationDAO.update(replicationModel)) {
							bubbleSuccess("Conexão editada com sucesso");
						} else {
							bubbleError("Houve um erro ao editar a conexão");
						}
					} 
					// NOVO CADASTRO
					else {
						// Insere a conexão no banco de dados
						ConnectionModel insertedModel = replicationDAO.insert(replicationModel);
						
						if(insertedModel != null) {
							// Atribui o model recém criado ao model
							replicationModel = insertedModel;

							// Seta form para edição
							setFormMode(UPDATE_MODE);

							// Ativa botão Remover
							btnRemove.setEnabled(true);
							
							bubbleSuccess("Conexão cadastrada com sucesso");
						} else {
							bubbleError("Houve um erro ao cadastrar a conexão");
						}
					}
				} catch (Exception error) {
					error.printStackTrace();
					bubbleError(error.getMessage());
				}
				
			}
		});
		
		btnTestarConexo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!validateFields()) {
					return;
				}
				
				// TODO Testar conexao com bd
				
				
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
		//lblCompany = new JLabel("Estabelecimento:");

		// TextFields
		txfDescription = new JTextField();
		txfDescription.setColumns(10);
		formFields.add(txfDescription);
		txfAddressIP = new JFormattedTextField();
		txfAddressIP.setFormatterFactory(new DefaultFormatterFactory(ipFormatter));
		txfAddressIP.setColumns(10);
		formFields.add(txfAddressIP);
		txfPort = new JTextField();
		txfPort.setColumns(10);
		formFields.add(txfPort);
		txfNameDB = new JTextField();
		txfNameDB.setColumns(10);
		formFields.add(txfNameDB);
		
		List<Database> databaseList = new ArrayList<>();
		databaseList.add(new Database("", "-- Selecione --"));
		Databases.getDatabases().forEach((code, description) -> databaseList.add(new Database(code, description)));
		
		cbxModelDB = new JComboBox<Database>();
		cbxModelDB.setModel(new GenericComboModel<Database>(databaseList));
		cbxModelDB.setSelectedIndex(0);
		formFields.add(cbxModelDB);
		//txfCompany = new JTextField();
		//txfCompany.setColumns(10);
		//formFields.add(txfCompany);

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
										.addComponent(lblPort)
										.addComponent(lblDescription)
										.addComponent(lblAddressIP)
										.addComponent(lblNameDB)
										.addComponent(lblModelDB))
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
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
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txfPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPort))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txfNameDB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNameDB))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(cbxModelDB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblModelDB))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnTestarConexo, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(52, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
	}
	
	private boolean validateFields() {
		if(txfDescription.getText().isEmpty() || txfDescription.getText() == null) {
			bubbleWarning("Informe uma descrição para a conexão!");
			return false;
		} else if(txfAddressIP.getText().isEmpty() || txfAddressIP.getText() == null) {
			bubbleWarning("Informe o endereço de IP!");
			return false;
		}  else if(txfPort.getText().isEmpty() || txfPort.getText() == null) {
			bubbleWarning("Informe a porta do endereço da conexão!");
			return false;
		}  else if(txfNameDB.getText().isEmpty() || txfNameDB.getText() == null) {
			bubbleWarning("Informe o nome do banco!");
			return false;
		}  else if(cbxModelDB.getSelectedItem().equals("-- Selecione --")|| cbxModelDB.getSelectedItem() == null) {
			bubbleWarning("Selecione o modelo do banco!");
			return false;
		}  /*else if(txfCompany.getText().isEmpty() || txfCompany.getText() == null) {
			bubbleWarning("Informe o estabelecimento!");
			return false;
		} */
		
		return true;
	}
	
	private Timestamp getDateTime(Date date) {
		long dataTime = date.getTime();
		Timestamp ts = new Timestamp(dataTime);
		return ts;
	}
}