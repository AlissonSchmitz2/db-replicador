package br.com.dbreplicador.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.dbreplicador.dao.contracts.ISearchable;
import br.com.dbreplicador.model.ConnectionModel;
import br.com.dbreplicador.model.DirectionModel;
import br.com.dbreplicador.model.ProcessModel;

public class DirectionDAO extends AbstractCrudDAO<DirectionModel> implements ISearchable<DirectionModel>{
	private static final String TABLE_NAME = "tb_replicacao_direcao";

	private String columnId = "codigo_direcao";

	private String defaultOrderBy = "codigo_direcao ASC";

	private String[] defaultValuesToInsert = new String[] {
			"DEFAULT"
	};

	private String[] columnsToInsert = new String[] {
			"codigo_direcao",
			"data_atual",
			"usuario",
			"processo",
			"database_origem",
			"usuario_origem",
			"senha_origem",
			"database_destino",
			"usuario_destino",
			"senha_destino",
			"automatico_manual",
			"periodo_ano",
			"periodo_mes",
			"periodo_dia",
			"periodo_hora",
			"periodo_minuto",
			"periodo_segundo",
			"executar_dia_de",
			"executar_dia_ate",
			"executar_hora_de",
			"executar_hora_ate",
			"duracao_maximo",
			"execucao_ultima",
			"retencao",					
			"habilitado"
	};
		
	private String[] columnsToUpdate = new String[] {
			"data_atual",
			"usuario",
			"processo",
			"database_origem",
			"usuario_origem",
			"senha_origem",
			"database_destino",
			"usuario_destino",
			"senha_destino",
			"automatico_manual",
			"periodo_ano",
			"periodo_mes",
			"periodo_dia",
			"periodo_hora",
			"periodo_minuto",
			"periodo_segundo",
			"executar_dia_de",
			"executar_dia_ate",
			"executar_hora_de",
			"executar_hora_ate",
			"duracao_maximo",
			"execucao_ultima",
			"retencao",					
			"habilitado"
	};

	Connection connection;

	public DirectionDAO(Connection connection) throws SQLException {
		this.connection = connection;

		this.connection.setAutoCommit(false);
	}

	@Override
	public DirectionModel insert(DirectionModel model) throws SQLException {
		String query = getInsertQuery(TABLE_NAME, columnsToInsert, defaultValuesToInsert);
		PreparedStatement pst = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS); 
		
		pst.clearParameters();
		
		setParam(pst, 1, model.getCurrentDate());
		setParam(pst, 2, model.getUser());
		setParam(pst, 3, model.getProcess());
		setParam(pst, 4, model.getOriginDatabase());
		setParam(pst, 5, model.getOriginUser());
		setParam(pst, 6, model.getOriginPassword());
		setParam(pst, 7, model.getDestinationDatabase());
		setParam(pst, 8, model.getDestinationUser());
		setParam(pst, 9, model.getDestinationPassword());
		setParam(pst, 10, model.isAutomaticManual());
		setParam(pst, 11, model.getYearPeriod());
		setParam(pst, 12, model.getMounthPeriod());
		setParam(pst, 13, model.getDayPeriod());
		setParam(pst, 14, model.getHourPeriod());
		setParam(pst, 15, model.getMinutePeriod());
		setParam(pst, 16, model.getSecondPeriod());
		setParam(pst, 17, model.getExecuteDayOf());
		setParam(pst, 18, model.getExecuteDayTo());
		setParam(pst, 19, model.getExecuteHourOf());
		setParam(pst, 20, model.getExecuteHourTo());
		setParam(pst, 21, model.getMaxDuration());
		setParam(pst, 22, model.getLastExecution());
		setParam(pst, 23, model.getRetention());
		setParam(pst, 24, model.isEnabled());
		
		int result = pst.executeUpdate();
		
		if(result > 0) {
			connection.commit();
			
			ResultSet rst = pst.getGeneratedKeys();
			if(rst.next()) {
				int lastInsertedCode = rst.getInt(columnId);
				
				// Antes de retornar, seta o id ao objeto modalidade
				model.setDirectionCode(lastInsertedCode);
				
				return model;
			}
		}
		
