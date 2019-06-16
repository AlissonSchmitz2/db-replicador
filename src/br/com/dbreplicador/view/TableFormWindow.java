
package br.com.dbreplicador.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.event.InternalFrameEvent;

import br.com.dbreplicador.dao.TableDAO;
import br.com.dbreplicador.image.MasterImage;
import br.com.dbreplicador.model.ProcessModel;
import br.com.dbreplicador.model.TableModel;
import br.com.dbreplicador.util.DateTimePicker;
import br.com.dbreplicador.util.InternalFrameListener;

public class TableFormWindow extends AbstractWindowFrame implements KeyEventPostProcessor{
	private static final long serialVersionUID = 3242592994592829458L;
	private JLabel lblProcess, lblOrder;
	private JTextField txfProcess;
	private ListProcessFormWindow searchProcessWindow;
	
	private ListTableFormWindow searchListTableWindow;

	// Guarda os fields em uma lista para facilitar manipulação em massa
	private List<Component> formFields = new ArrayList<Component>();

	// Componentes
	private JButton btnSearch, btnAdd, btnRemove, btnSave;
	private JTextField txfOrder, txfTableOrigin, txfTableDestiny, txfSaveAfter, txfColumnKey, txfColumnControl;
	private JLabel lblTableOrigin, lblTableDestiny, lblSaveAfter, lblColumnKey, lblColumnControl, lblLastReplication;
	private JCheckBox cbxEnable, cbxIgnoreError, cbxBackupIncre;
	private DateTimePicker dateTimePicker;
	private JDesktopPane desktop;
	private Connection connection;
	
	private TableModel tableModel;
	private TableDAO tableDAO;
	private JLabel lblColumnType;
	private JTextField txfColumnType;

	public TableFormWindow(JDesktopPane desktop, Connection connection) {
		super("Cadastro de Tabelas", 470, 430, desktop);
		this.desktop = desktop;
		this.connection = connection;
		
		setFrameIcon(MasterImage.details_16x16);
		
		createComponents();
		
		try {
			tableDAO = new TableDAO(connection);
		} catch (Exception error) {
			error.printStackTrace();
		}

		// Por padrão campos são desabilitados ao iniciar
		disableComponents(formFields);

		setButtonsActions();
		// Key events
		registerKeyEvent();
	}
	
	private void registerKeyEvent() {
		// Register key event post processor.
		TableFormWindow windowInstance = this;
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(windowInstance);

		// Unregister key event
		addInternalFrameListener(new InternalFrameListener() {
			@Override
			public void internalFrameClosed(InternalFrameEvent e) {
				KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventPostProcessor(windowInstance);
			}
		});
	}
	
	@Override
	public boolean postProcessKeyEvent(KeyEvent ke) {
		// Abre tela seleção cidade ao clicar F9
		if (ke.getID() == KeyEvent.KEY_PRESSED && ke.getKeyCode() == KeyEvent.VK_F9) {
			if (btnSave.isEnabled()) {
				openSearchProcess();
			}

			return true;
		}

		return false;
	}

