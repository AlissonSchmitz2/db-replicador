package br.com.dbreplicador.view;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import br.com.dbreplicador.dao.ExecutionDAO;
import br.com.dbreplicador.dao.TableExecutionDAO;
import br.com.dbreplicador.image.MasterImage;
import br.com.dbreplicador.model.ExecutionModel;
import br.com.dbreplicador.view.tableModel.ExecutionTTableModel;
import br.com.dbreplicador.view.tableModel.ExecutionTableModel;

public class QueryFormWindow extends AbstractGridWindow{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JDesktopPane desktop; 
	private Connection connection;
	
	private TableExecutionDAO tableExecutionDAO;
	private ExecutionDAO executionDAO = null;
	
	private ExecutionTableModel executionTableModel;
	private JTable jExecution;

	private ExecutionTTableModel executionTTableModel;
	private JTable jExecutionTable;
	
	private JLabel label;
	
	public QueryFormWindow(JDesktopPane desktop, Connection connection) {
		super("Consulta de Logs", 800, 600, desktop, false);
		this.desktop = desktop;
		this.connection = connection;

		setFrameIcon(MasterImage.direction_16x16);

		try {
			tableExecutionDAO = new TableExecutionDAO(connection);
			executionDAO = new ExecutionDAO(connection);
		} catch (Exception error) {
			error.printStackTrace();
		}

		createComponents();
		
		// Por padrão campos são desabilitados ao iniciar
		//disableComponents(formFields);

		setButtonsActions();
		// Key events
		registerKeyEvent();
	}
	
	private void createGridExecution() {
		executionTableModel = new ExecutionTableModel();
		jExecution = new JTable(executionTableModel);

		jExecution.getColumnModel().getColumn(0).setPreferredWidth(120);
		jExecution.getColumnModel().getColumn(1).setPreferredWidth(100);
		jExecution.getColumnModel().getColumn(2).setPreferredWidth(150);
		jExecution.getColumnModel().getColumn(3).setPreferredWidth(150);
		jExecution.getColumnModel().getColumn(4).setPreferredWidth(150);
		jExecution.getColumnModel().getColumn(5).setPreferredWidth(120);
		jExecution.getColumnModel().getColumn(6).setPreferredWidth(100);
		jExecution.getColumnModel().getColumn(7).setPreferredWidth(200);
		
		// Habilita a seleção por linha
		jExecution.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		new JScrollPane(jExecution, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jExecution.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		grid = new JScrollPane(jExecution);
		setLayout(null);
		resizeGrid(grid, 5, 100, 770, 180);
		grid.setVisible(true);

		add(grid);
	
		try {
			executionTableModel.addModelsList(executionDAO.selectAll());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		/*
		jExecution.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 1) {
					// Atribui o model da linha clicada
					//ExecutionModel executionModel = executionTableModel.getModel(jExecution.getSelectedRow());
					
					try {
						executionTTableModel.addModelsList(tableExecutionDAO.selectAll());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
		*/
	}

	private void createGridExecutionTable() {
		executionTTableModel = new ExecutionTTableModel();
		jExecutionTable = new JTable(executionTTableModel);
		jExecutionTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		jExecutionTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		jExecutionTable.getColumnModel().getColumn(2).setPreferredWidth(150);
		jExecutionTable.getColumnModel().getColumn(3).setPreferredWidth(150);
		jExecutionTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		jExecutionTable.getColumnModel().getColumn(5).setPreferredWidth(120);
		jExecutionTable.getColumnModel().getColumn(6).setPreferredWidth(100);
		jExecutionTable.getColumnModel().getColumn(7).setPreferredWidth(300);
		
		// Habilita a seleção por linha
		jExecutionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		new JScrollPane(jExecutionTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jExecutionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			
		try {
			executionTTableModel.addModelsList(tableExecutionDAO.selectAll());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		grid = new JScrollPane(jExecutionTable);
		setLayout(null);
		resizeGrid(grid, 5, 320, 770, 220);
		grid.setVisible(true);

		add(grid);
	}
	
	public void createComponents() {
		label = new JLabel("Log das direções: ");
		label.setBounds(5, 55, 120, 50);
		getContentPane().add(label);
		
		label = new JLabel("Log das Tabelas: ");
		label.setBounds(5, 280, 120, 50);
		getContentPane().add(label);
		
		createGridExecutionTable();
		createGridExecution();
	}
	
	private void setButtonsActions() {
		
	}
	
	private void registerKeyEvent() {
		
	}
}