		return null;
	}

	@Override
	public boolean update(DirectionModel model) throws SQLException {
		String query = getUpdateQuery(TABLE_NAME, columnId, columnsToUpdate);
		
		PreparedStatement pst = connection.prepareStatement(query);
		
		setParam(pst, 1, model.getCurrentDate());
		setParam(pst, 2, model.getUser());
		setParam(pst, 3, model.getProcess());
		setParam(pst, 4, model.getOriginDatabase());
		setParam(pst, 5, model.getOriginUser());
		setParam(pst, 6, model.getOriginPassword());
		setParam(pst, 7, model.getDestinationDatabase());
		setParam(pst, 8, model.getDestinationUser());
		setParam(pst, 9, model.getDestinationPassword());
		setParam(pst, 10, model.isAutomaticManual());
		setParam(pst, 11, model.getYearPeriod());
		setParam(pst, 12, model.getMounthPeriod());
		setParam(pst, 13, model.getDayPeriod());
		setParam(pst, 14, model.getHourPeriod());
		setParam(pst, 15, model.getMinutePeriod());
		setParam(pst, 16, model.getSecondPeriod());
		setParam(pst, 17, model.getExecuteDayOf());
		setParam(pst, 18, model.getExecuteDayTo());
		setParam(pst, 19, model.getExecuteHourOf());
		setParam(pst, 20, model.getExecuteHourTo());
		setParam(pst, 21, model.getMaxDuration());
		setParam(pst, 22, model.getLastExecution());
		setParam(pst, 23, model.getRetention());
		setParam(pst, 24, model.isEnabled());
		
		//Identificador do Where
		setParam(pst, 25, model.getDirectionCode());
		
		int result = pst.executeUpdate();
		if(result > 0) {
			connection.commit();
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean delete(DirectionModel model) throws SQLException {
		return deleteById(model.getDirectionCode());
	}

	@Override
	public boolean deleteById(Integer id) throws SQLException {
		String query = getDeleteQuery(TABLE_NAME, columnId);
		PreparedStatement pst = connection.prepareStatement(query);
		
		setParam(pst, 1, id);
		
		int result = pst.executeUpdate();
		if(result > 0) {
			connection.commit();
			return true;
		}
		return false;
	}

	@Override
	public List<DirectionModel> selectAll() throws SQLException {
		String query = getSelectAllQuery(TABLE_NAME, "*" , columnId);
		
		PreparedStatement pst = connection.prepareStatement(query);
		
		List<DirectionModel> listModel = new ArrayList<DirectionModel>();
		
		ResultSet rst = pst.executeQuery();
		
		while(rst.next()) {
			DirectionModel model = createModelFromResultSet(rst);
			
			listModel.add(model);
		}
		
		return listModel;
	}

	@Override
	public DirectionModel findById(Integer id) throws SQLException {
		String query = getFindByQuery(TABLE_NAME, columnId, "*", defaultOrderBy);
		
		PreparedStatement pst = connection.prepareStatement(query);
		setParam(pst, 1, id);
		
		ResultSet rst = pst.executeQuery();
		
		DirectionModel model = null;
		
		if(rst.next()) {
			model = createModelFromResultSet(rst);
		}
		
		return model;
	}
	
	public Map<Integer, DirectionModel> getProcessesToReplication() throws SQLException {
		TableDAO tableDao = new TableDAO(connection);
		
		String query = "SELECT d.*,\n" + 
				"       p.codigo_processo AS p_codigo_processo,\n" + 
				"       p.descricao AS p_descricao,\n" + 
				"       p.data_atual_de AS p_data_atual_de,\n" + 
				"       p.erro_ignorar AS p_error_ignorar,\n" + 
				"\n" + 
				"       oc.endereco AS oc_endereco,\n" + 
				"       oc.porta AS oc_porta,\n" + 
				"       oc.database AS oc_database,\n" + 
				"       oc.tipo_banco AS oc_tipo_banco,\n" + 
				"\n" + 
				"       dc.endereco AS dc_endereco,\n" + 
				"       dc.porta AS dc_porta,\n" + 
				"       dc.database AS dc_database,\n" + 
				"       dc.tipo_banco AS dc_tipo_banco\n" + 
				"\n" + 
				"FROM tb_replicacao_direcao AS d\n" + 
				"    INNER JOIN tb_replicacao_processo AS p ON p.processo=d.processo\n" + 
				"    INNER JOIN tb_replicacao AS oc ON oc.codigo_replicacao=d.database_origem\n" + 
				"    INNER JOIN tb_replicacao AS dc ON dc.codigo_replicacao=d.database_destino\n" + 
				"WHERE p.habilitado=true AND d.habilitado=true;";
		
		PreparedStatement pst = connection.prepareStatement(query);
		
		Map<Integer, DirectionModel> directions = new HashMap<Integer, DirectionModel>();
	
		ResultSet directionsRst = pst.executeQuery();
	
		while (directionsRst.next()) {
			//DIREÇÕES
			DirectionModel direction = createModelFromResultSet(directionsRst);
			
			//PROCESSO
			ProcessModel process = new ProcessModel();
			process.setProcessCode(directionsRst.getInt("p_codigo_processo"));
			process.setDescription(directionsRst.getString("p_descricao"));
			process.setCurrentDateOf(directionsRst.getTimestamp("p_data_atual_de"));
			process.setErrorIgnore(directionsRst.getBoolean("p_error_ignorar"));
			//Processo -> Direção
			direction.setProcessModel(process);
			
			//CONEXÕES
			ConnectionModel originConnection = new ConnectionModel();
			originConnection.setDatebaseType(directionsRst.getString("oc_tipo_banco"));
			originConnection.setAddress(directionsRst.getString("oc_endereco"));
			originConnection.setDatabase(directionsRst.getString("oc_database"));
			originConnection.setPort(directionsRst.getInt("oc_porta"));
			//Conexão Origem -> Direção
			direction.setOriginConnectionModel(originConnection);
			
			ConnectionModel destinationConnection = new ConnectionModel();
			destinationConnection.setDatebaseType(directionsRst.getString("dc_tipo_banco"));
			destinationConnection.setAddress(directionsRst.getString("dc_endereco"));
			destinationConnection.setDatabase(directionsRst.getString("dc_database"));
			destinationConnection.setPort(directionsRst.getInt("dc_porta"));
			//Conexão Destino -> Direção
			direction.setDestinationConnectionModel(destinationConnection);
			
			//TABELAS
			direction.setTables(tableDao.getTablesToReplication(directionsRst.getString("processo")));
			
			//Adiciona a lista
			directions.put(directions.size() + 1, direction);
		}

		return directions;
	}
	
	/**
	 * Cria um objeto Model a partir do resultado obtido no banco de dados
	 * 
	 * @param rst
	 * @return ExecutionModel
	 * @throws SQLException
	 */
	private DirectionModel createModelFromResultSet(ResultSet rst) throws SQLException {
		DirectionModel model = new DirectionModel();

		model.setDirectionCode(rst.getInt("codigo_direcao"));
		model.setCurrentDate(rst.getTimestamp("data_atual"));
		model.setUser(rst.getString("usuario"));
		model.setProcess(rst.getString("processo"));
		model.setOriginDatabase(rst.getInt("database_origem"));
		model.setOriginUser(rst.getString("usuario_origem"));
		model.setOriginPassword(rst.getString("senha_origem"));
		model.setDestinationDatabase(rst.getInt("database_destino"));
		model.setDestinationUser(rst.getString("usuario_destino"));
		model.setDestinationPassword(rst.getString("senha_destino"));
		model.setAutomaticManual(rst.getBoolean("automatico_manual"));
		model.setYearPeriod(rst.getInt("periodo_ano"));
		model.setMonthPeriod(rst.getInt("periodo_mes"));
		model.setDayPeriod(rst.getInt("periodo_dia"));
		model.setHourPeriod(rst.getInt("periodo_hora"));
		model.setMinutePeriod(rst.getInt("periodo_minuto"));
		model.setSecondPeriod(rst.getInt("periodo_segundo"));
		model.setExecuteDayOf(rst.getInt("executar_dia_de"));
		model.setExecuteDayTo(rst.getInt("executar_dia_ate"));
		model.setExecuteHourOf(rst.getInt("executar_hora_de"));
		model.setExecuteHourTo(rst.getInt("executar_hora_ate"));
		model.setMaxDuration(rst.getInt("duracao_maximo"));
		model.setLastExecution(rst.getDate("execucao_ultima"));
		model.setRetention(rst.getInt("retencao"));
		model.setEnabled(rst.getBoolean("habilitado"));
				
		return model;
	}
	
	@Override
	public List<DirectionModel> search(String word) throws SQLException {
		String query = "SELECT d.*, ro.database AS ro_database_origem, rd.database AS rd_database_destino FROM " + TABLE_NAME + " AS d "
				+ "LEFT JOIN tb_replicacao AS ro ON d.database_origem = ro.codigo_replicacao "
				+ "LEFT JOIN tb_replicacao AS rd ON d.database_destino = rd.codigo_replicacao "
				+ "WHERE d.processo ILIKE ? ORDER BY " + defaultOrderBy;
		
		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, "%" + word + "%");

		List<DirectionModel> directionList = new ArrayList<DirectionModel>();

		ResultSet rst = pst.executeQuery();

		while (rst.next()) {
			DirectionModel model = createModelFromResultSet(rst);
			
			// Recupera o nome do database origem. (Por enquanto não há a necessidade de recuperar e montar o objeto completo).
			ConnectionModel connectionModel_DBOrigin = new ConnectionModel();
			connectionModel_DBOrigin.setDatabase(rst.getString("ro_database_origem"));
			model.setOriginConnectionModel(connectionModel_DBOrigin);
			
			// Recupera o nome do database destino. (Por enquanto não há a necessidade de recuperar e montar o objeto completo).
			ConnectionModel connectionModel_DBDestiny = new ConnectionModel();
			connectionModel_DBDestiny.setDatabase(rst.getString("rd_database_destino"));
			model.setDestinationConnectionModel(connectionModel_DBDestiny);

			directionList.add(model);
		}

		return directionList;
	}
}
