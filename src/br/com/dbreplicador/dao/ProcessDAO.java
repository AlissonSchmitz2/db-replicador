package br.com.dbreplicador.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.dbreplicador.dao.contracts.Searchable;
import br.com.dbreplicador.model.ProcessModel;

public class ProcessDAO extends AbstractCrudDAO<ProcessModel> implements Searchable<ProcessModel> {
	private static final String TABLE_NAME = "tb_replicacao_processo";

	private String columnId = "codigo_processo";

	private String defaultOrderBy = "codigo_processo ASC";

	private String[] defaultValuesToInsert = new String[] { "DEFAULT" };

	private String[] columnsToInsert = new String[] { "codigo_processo", "data_atual", "usuario", "processo",
			"descricao", "data_atual_de", "erro_ignorar", "habilitado" };

	private String[] columnsToUpdate = new String[] { "data_atual", "usuario", "processo", "descricao", "data_atual_de",
			"erro_ignorar", "habilitado" };

	Connection connection;

	public ProcessDAO(Connection connection) throws SQLException {
		this.connection = connection;

		this.connection.setAutoCommit(false);
	}

	@Override
	public ProcessModel insert(ProcessModel model) throws SQLException {
		String query = getInsertQuery(TABLE_NAME, columnsToInsert, defaultValuesToInsert);

		PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		pst.clearParameters();

		setParam(pst, 1, model.getCurrentDate());
		setParam(pst, 2, model.getUser());
		setParam(pst, 3, model.getProcess());
		setParam(pst, 4, model.getDescription());
		setParam(pst, 5, model.getCurrentDateOf());
		setParam(pst, 6, model.isErrorIgnore());
		setParam(pst, 7, model.isEnable());

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				int lastInsertedCode = rs.getInt(columnId);

				// Antes de retornar, seta o id ao objeto modalidade
				model.setProcessCode(lastInsertedCode);

				return model;
			}
		}

		return null;
	}

	@Override
	public boolean update(ProcessModel model) throws SQLException {
		String query = getUpdateQuery(TABLE_NAME, columnId, columnsToUpdate);

		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, model.getCurrentDate());
		setParam(pst, 2, model.getUser());
		setParam(pst, 3, model.getProcess());
		setParam(pst, 4, model.getDescription());
		setParam(pst, 5, model.getCurrentDateOf());
		setParam(pst, 6, model.isErrorIgnore());
		setParam(pst, 7, model.isEnable());

		// Identificador WHERE
		setParam(pst, 8, model.getProcessCode());

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			return true;
		}

		return false;
	}

	@Override
	public boolean delete(ProcessModel model) throws SQLException {
		return deleteById(model.getProcessCode());
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
	public List<ProcessModel> selectAll() throws SQLException {
		String query = getSelectAllQuery(TABLE_NAME, "*", defaultOrderBy);

		PreparedStatement pst = connection.prepareStatement(query);

		List<ProcessModel> replicationsProcessList = new ArrayList<ProcessModel>();

		ResultSet rst = pst.executeQuery();

		while (rst.next()) {
			ProcessModel model = createModelFromResultSet(rst);

			replicationsProcessList.add(model);
		}

		return replicationsProcessList;
	}

	@Override
	public ProcessModel findById(Integer id) throws SQLException {
		ProcessModel model = null;

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
	 * @return ProcessModel
	 * @throws SQLException
	 */
	private ProcessModel createModelFromResultSet(ResultSet rst) throws SQLException {
		ProcessModel model = new ProcessModel();

		model.setProcessCode(rst.getInt("codigo_processo"));
		model.setCurrentDate(rst.getTimestamp("data_atual"));
		model.setUser(rst.getString("usuario"));
		model.setProcess(rst.getString("processo"));
		model.setDescription(rst.getString("descricao"));
		model.setCurrentDateOf(rst.getTimestamp("data_atual_de"));
		model.setErrorIgnore(rst.getBoolean("erro_ignorar"));
		model.setEnable(rst.getBoolean("habilitado"));

		return model;
	}

	@Override
	public List<ProcessModel> search(String word) throws SQLException {
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE processo ILIKE ? ORDER BY " + defaultOrderBy;
		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, "%" + word + "%");

		List<ProcessModel> processList = new ArrayList<ProcessModel>();

		ResultSet rst = pst.executeQuery();

		while (rst.next()) {
			ProcessModel model = createModelFromResultSet(rst);

			processList.add(model);
		}

		return processList;
	}

}
