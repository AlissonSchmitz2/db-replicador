package br.com.dbreplicador.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.dbreplicador.dao.contracts.ISearchable;
import br.com.dbreplicador.model.TableModel;

public class TableDAO extends AbstractCrudDAO<TableModel> implements ISearchable<TableModel>{
	private static final String TABLE_NAME = "tb_replicacao_tabela";

	private String columnId = "codigo_replicacao";

	private String defaultOrderBy = "codigo_replicacao ASC";

	private String[] defaultValuesToInsert = new String[] {
			"DEFAULT"
	};

	private String[] columnsToInsert = new String[] {
			"codigo_replicacao",
			"data_atual",
			"usuario",
			"processo",
			"ordem",
			"tabela_origem",
			"tabela_destino",
			"coluna_tipo",
			"coluna_chave",
			"backup_incremental",
			"linhas_maximo",
			"erro_ignorar",
			"habilitado",
			"coluna_controle"
	};
	
	private String[] columnsToUpdate = new String[] {
			"data_atual",
			"usuario",
			"processo",
			"ordem",
			"tabela_origem",
			"tabela_destino",
			"coluna_tipo",
			"coluna_chave",
			"backup_incremental",
			"linhas_maximo",
			"erro_ignorar",
			"habilitado",
			"coluna_controle"
	};

	Connection connection;

	public TableDAO(Connection connection) throws SQLException {
		this.connection = connection;

		this.connection.setAutoCommit(false);
	}

	@Override
	public TableModel insert(TableModel model) throws SQLException {
		String query = getInsertQuery(TABLE_NAME, columnsToInsert, defaultValuesToInsert);

		PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		pst.clearParameters();

		setParam(pst, 1, model.getCurrentDate());
		setParam(pst, 2, model.getUser());
		setParam(pst, 3, model.getProcess());
		setParam(pst, 4, model.getOrder());
		setParam(pst, 5, model.getOriginTable());
		setParam(pst, 6, model.getDestinationTable());
		setParam(pst, 7, model.getTypeColumn());
		setParam(pst, 8, model.getKeyColumn());
		setParam(pst, 9, model.isIncrementalBackup());
		setParam(pst, 10, model.getMaximumLines());
		setParam(pst, 11, model.isErrorIgnore());
		setParam(pst, 12, model.isEnable());
		setParam(pst, 13, model.getControlColumn());

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				int lastInsertedCode = rs.getInt(columnId);
				
				// Antes de retornar, seta o id ao objeto modalidade
				model.setReplicationCode(lastInsertedCode);
				
				return model;
			}
		}

		return null;
	}

	@Override
	public boolean update(TableModel model) throws SQLException {
		String query = getUpdateQuery(TABLE_NAME, columnId, columnsToUpdate);

		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, model.getCurrentDate());
		setParam(pst, 2, model.getUser());
		setParam(pst, 3, model.getProcess());
		setParam(pst, 4, model.getOrder());
		setParam(pst, 5, model.getOriginTable());
		setParam(pst, 6, model.getDestinationTable());
		setParam(pst, 7, model.getTypeColumn());
		setParam(pst, 8, model.getKeyColumn());
		setParam(pst, 9, model.isIncrementalBackup());
		setParam(pst, 10, model.getMaximumLines());
		setParam(pst, 11, model.isErrorIgnore());
		setParam(pst, 12, model.isEnable());
		setParam(pst, 13, model.getControlColumn());
		
		// Identificador WHERE
		setParam(pst, 14, model.getReplicationCode());

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			return true;
		}

		return false;
	}

	@Override
	public boolean delete(TableModel model) throws SQLException {
		return deleteById(model.getReplicationCode());
	}

	@Override
	public boolean deleteById(Integer id) throws SQLException {
		String query = getDeleteQuery(TABLE_NAME, columnId);
		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, id);

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			return true;
		}

		return false;
	}

	@Override
	public List<TableModel> selectAll() throws SQLException {
		String query = getSelectAllQuery(TABLE_NAME, "*", defaultOrderBy);

		PreparedStatement pst = connection.prepareStatement(query);

		List<TableModel> replicationsTableList = new ArrayList<TableModel>();

		ResultSet rst = pst.executeQuery();

		while (rst.next()) {
			TableModel model = createModelFromResultSet(rst);

			replicationsTableList.add(model);
		}

		return replicationsTableList;
	}

	@Override
	public TableModel findById(Integer id) throws SQLException {
		TableModel model = null;

		String query = getFindByQuery(TABLE_NAME, columnId, "*", defaultOrderBy);
		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, id);
		ResultSet rst = pst.executeQuery();

		if (rst.next()) {
			model = createModelFromResultSet(rst);
		}

		return model;
	}

	/**
	 * Cria um objeto Model a partir do resultado obtido no banco de dados
	 * 
	 * @param rst
	 * @return TableModel
	 * @throws SQLException
	 */
	private TableModel createModelFromResultSet(ResultSet rst) throws SQLException {
		TableModel model = new TableModel();

		model.setReplicationCode(rst.getInt("codigo_replicacao"));
		model.setCurrentDate(rst.getTimestamp("data_atual"));
		model.setUser(rst.getString("usuario"));
		model.setProcess(rst.getString("processo"));
		model.setOrder(rst.getInt("ordem"));
		model.setOriginTable(rst.getString("tabela_origem"));
		model.setDestinationTable(rst.getString("tabela_destino"));
		model.setTypeColumn(rst.getString("coluna_tipo"));
		model.setKeyColumn(rst.getString("coluna_chave"));
		model.setControlColumn(rst.getString("coluna_chave"));
		model.setIncrementalBackup(rst.getBoolean("backup_incremental"));
		model.setMaximumLines(rst.getInt("linhas_maximo"));
		model.setErrorIgnore(rst.getBoolean("erro_ignorar"));
		model.setEnable(rst.getBoolean("habilitado"));
		model.setControlColumn(rst.getString("coluna_controle"));

		return model;
	}
	
	@Override
	public List<TableModel> search(String word) throws SQLException {
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE processo ILIKE ? ORDER BY " + defaultOrderBy;
		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, "%" + word + "%");

		List<TableModel> tablesList = new ArrayList<TableModel>();

		ResultSet rst = pst.executeQuery();

		while (rst.next()) {
			TableModel model = createModelFromResultSet(rst);

			tablesList.add(model);
		}

		return tablesList;
	}
	
}
