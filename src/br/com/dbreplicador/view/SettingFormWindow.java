package br.com.dbreplicador.view;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import br.com.dbreplicador.image.MasterImage;
import br.com.dbreplicador.model.SettingDBReplicator;
import br.com.dbreplicador.util.ManipulateFile;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPasswordField;
import javax.swing.JButton;

public class SettingFormWindow extends AbstractWindowFrame {
	private static final long serialVersionUID = -7938162284617422195L;

	private JLabel lblUser;
	private JTextField txfUser;
	private JButton btnSave;
	private JPasswordField txfPassword;
	private ManipulateFile man = new ManipulateFile();

	public SettingFormWindow(JDesktopPane desktop) {
		super("Configuração", 370, 240, desktop);
		createComponents();

		setFrameIcon(MasterImage.setting_16x16);

		setButtonsActions();
	}

	private void setButtonsActions() {
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (!validateFields()) {
					return;
				}

				man.inserirDadosNoArquivo(txfUser.getText(), new String(txfPassword.getPassword()));

				bubbleSuccess("Dados salvos com sucesso!");
			}
		});

	}

	private void createComponents() {

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Conex\u00E3o Banco Replicador",
				TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));

		btnSave = new JButton("SALVAR", MasterImage.save_22x22);

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(52)
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(126)
							.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(41, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(23)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
					.addGap(27)
					.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(52, Short.MAX_VALUE))
		);

		// Labels
		lblUser = new JLabel("Usu\u00E1rio:");

		// TextFields
		txfUser = new JTextField();
		txfUser.setColumns(10);

		txfPassword = new JPasswordField();
		txfPassword.setColumns(10);

		JLabel lblPassword = new JLabel("Senha:");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup().addComponent(lblUser)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(txfUser, GroupLayout.PREFERRED_SIZE, 169, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
								.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addGap(10).addComponent(txfPassword, GroupLayout.PREFERRED_SIZE, 169,
										GroupLayout.PREFERRED_SIZE)))
				.addContainerGap(20, Short.MAX_VALUE)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblUser).addComponent(txfUser,
						GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup().addGap(3).addComponent(lblPassword))
						.addComponent(txfPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addContainerGap(61, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);

		recoverDataDB();
	}

	private void recoverDataDB() {
		SettingDBReplicator settingDBReplicator = new SettingDBReplicator();

		settingDBReplicator = man.RecoverData();

		txfUser.setText(settingDBReplicator.getUser());
		txfPassword.setText(settingDBReplicator.getPassword());
	}

	private boolean validateFields() {
		if (txfUser.getText().isEmpty() || txfUser.getText() == null) {
			bubbleWarning("Informe o usuário do banco de dados!");
			return false;
		} else if (new String(txfPassword.getPassword()).equals("") || txfPassword.getPassword() == null) {
			bubbleWarning("Informe uma descrição para o processo!");
			return false;
		}

		return true;
	}
}
