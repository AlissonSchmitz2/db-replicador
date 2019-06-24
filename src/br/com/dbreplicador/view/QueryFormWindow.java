package br.com.dbreplicador.view;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JDesktopPane;
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
	
	public QueryFormWindow(JDesktopPane desktop, Connection connection) {
		super("Controle de Alunos", 700, 450, desktop, false);
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

		// Habilita a seleção por linha
		jExecution.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		new JScrollPane(jExecution, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jExecution.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		grid = new JScrollPane(jExecution);
		setLayout(null);
		resizeGrid(grid, 5, 100, 670, 180);
		grid.setVisible(true);

		add(grid);
	
		try {
			executionTableModel.addModelsList(executionDAO.selectAll());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createGridExecutionTable() {
		executionTTableModel = new ExecutionTTableModel();
		jExecutionTable = new JTable(executionTTableModel);

		// Habilita a seleção por linha
		jExecutionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		new JScrollPane(jExecutionTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jExecutionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		grid = new JScrollPane(jExecutionTable);
		setLayout(null);
		resizeGrid(grid, 5, 300, 670, 100);
		grid.setVisible(true);

		add(grid);
	}
	
	public void createComponents() {
		createGridExecution();
		createGridExecutionTable();
	}
	
	private void setButtonsActions() {
		
	}
	
	private void registerKeyEvent() {
		
	}
}
