package br.com.dbreplicador.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import br.com.dbreplicador.model.ExecutionModel;

public class ExecutionDAO extends AbstractCrudDAO<ExecutionModel>{
	private static final String TABLE_NAME = "tb_replicacao_execucao";

	private String columnId = "codigo_execucao";

	private String defaultOrderBy = "codigo_execucao ASC";

	private String[] defaultValuesToInsert = new String[] {
			"DEFAULT",
			"'" + new Timestamp(System.currentTimeMillis()).toString() + "'",
			"'not-applicable'"
	};

	private String[] columnsToInserts = new String[] {
			"codigo_execucao",
			"data_atual",
			"usuario",
			"database_origem",
			"usuario_origem",
			"database_destino",
			"usuario_destino",
			"inicio_data_hora",
			"fim_data_hora",
			"data_atual_ate",
			"ocorrencia",
			"processo"
	};
	
	private String[] columnsToUpdate = new String[] {
			"data_atual",
			"usuario",
			"database_origem",
			"usuario_origem",
			"database_destino",
			"usuario_destino",
			"inicio_data_hora",
			"fim_data_hora",
			"data_atual_ate",
			"ocorrencia",
			"processo"
	};

	Connection connection;

	public ExecutionDAO(Connection connection) throws SQLException {
		this.connection = connection;

		this.connection.setAutoCommit(false);
	}

	
	@Override
	public ExecutionModel insert(ExecutionModel model) throws SQLException {
		String query = getInsertQuery(TABLE_NAME, columnsToInserts, defaultValuesToInsert);

		PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		pst.clearParameters();
		
		//setParam(pst, 1, model.getCurrentDate());
		//setParam(pst, 2, model.getUser());
		setParam(pst, 1, model.getOriginDatabase());
		setParam(pst, 2, model.getOriginUser());
		setParam(pst, 3, model.getDestinatioDatabase());
		setParam(pst, 4, model.getDestinationUser());
		setParam(pst, 5, model.getDateHourInitial());
		setParam(pst, 6, model.getDateHourFinal());
		setParam(pst, 7, model.getCurrentDate());
		setParam(pst, 8, model.getOccurrence());
		setParam(pst, 9, model.getProcess());
			
		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				int lastInsertedCode = rs.getInt(columnId);
				
				// Antes de retornar, seta o id ao objeto
				model.setExecuteCode(lastInsertedCode);
				
				return model;
			}
		}

		return null;
	}

	@Override
	public boolean update(ExecutionModel model) throws SQLException {
		String query = getUpdateQuery(TABLE_NAME, columnId, columnsToUpdate);

		PreparedStatement pst = connection.prepareStatement(query);
		
		setParam(pst, 1, model.getCurrentDate());
		setParam(pst, 2, model.getUser());
		setParam(pst, 3, model.getOriginDatabase());
		setParam(pst, 4, model.getOriginUser());
		setParam(pst, 5, model.getDestinatioDatabase());
		setParam(pst, 6, model.getDestinationUser());
		setParam(pst, 7, model.getDateHourInitial());
		setParam(pst, 8, model.getDateHourFinal());
		setParam(pst, 9, model.getCurrentDate());
		setParam(pst, 10, model.getOccurrence());
		setParam(pst, 11, model.getProcess());

		//Identificador Where
		setParam(pst,12,model.getExecuteCode());
		
		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			return true;
		}

		return false;
	}

	@Override
	public boolean delete(ExecutionModel model) throws SQLException {
		return deleteById(model.getExecuteCode());
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
	public List<ExecutionModel> selectAll() throws SQLException {
		String query = getSelectAllQuery(TABLE_NAME, "*", defaultOrderBy);

		PreparedStatement pst = connection.prepareStatement(query);

		List<ExecutionModel> replicationsList = new ArrayList<ExecutionModel>();

		ResultSet rst = pst.executeQuery();

		while (rst.next()) {
			ExecutionModel model = createModelFromResultSet(rst);

			replicationsList.add(model);
		}

		return replicationsList;
	}

	@Override
	public ExecutionModel findById(Integer id) throws SQLException {
		ExecutionModel model = null;

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
	 * @return ExecutionModel
	 * @throws SQLException
	 */
	private ExecutionModel createModelFromResultSet(ResultSet rst) throws SQLException {
		ExecutionModel model = new ExecutionModel();

		model.setCurrentDate(rst.getTimestamp("data_atual"));
		model.setCurrenteDateTo(rst.getTimestamp("data_atual_ate"));
		model.setDateHourFinal(rst.getTimestamp("fim_data_hora"));
		model.setDateHourInitial(rst.getTimestamp("inicio_data_hora"));
		model.setDestinatioDatabase(rst.getInt("database_destino"));
		model.setDestinationUser(rst.getString("usuario_destino"));
		model.setExecuteCode(rst.getInt("codigo_execucao"));
		model.setOccurrence(rst.getString("ocorrencia"));
		model.setOriginDatabase(rst.getInt("database_origem"));
		model.setOriginUser(rst.getString("usuario_origem"));
		model.setUser(rst.getString("usuario"));
		
		return model;
	}


}