	private void setButtonsActions() {
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (searchListTableWindow == null) {
					searchListTableWindow = new ListTableFormWindow(desktop, connection);

					searchListTableWindow.addInternalFrameListener(new InternalFrameListener() {
						
						@Override
						public void internalFrameClosed(InternalFrameEvent e) {
							TableModel selectedModel = ((ListTableFormWindow) e.getInternalFrame())
									.getSelectedModel();

							if (selectedModel != null) {
								// Atribui o model selecionado
								tableModel = selectedModel;

								// Seta dados do model para os campos
								txfProcess.setText(tableModel.getProcess());
								txfOrder.setText(tableModel.getOrder().toString());
								txfTableOrigin.setText(tableModel.getOriginTable());
//								txfOperation.setText(tableModel.isOperation()) ;REFATORAR
								txfTableDestiny.setText(tableModel.getDestinationTable());
								txfSaveAfter.setText(tableModel.getMaximumLines().toString());
								dateTimePicker.setDate(tableModel.getCurrentDateOf());
								cbxBackupIncre.setSelected(tableModel.isIncrementalBackup());
								cbxIgnoreError.setSelected(tableModel.isErrorIgnore());
								cbxEnable.setSelected(tableModel.isEnable());
								txfColumnKey.setText(tableModel.getKeyColumn());
								txfColumnType.setText(tableModel.getTypeColumn());
								txfColumnControl.setText(tableModel.getControlColumn());
								
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
							searchListTableWindow = null;
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
				txfProcess.setText("Teclar F9");
				clearFormFields(formFields);
				
				// Cria nova entidade model
				tableModel = new TableModel();

				// Seta valor padrão para o CheckBox "Habilitado" e "Backup Incremental"
				cbxEnable.setSelected(true);
				cbxBackupIncre.setSelected(true);
				
				btnRemove.setEnabled(false);
				btnSave.setEnabled(true);
			}
		});

		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(isEditing()) {
						if(tableDAO.delete(tableModel)) {
							bubbleSuccess("Tabela excluída com sucesso");
							
							// Seta form para modo Cadastro
							setFormMode(CREATE_MODE);

							// Desativa campos
							disableComponents(formFields);

							// Limpar dados dos campos
							clearFormFields(formFields);
							
							// Cria nova entidade model
							tableModel = new TableModel();
							
							// Desativa botão salvar
							btnSave.setEnabled(false);

							// Desativa botão remover
							btnRemove.setEnabled(false);
							
							txfProcess.setText("Teclar F9");
						} else {
							bubbleError("Houve um erro ao tentar excluir a tabela");
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
				
				tableModel.setCurrentDate(getDateTime(new Date()));
				tableModel.setDestinationTable(txfTableDestiny.getText());
				tableModel.setEnable(cbxEnable.isSelected());
				tableModel.setErrorIgnore(cbxIgnoreError.isSelected());
				tableModel.setKeyColumn(txfColumnKey.getText());
				tableModel.setControlColumn(txfColumnControl.getText());
				tableModel.setMaximumLines(Integer.parseInt(txfSaveAfter.getText()));
				tableModel.setIncrementalBackup(cbxBackupIncre.isSelected());
				tableModel.setOrder(Integer.parseInt(txfOrder.getText()));
				tableModel.setOriginTable(txfTableOrigin.getText());
				tableModel.setProcess(txfProcess.getText());
				tableModel.setTypeColumn(txfColumnType.getText());
				tableModel.setUser("admin");
				if(dateTimePicker.getDate() != null) tableModel.setCurrentDateOf(getDateTime(dateTimePicker.getDate()));
				
				try {
					// EDIÇÃO CADASTRO
					if(isEditing()) {						
						if(tableDAO.update(tableModel)) {
							bubbleSuccess("Tabela editada com sucesso");
						} else {
							bubbleError("Houve um erro ao editar a tabela!");
						}
					} 
					// NOVO CADASTRO
					else {
						// Insere a tabela no banco de dados
						TableModel insertedModel = tableDAO.insert(tableModel);
						
						if(insertedModel != null) {
							// Atribui o model recém criado ao model
							tableModel = insertedModel;

							// Seta form para edição
							setFormMode(UPDATE_MODE);

							// Ativa botão Remover
							btnRemove.setEnabled(true);
							
							bubbleSuccess("Tabelas cadastradas com sucesso");
						} else {
							bubbleError("Houve um erro ao cadastrar as tabelas!");
						}
					}
				} catch (Exception error) {
					error.printStackTrace();
					bubbleError(error.getMessage());
				}
			}
		});
	}
		
	private Timestamp getDateTime(Date date) {
		long dataTime = date.getTime();
		Timestamp ts = new Timestamp(dataTime);
		return ts;
	}
	
	private void openSearchProcess() {
		if (searchProcessWindow == null) {
			searchProcessWindow = new ListProcessFormWindow(desktop, connection);

			searchProcessWindow.addInternalFrameListener(new InternalFrameListener() {
				@Override
				public void internalFrameClosed(InternalFrameEvent e) {
					ProcessModel selectedModel = ((ListProcessFormWindow) e.getInternalFrame()).getSelectedModel();

					if (selectedModel != null) {
						// Atribui cidade para o model
						txfProcess.setText(selectedModel.getProcess());
					}

					// Reseta janela
					searchProcessWindow = null;
				}
			});
		}
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
		lblTableDestiny = new JLabel("Tabela Destino:");
		lblSaveAfter = new JLabel("Salvar Ap\u00F3s:");
		lblColumnKey = new JLabel("Coluna Chave:");
		lblColumnControl = new JLabel("Coluna Controle:");
		lblColumnType = new JLabel("Coluna Tipo:");
		lblLastReplication = new JLabel("\u00DAltima Replica\u00E7\u00E3o:");

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
		txfColumnControl = new JTextField();
		formFields.add(txfColumnControl);
		txfColumnType = new JTextField();
//		txfColumnType.setVisible(false);
		formFields.add(txfColumnType);
		cbxBackupIncre = new JCheckBox("Backup Incremental");
		formFields.add(cbxBackupIncre);
		
		dateTimePicker = new DateTimePicker();
		dateTimePicker.setFormats("dd/MM/yyyy HH:mm:ss");
		formFields.add(dateTimePicker);
		
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
								.addComponent(lblColumnKey)
								.addComponent(lblColumnType)
								.addComponent(lblOrder)
								.addComponent(lblProcess)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addComponent(lblTableOrigin)
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(lblSaveAfter)
										.addComponent(lblTableDestiny)))
								.addComponent(lblColumnControl))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(txfColumnType, GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(txfSaveAfter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(lblLastReplication)
									.addPreferredGap(ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
									.addComponent(dateTimePicker, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE))
								.addComponent(txfColumnKey, GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
								.addComponent(txfColumnControl, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(txfTableOrigin)
								.addComponent(cbxIgnoreError, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
								.addComponent(cbxEnable, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
								.addComponent(cbxBackupIncre, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
								.addComponent(txfTableDestiny)
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
									.addComponent(txfProcess, Alignment.LEADING)
									.addComponent(txfOrder, Alignment.LEADING)))))
					.addGap(29))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnRemove, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblProcess)
						.addComponent(txfProcess, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txfOrder, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblOrder))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txfTableOrigin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTableOrigin, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txfTableDestiny, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTableDestiny))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txfSaveAfter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblSaveAfter)
						.addComponent(dateTimePicker, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblLastReplication))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(cbxBackupIncre)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cbxIgnoreError)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cbxEnable)
					.addGap(7)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txfColumnKey, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblColumnKey))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txfColumnControl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblColumnControl))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txfColumnType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblColumnType))
					.addGap(88))
		);
		getContentPane().setLayout(groupLayout);
		
		txfProcess.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				openSearchProcess();
			}

			@Override
			public void focusLost(FocusEvent e) {
			}
		});
	}

	private boolean validateFields() {
		if (txfProcess.getText().equals("Teclar F9")) {
			bubbleWarning("Selecione o processo!");
			return false;
		} else if (txfOrder.getText().isEmpty() || txfOrder.getText() == null) {
			bubbleWarning("Informe a ordem da tabela!");
			return false;
		} else if (txfTableOrigin.getText().isEmpty() || txfTableOrigin.getText() == null) {
			bubbleWarning("Informe a tabela origem!");
			return false;
		} else if (txfTableDestiny.getText().isEmpty() || txfTableDestiny.getText() == null) {
			bubbleWarning("Informe a tabela destino!");
			return false;
		} else if (txfSaveAfter.getText().isEmpty() || txfSaveAfter.getText() == null) {
			bubbleWarning("Informe após quantos registro devem ser salvos!");
			return false;
		} else if (txfColumnKey.getText().isEmpty() || txfColumnKey.getText() == null) {
			bubbleWarning("Informe a coluna chave!");
			return false;
//		} else if (txfColumnControl.getText().isEmpty() || txfColumnControl.getText() == null) {
//			bubbleWarning("Selecione o tipo da coluna chave!");
//			return false;
		} else if (txfColumnControl.getText().isEmpty() || txfColumnControl.getText() == null) {
			bubbleWarning("Informe a coluna de controle!");
			return false;
		}

		return true;
	}
}
