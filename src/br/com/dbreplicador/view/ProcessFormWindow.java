package br.com.dbreplicador.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.event.InternalFrameEvent;

import com.toedter.calendar.JDateChooser;

import br.com.dbreplicador.dao.ProcessDAO;
import br.com.dbreplicador.database.ConnectionFactory;
import br.com.dbreplicador.image.MasterImage;
import br.com.dbreplicador.model.ProcessModel;
import br.com.dbreplicador.util.InternalFrameListener;

public class ProcessFormWindow extends AbstractWindowFrame{
	private static final long serialVersionUID = -4888464460307835343L;
	
	// Componentes
	private JButton btnSearch, btnAdd, btnRemove, btnSave;
	private JLabel lblProcess, lblDescription, lblDateOf;
	private JTextField txfProcess, txfDescription;
	private JDateChooser jDateFor;
	private JCheckBox cbxIgnoreError, cbxEnable;
	private JDesktopPane desktop;

	// Guarda os fields em uma lista para facilitar manipulação em massa
	private List<Component> formFields = new ArrayList<Component>();
	
	private ListProcessFormWindow searchProcessWindow;
	
	private ProcessModel processModel;
	private ProcessDAO processDAO;
	// TODO: Conexão provisória (Refatorar)
	private Connection CONNECTION = ConnectionFactory.getConnection("postgres", "ssda7321");

	public ProcessFormWindow(JDesktopPane desktop) {
		super("Cadastro de Processos", 455, 270, desktop);

		this.desktop = desktop;
		
		createComponents();
		
		setFrameIcon(MasterImage.process_16x16);
		
		try {
			processDAO = new ProcessDAO(CONNECTION);
		} catch (Exception error) {
			error.printStackTrace();
		}

		// Por padrão campos são desabilitados ao iniciar
		disableComponents(formFields);

		setButtonsActions();
	}

	private void setButtonsActions() {
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (searchProcessWindow == null) {
					searchProcessWindow = new ListProcessFormWindow(desktop, CONNECTION);

					searchProcessWindow.addInternalFrameListener(new InternalFrameListener() {
						
						@Override
						public void internalFrameClosed(InternalFrameEvent e) {
							ProcessModel selectedModel = ((ListProcessFormWindow) e.getInternalFrame())
									.getSelectedModel();

							if (selectedModel != null) {
								// Atribui o model selecionado
								processModel = selectedModel;

								// Seta dados do model para os campos
								txfProcess.setText(processModel.getProcess());
								txfDescription.setText(processModel.getDescription());
								jDateFor.setDate(processModel.getCurrentDateOf());
								cbxEnable.setSelected(processModel.isEnable());
								cbxIgnoreError.setSelected(processModel.isErrorIgnore());
								
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
							searchProcessWindow = null;
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
				processModel = new ProcessModel();
				
				btnRemove.setEnabled(false);
				btnSave.setEnabled(true);
			}
		});

		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(isEditing()) {
						if(processDAO.delete(processModel)) {
							bubbleSuccess("Processo excluído com sucesso");
							
							// Seta form para modo Cadastro
							setFormMode(CREATE_MODE);

							// Desativa campos
							disableComponents(formFields);

							// Limpar dados dos campos
							clearFormFields(formFields);
							
							// Cria nova entidade model
							processModel = new ProcessModel();
							
							// Desativa botão salvar
							btnSave.setEnabled(false);

							// Desativa botão remover
							btnRemove.setEnabled(false);
						} else {
							bubbleError("Houve um erro ao tentar excluir o processo");
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
				
				processModel.setCurrentDate(getDateTime(new Date()));
				processModel.setUser("admin");
				processModel.setProcess(txfProcess.getText());
				processModel.setDescription(txfDescription.getText());
				processModel.setCurrentDateOf(getDateTime(jDateFor.getDate()));
				processModel.setEnable(cbxEnable.isSelected());
				processModel.setErrorIgnore(cbxIgnoreError.isSelected());
				
				try {
					// EDIÇÃO CADASTRO
					if(isEditing()) {						
						if(processDAO.update(processModel)) {
							bubbleSuccess("Processo editado com sucesso");
						} else {
							bubbleError("Houve um erro ao editar o processo");
						}
					} 
					// NOVO CADASTRO
					else {
						// Insere o processo no banco de dados
						ProcessModel insertedModel = processDAO.insert(processModel);
						
						if(insertedModel != null) {
							// Atribui o model recém criado ao model
							processModel = insertedModel;

							// Seta form para edição
							setFormMode(UPDATE_MODE);

							// Ativa botão Remover
							btnRemove.setEnabled(true);
							
							bubbleSuccess("Processo cadastrado com sucesso");
						} else {
							bubbleError("Houve um erro ao cadastrar o processo");
						}
					}
				} catch (Exception error) {
					error.printStackTrace();
					bubbleError(error.getMessage());
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
	
	private boolean validateFields() {
		if(txfProcess.getText().isEmpty() || txfProcess.getText() == null) {
			bubbleWarning("Informe o nome do processo!");
			return false;
		} else if(txfDescription.getText().isEmpty() || txfDescription.getText() == null) {
			bubbleWarning("Informe uma descrição para o processo!");
			return false;
		} 		
		
		return true;
	}
	
	private Timestamp getDateTime(Date date) {
		long dataTime = date.getTime();
		Timestamp ts = new Timestamp(dataTime);
		return ts;
	}
}
