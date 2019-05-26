
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

import javax.swing.JProgressBar;

public class ReplicatorWindow extends AbstractWindowFrame {
	private static final long serialVersionUID = -4888464460307835343L;

	// Guarda os fields em uma lista para facilitar manipulação em massa
	private List<Component> formFields = new ArrayList<Component>();

	// Componentes
	private JTextField txfProcess, txfOrigin, txfDestiny;
	private JLabel lblOrigin, lblDestiny, lblProcess, lblRows, lblRowInit, lblRows2, lblRowsFinal;
	private JProgressBar progressBar;
	private JButton btnReplicate;
	
	public ReplicatorWindow(JDesktopPane desktop) {
		super("Replicar", 655, 270, desktop);

		setFrameIcon(MasterImage.replicator_16x16);
		
		createComponents();

		// Por padrão campos são desabilitados ao iniciar
		disableComponents(formFields);

		setButtonsActions();
	}

	private void setButtonsActions() {
		btnReplicate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				progressBar.setIndeterminate(true);
				progressBar.setStringPainted(true);
				progressBar.setString("a");
			}
		});
	}

	private void createComponents() {

		lblOrigin = new JLabel("Origem:");
		lblDestiny = new JLabel("Destino:");
		lblProcess = new JLabel("Processo:");
		txfProcess = new JTextField();
		txfProcess.setColumns(10);
		txfOrigin = new JTextField();
		txfOrigin.setColumns(10);
		txfDestiny = new JTextField();
		txfDestiny.setColumns(10);

		lblRows = new JLabel("Linhas:");
		lblRowInit = new JLabel("1");
		lblRows2 = new JLabel("/");
		lblRowsFinal = new JLabel("100");

		progressBar = new JProgressBar();
		btnReplicate = new JButton("Replicar");

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addContainerGap()
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(progressBar, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(lblDestiny)
										.addGroup(groupLayout.createSequentialGroup().addGap(9).addComponent(lblOrigin,
												GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
										.addComponent(lblProcess))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(txfDestiny, Alignment.LEADING)
										.addComponent(txfOrigin, Alignment.LEADING).addComponent(txfProcess,
												Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 175,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED, 293, Short.MAX_VALUE).addComponent(lblRows)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblRowInit)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblRows2)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblRowsFinal)))
				.addGap(20))
				.addGroup(groupLayout.createSequentialGroup().addGap(280)
						.addComponent(btnReplicate, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(269, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(txfProcess, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblProcess).addComponent(lblRows).addComponent(lblRowInit)
								.addComponent(lblRows2).addComponent(lblRowsFinal))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(txfOrigin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblOrigin))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(txfDestiny, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblDestiny))
						.addGap(32)
						.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addComponent(btnReplicate, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(44, Short.MAX_VALUE)));
		getContentPane().setLayout(groupLayout);
	}
}
