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
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import br.com.dbreplicador.dao.ProcessDAO;
import br.com.dbreplicador.dao.TableDAO;
import br.com.dbreplicador.image.MasterImage;
import br.com.dbreplicador.model.TableModel;
import br.com.dbreplicador.view.tableModel.TablesTableModel;

public class ListTableFormWindow extends AbstractGridWindow {
	private static final long serialVersionUID = 971247642647028655L;
	
	private TableDAO tableDAO;
	private TableModel tableModel;

	private JButton btnSearch;
	private JTextField txfSearch;

	private TablesTableModel tablesTableModel;
	private JTable jTableModels;

	public ListTableFormWindow(JDesktopPane desktop, Connection CONNECTION) {
		super("Tabelas", 445, 310, desktop, true);

		setFrameIcon(MasterImage.details_16x16);
		
		createComponents();

		try {
			tableDAO = new TableDAO(CONNECTION);
		} catch (SQLException error) {
			error.printStackTrace();
		}
		
		txfSearch.requestFocusInWindow();

		setButtonsActions();
	}

	public TableModel getSelectedModel() {
		return tableModel;
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
		txfSearch.setToolTipText("Informe o aluno");
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
		tablesTableModel = new TablesTableModel();
		jTableModels = new JTable(tablesTableModel);

		jTableModels.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2) {
					// Atribui o model da linha clicada
					tableModel = tablesTableModel.getModel(jTableModels.getSelectedRow());

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
		jTableModels.getColumnModel().getColumn(0).setMaxWidth(60);
		jTableModels.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent ke) {
				if (ke.getID() == KeyEvent.KEY_PRESSED && ke.getKeyCode() == KeyEvent.VK_ENTER) {
					// Atribui o model da linha selecionada
					tableModel = tablesTableModel.getModel(jTableModels.getSelectedRow());

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
		if (word.length() < 1) {
			bubbleWarning("Você precisa inserir ao menos 1 caracter para iniciar a busca");
		  return;
		}

		tablesTableModel.clear();

		try {
			tablesTableModel.addModelsList(tableDAO.search(word));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
