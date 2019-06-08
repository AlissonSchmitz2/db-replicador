package br.com.dbreplicador.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.dbreplicador.dao.contracts.ISearchable;
import br.com.dbreplicador.model.ReplicationModel;

public class ReplicationDAO extends AbstractCrudDAO<ReplicationModel> implements ISearchable<ReplicationModel> {
	private static final String TABLE_NAME = "tb_replicacao";

	private String columnId = "codigo_replicacao";

	private String defaultOrderBy = "codigo_replicacao ASC";

	private String[] defaultValuesToInsert = new String[] {
			"DEFAULT"
	};

	private String[] columnsToInsert = new String[] {
			"codigo_replicacao",
			"data_atual",
			"usuario",
			"nome",
			"endereco",
			"porta",
			"database",
			"tipo_banco",
			"url"
	};
	
	private String[] columnsToUpdate = new String[] {
			"data_atual",
			"usuario",
			"nome",
			"endereco",
			"porta",
			"database",
			"tipo_banco",
			"url"
	};

	Connection connection;

	public ReplicationDAO(Connection connection) throws SQLException {
		this.connection = connection;

		this.connection.setAutoCommit(false);
	}

	@Override
	public ReplicationModel insert(ReplicationModel model) throws SQLException {
		String query = getInsertQuery(TABLE_NAME, columnsToInsert, defaultValuesToInsert);

		PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		pst.clearParameters();

		setParam(pst, 1, model.getCurrentDate());
		setParam(pst, 2, model.getUser());
		setParam(pst, 3, model.getName());
		setParam(pst, 4, model.getAddress());
		setParam(pst, 5, model.getDoor());
		setParam(pst, 6, model.getDatabase());
		setParam(pst, 7, model.getDatebaseType());
		setParam(pst, 8, model.getUrl());

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
	public boolean update(ReplicationModel model) throws SQLException {
		String query = getUpdateQuery(TABLE_NAME, columnId, columnsToUpdate);

		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, model.getCurrentDate());
		setParam(pst, 2, model.getUser());
		setParam(pst, 3, model.getName());
		setParam(pst, 4, model.getAddress());
		setParam(pst, 5, model.getDoor());
		setParam(pst, 6, model.getDatabase());
		setParam(pst, 7, model.getDatebaseType());
		setParam(pst, 8, model.getUrl());

		// Identificador WHERE
		setParam(pst, 9, model.getReplicationCode());

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			return true;
		}

		return false;
	}

	@Override
	public boolean delete(ReplicationModel model) throws SQLException {
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
	public ReplicationModel findById(Integer id) throws SQLException {
		ReplicationModel model = null;

		String query = getFindByQuery(TABLE_NAME, columnId, "*", defaultOrderBy);
		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, id);
		ResultSet rst = pst.executeQuery();

		if (rst.next()) {
			model = createModelFromResultSet(rst);
		}

		return model;
	}
	
	@Override
	public List<ReplicationModel> selectAll() throws SQLException {
		String query = getSelectAllQuery(TABLE_NAME, "*", defaultOrderBy);

		PreparedStatement pst = connection.prepareStatement(query);

		List<ReplicationModel> replicationsList = new ArrayList<ReplicationModel>();

		ResultSet rst = pst.executeQuery();

		while (rst.next()) {
			ReplicationModel model = createModelFromResultSet(rst);

			replicationsList.add(model);
		}

		return replicationsList;
	}
	
	/**
	 * Cria um objeto Model a partir do resultado obtido no banco de dados
	 * 
	 * @param rst
	 * @return ReplicationModel
	 * @throws SQLException
	 */
	private ReplicationModel createModelFromResultSet(ResultSet rst) throws SQLException {
		ReplicationModel model = new ReplicationModel();

		model.setReplicationCode(rst.getInt("codigo_replicacao"));
		model.setCurrentDate(rst.getTimestamp("data_atual"));
		model.setUser(rst.getString("usuario"));
		model.setName(rst.getString("nome"));
		model.setAddress(rst.getString("endereco"));
		model.setDoor(rst.getInt("porta"));
		model.setDatabase(rst.getString("database"));
		model.setDatebaseType(rst.getString("tipo_banco"));
		model.setUrl(rst.getString("url"));

		return model;
	}

	@Override
	public List<ReplicationModel> search(String word) throws SQLException {
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE nome ILIKE ? OR database ILIKE ? ORDER BY " + defaultOrderBy;
		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, "%" + word + "%");
		setParam(pst, 2, "%" + word + "%");

		List<ReplicationModel> replicationList = new ArrayList<ReplicationModel>();

		ResultSet rst = pst.executeQuery();

		while (rst.next()) {
			ReplicationModel model = createModelFromResultSet(rst);

			replicationList.add(model);
		}

		return replicationList;
	}
	
}
