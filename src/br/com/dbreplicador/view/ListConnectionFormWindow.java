package br.com.dbreplicador.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.GroupLayout.Alignment;

import br.com.dbreplicador.dao.ReplicationDAO;
import br.com.dbreplicador.image.MasterImage;
import br.com.dbreplicador.model.ConnectionModel;
import br.com.dbreplicador.view.tableModel.ConnectionTableModel;

public class ListConnectionFormWindow extends AbstractGridWindow {
	private static final long serialVersionUID = -959168673441667719L;
	
	private ReplicationDAO replicationDAO;
	private ConnectionModel replicationModel;
	
	private JButton btnSearch;
	private JTextField txfSearch;
	
	private ConnectionTableModel connectionTableModel;
	private JTable jTableModels;

	public ListConnectionFormWindow(JDesktopPane desktop, Connection CONNECTION) {
		super("Conexões", 445, 310, desktop, true);

		setFrameIcon(MasterImage.process_16x16);
		
		createComponents();
		
		try {
			replicationDAO = new ReplicationDAO(CONNECTION);
		} catch (SQLException error) {
			error.printStackTrace();
		}

		txfSearch.requestFocusInWindow();

		setButtonsActions();
	}
	
	public ConnectionModel getSelectedModel() {
		return replicationModel;
	}

	private void setButtonsActions() {
		// Ação Buscar
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadGrid(txfSearch.getText());
			}
		});
	}
	
	private void createComponents() {
		txfSearch = new JTextField();
		txfSearch.setToolTipText("Informe o nome do banco ou a descrição da conexão");
		txfSearch.requestFocusInWindow();
		txfSearch.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent ke) {
				if (ke.getID() == KeyEvent.KEY_PRESSED && ke.getKeyCode() == KeyEvent.VK_ENTER) {
					loadGrid(txfSearch.getText());
				}
			}

			public void keyReleased(KeyEvent keyEvent) {
			}

			public void keyTyped(KeyEvent keyEvent) {
			}
		});

		btnSearch = new JButton("Buscar");
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addComponent(txfSearch, GroupLayout.PREFERRED_SIZE, 330, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(10)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(txfSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)))
		);
		getContentPane().setLayout(groupLayout);

		createGrid();
	}
	
	private void createGrid() {
		connectionTableModel = new ConnectionTableModel();
		jTableModels = new JTable(connectionTableModel);

		jTableModels.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2) {
					// Atribui o model da linha clicada
					replicationModel = connectionTableModel.getModel(jTableModels.getSelectedRow());

					// Fecha a janela
					try {
						setClosed(true);
					} catch (PropertyVetoException e) {
						e.printStackTrace();
					}
				}
			}
		});

		// Habilita a seleção por linha
		jTableModels.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// Add ayout na grid
		jTableModels.setDefaultRenderer(Object.class, renderer);
		jTableModels.getColumnModel().getColumn(2).setMaxWidth(70);
		jTableModels.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent ke) {
				if (ke.getID() == KeyEvent.KEY_PRESSED && ke.getKeyCode() == KeyEvent.VK_ENTER) {
					// Atribui o model da linha selecionada
					replicationModel = connectionTableModel.getModel(jTableModels.getSelectedRow());

					// Fecha a janela
					try {
						setClosed(true);
					} catch (PropertyVetoException e) {
						e.printStackTrace();
					}
				}
			}

			public void keyReleased(KeyEvent keyEvent) {
			}

			public void keyTyped(KeyEvent keyEvent) {
			}
		});

		grid = new JScrollPane(jTableModels);
		resizeGrid(grid, 5, 40, 420, 230);
		grid.setVisible(true);

		add(grid);
	}
	
	private void loadGrid(String word) {
		/*if (word.length() < 1) {
			bubbleWarning("Você precisa inserir ao menos 1 caracter para iniciar a busca");
		  return;
		}*/

		connectionTableModel.clear();

		try {
			connectionTableModel.addModelsList(replicationDAO.search(word));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
