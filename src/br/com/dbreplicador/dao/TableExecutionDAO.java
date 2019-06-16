package br.com.dbreplicador.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.dbreplicador.model.TableExecutionModel;

public class TableExecutionDAO extends AbstractCrudDAO<TableExecutionModel>{
	private static final String TABLE_NAME = "tb_replicacao_tabela_execucao";

	private String columnId = "codigo_tabela";

	private String defaultOrderBy = "codigo_tabela ASC";

	private String[] defaultValuesToInsert = new String[] {
			"DEFAULT"
	};

	private String[] columnsToInsert = new String[] {
			"codigo_tabela",
			"data_atual",
			"processo",
			"database_origem",
			"usuario_origem",
			"database_destino",
			"usuario_destino",
			"execucao_inicio_data_hora",
			"ordem",
			"inicio_data_hora",
			"fim_data_hora",
			"data_atual_ate",
			"linhas_processadas",
			"sucesso",
			"mensagem"
	};
	
	private String[] columnsToUpdate = new String[] {
			"data_atual",
			"processo",
			"database_origem",
			"usuario_origem",
			"database_destino",
			"usuario_destino",
			"execucao_inicio_data_hora",
			"ordem",
			"inicio_data_hora",
			"fim_data_hora",
			"data_atual_ate",
			"linhas_processadas",
			"sucesso",
			"mensagem"
	};

	Connection connection;

	public TableExecutionDAO(Connection connection) throws SQLException {
		this.connection = connection;

		this.connection.setAutoCommit(false);
	}

	@Override
	public TableExecutionModel insert(TableExecutionModel model) throws SQLException {
		String query = getInsertQuery(TABLE_NAME, columnsToInsert, defaultValuesToInsert);

		PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		pst.clearParameters();

		setParam(pst, 1, model.getCurrentDate());
		setParam(pst, 2, model.getProcess());
		setParam(pst, 3, model.getOriginDatabase());
		setParam(pst, 4, model.getOriginUser());
		setParam(pst, 5, model.getDestinationDatabase());
		setParam(pst, 6, model.getDestinationUser());
		setParam(pst, 7, model.getExecutionStartDateTime());
		setParam(pst, 8, model.getOrder());
		setParam(pst, 9, model.getStartDateTime());
		setParam(pst, 10, model.getFinishDateTime());
		setParam(pst, 11, model.getCurrentDateUntil());
		setParam(pst, 12, model.getProcessedLines());
		setParam(pst, 13, model.isSucess());
		setParam(pst, 14, model.getMessage());

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				int lastInsertedCode = rs.getInt(columnId);
				
				// Antes de retornar, seta o id ao objeto modalidade
				model.setTableCode(lastInsertedCode);
				
				return model;
			}
		}

		return null;
	}

	@Override
	public boolean update(TableExecutionModel model) throws SQLException {
		String query = getUpdateQuery(TABLE_NAME, columnId, columnsToUpdate);

		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, model.getCurrentDate());
		setParam(pst, 2, model.getProcess());
		setParam(pst, 3, model.getOriginDatabase());
		setParam(pst, 4, model.getOriginUser());
		setParam(pst, 5, model.getDestinationDatabase());
		setParam(pst, 6, model.getDestinationUser());
		setParam(pst, 7, model.getExecutionStartDateTime());
		setParam(pst, 8, model.getOrder());
		setParam(pst, 9, model.getStartDateTime());
		setParam(pst, 10, model.getFinishDateTime());
		setParam(pst, 11, model.getCurrentDateUntil());
		setParam(pst, 12, model.getProcessedLines());
		setParam(pst, 13, model.isSucess());
		setParam(pst, 14, model.getMessage());

		// Identificador WHERE
		setParam(pst, 15, model.getTableCode());

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			return true;
		}

		return false;
	}

	@Override
	public boolean delete(TableExecutionModel model) throws SQLException {
		return deleteById(model.getTableCode());
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
	public List<TableExecutionModel> selectAll() throws SQLException {
		String query = getSelectAllQuery(TABLE_NAME, "*", defaultOrderBy);

		PreparedStatement pst = connection.prepareStatement(query);

		List<TableExecutionModel> replicationsTableExecutionList = new ArrayList<TableExecutionModel>();

		ResultSet rst = pst.executeQuery();

		while (rst.next()) {
			TableExecutionModel model = createModelFromResultSet(rst);

			replicationsTableExecutionList.add(model);
		}

		return replicationsTableExecutionList;
	}

	@Override
	public TableExecutionModel findById(Integer id) throws SQLException {
		TableExecutionModel model = null;

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
	 * @return TableExecutionModel
	 * @throws SQLException
	 */
	private TableExecutionModel createModelFromResultSet(ResultSet rst) throws SQLException {
		TableExecutionModel model = new TableExecutionModel();

		model.setTableCode(rst.getInt("codigo_tabela"));
		model.setCurrentDate(rst.getTimestamp("data_atual"));
		model.setProcess(rst.getString("processo"));
		model.setOriginDatabase(rst.getInt("database_origem"));
		model.setOriginUser(rst.getString("usuario_origem"));
		model.setDestinationDatabase(rst.getInt("database_destino"));
		model.setDestinationUser(rst.getString("usuario_destino"));
		model.setExecutionStartDateTime(rst.getTimestamp("execucao_inicio_data_hora"));
		model.setOrder(rst.getInt("ordem"));
		model.setStartDateTime(rst.getTimestamp("inicio_data_hora"));
		model.setFinishDateTime(rst.getTimestamp("fim_data_hora"));
		model.setCurrentDateUntil(rst.getTimestamp("data_atual_ate"));
		model.setProcessedLines(rst.getInt("linhas_processadas"));
		model.setSucess(rst.getBoolean("sucesso"));
		model.setMessage(rst.getString("mensagem"));

		return model;
	}

}
