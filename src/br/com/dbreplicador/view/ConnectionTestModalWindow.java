package br.com.dbreplicador.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import br.com.replicator.database.ConnectionFactory;
import br.com.replicator.database.ConnectionInfo;
import br.com.replicator.enums.SupportedTypes;

public class ConnectionTestModalWindow extends JDialog {
	private static final long serialVersionUID = 9020482091831430989L;

	private JLabel lblUser, lblPassword;
	private JTextField txfUser;
	private JPasswordField txfPassword;
	private JButton btnOK;
	private ConnectionInfo connectionInfo;

	// Informações da conexão
	private String dbType, dbHost, dbName;
	private int dbPort;

	public ConnectionTestModalWindow(String dbType, String dbHost, int dbPort, String dbName) {
		this.dbType = dbType;
		this.dbHost = dbHost;
		this.dbPort = dbPort;
		this.dbName = dbName;

		setTitle("Testar Conexão");
		setModal(true);
		setBackground(new Color(239, 239, 239));
		setSize(266, 140);
		setLocationRelativeTo(null);
		setResizable(false);

		createComponents();

		setButtonsActions();
		setVisible(true);
	}

	private void setButtonsActions() {
		btnOK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				testConnection();
			}
		});
	}

	private void createComponents() {

		// Labels
		lblUser = new JLabel("Usu\u00E1rio:");
		lblPassword = new JLabel("Senha:");

		// TextFields
		txfUser = new JTextField();
		txfUser.setColumns(10);
		txfPassword = new JPasswordField();
		txfPassword.setColumns(10);

		btnOK = new JButton("OK");

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addGap(19)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGap(6)
								.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(txfPassword, GroupLayout.PREFERRED_SIZE, 169, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup().addComponent(lblUser)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(txfUser, GroupLayout.PREFERRED_SIZE, 169, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap(12, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup().addContainerGap(114, Short.MAX_VALUE)
						.addComponent(btnOK).addGap(89)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addGap(14)
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(lblUser).addComponent(
						txfUser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txfPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPassword))
				.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(btnOK)
				.addContainerGap(17, Short.MAX_VALUE)));
		getContentPane().setLayout(groupLayout);

		txfUser.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ENTER) {
					testConnection();
				}
			}
		});

		txfPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ENTER) {
					testConnection();
				}
			}
		});
	}

	private void testConnection() {
		if (dbType.equals("MySQL")) {
			connectionInfo = new ConnectionInfo(SupportedTypes.MYSQL, dbHost, dbPort, dbName + "?useSSL=false",
					txfUser.getText(), new String(txfPassword.getPassword()));
		} else {
			connectionInfo = new ConnectionInfo(SupportedTypes.POSTGRESQL, dbHost, dbPort, dbName, txfUser.getText(),
					new String(txfPassword.getPassword()));

		}

		Connection connOrigin;
		try {
			connOrigin = ConnectionFactory.getConnection(connectionInfo);
			connOrigin.setAutoCommit(false);

			JOptionPane.showMessageDialog(null, "Conexão realizada com sucesso!");
			connOrigin.close();
			dispose();

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Problema ao conectar no banco de dados informado, favor verificar os dados.", "Erro",
					JOptionPane.ERROR_MESSAGE, null);
			return;
		}
	}
}
