----------------------------------------------
-- TB_REPLICACAO
--
-- Tabela que armazenar� os dados de cada conexão.
--
-- nome		 :	Nome da Conex�o
-- endere�o  :	Endere�o IP de conexão com o banco de dados
-- porta	 :	Porta de conexão com o banco de dados
-- databse   :	SID de conexão com o banco de dados
-- tipo_banco:	Banco que ser� trabalhado: Postgres, Oracle, Firebird
-- url		 :	url j� montada de acordo com o tipo_banco
----------------------------------------------

CREATE TABLE IF NOT EXISTS	tb_replicacao
	(
		codigo_replicacao	serial CONSTRAINT tb_replicacao_pk PRIMARY KEY,
		data_atual			timestamp NOT NULL DEFAULT localtimestamp,
		usuario            	text CONSTRAINT tb_replicacao_2 NOT NULL,
		nome				text NOT NULL,
		endereco			text NOT NULL,
		porta				integer NOT NULL,
		database			text NOT NULL,
		tipo_banco			text NOT NULL,
		url					text NOT NULL
	)
;

----------------------------------------------
-- TB_REPLICACAO_PROCESSO
--
-- Tabela que armazenará os processos da replicacao
--
----------------------------------------------
CREATE TABLE  IF NOT EXISTS tb_replicacao_processo
	(
		codigo_processo		serial,
							UNIQUE (codigo_processo),
		data_atual			timestamp NOT NULL DEFAULT localtimestamp,
		usuario            	text CONSTRAINT tb_replicacao_processo_2 NOT NULL,
		processo			text CONSTRAINT tb_replicacao_processo_pk PRIMARY KEY NOT NULL,
		descricao			text NOT NULL,
		Data_Atual_De		timestamp,
		erro_ignorar		boolean,
		habilitado			boolean
	)
;

----------------------------------------------
-- TB_REPLICACAO_TABELA
--
-- Tabela que conterá a ordem das TABELAS a serem replicadas
----------------------------------------------

CREATE TABLE IF NOT EXISTS	tb_replicacao_tabela
	(
		codigo_replicacao	serial,
		data_atual			timestamp NOT NULL DEFAULT localtimestamp,
		usuario            	text CONSTRAINT tb_replicacao_2 NOT NULL,
		processo			text NOT NULL,
							CONSTRAINT tb_replicacao_tabela_f2 FOREIGN KEY(processo)
							REFERENCES tb_replicacao_processo(processo) DEFERRABLE,
		ordem				Integer NOT NULL,
							CONSTRAINT tb_replicacao_tabela_pk PRIMARY KEY (processo, ordem),
		tabela_origem		text NOT NULL,
		tabela_destino		text NOT NULL,
		coluna_tipo			text NOT NULL,
		coluna_chave		text NOT NULL,
		operacao			boolean,
		linhas_maximo		integer NOT NULL,
		erro_ignorar		boolean,
		habilitado			boolean
	)
;

----------------------------------------------
-- TB_REPLICACAO_DIRECAO
--
-- Tabela que conterá o cruzamento das conexão ORIGEM x DESTINO
--
-- codigo_direcao	:	Codigo unico para cada direção
-- database_origem	:	Endereço de conexão da ORIGEM
-- usuario_origem	:	Usuário do banco de dados da ORIGEM
-- senha_origem		:	Senha do banco de dados da ORIGEM
-- database_destino	:	Endereço de conexão do DESTINO
-- usuario_destino	:	Usuário do banco de dados do DESTINO
-- senha_destino	:	Senha do banco de dados do DESTINO
-- tempo		:	Tempo em minutos para realizar a replicação de forma automática
-- habilitado		:	Habilita/Desabilita a replicação ORIGEM x DESTINO
----------------------------------------------

CREATE TABLE IF NOT EXISTS	tb_replicacao_direcao
	(
		codigo_direcao	serial CONSTRAINT tb_replicacao_direcao_pk PRIMARY KEY,
		data_atual		timestamp NOT NULL DEFAULT localtimestamp,
		usuario         text CONSTRAINT tb_replicacao_2 NOT NULL,
		processo		text NOT NULL,
		database_origem	text NOT NULL,
						UNIQUE (database_origem),
		usuario_origem	text NOT NULL,
		senha_origem	text NOT NULL,
		database_destino text NOT NULL,
						UNIQUE (database_destino),
		usuario_destino	text NOT NULL,
		senha_destino	text NOT NULL,
		automatico_manual boolean,
		periodo_ano		numeric(2),
		periodo_mes		numeric(2),
		periodo_dia		numeric(3),
		periodo_hora	numeric(3),
		periodo_minuto	numeric(4),
		periodo_segundo	numeric(4),
						constraint tb_replicacao_direcao_451
										check
										(
											(
												periodo_ano		is null
												and
												periodo_mes		is null
												and
												periodo_dia		is null
												and
												periodo_hora		is null
												and
												periodo_minuto		is null
												and
												periodo_segundo		is null
											)
											or
											(
												periodo_ano		is not null
												and
												periodo_mes		is not null
												and
												periodo_dia		is not null
												and
												periodo_hora		is not null
												and
												periodo_minuto		is not null
												and
												periodo_segundo		is not null
											)
										)
									,
		executar_dia_de			numeric(2),
		executar_dia_ate		numeric(2),
		executar_hora_de		numeric(2),
		executar_hora_ate		numeric(2),
		duracao_maximo			numeric(4),
		execucao_ultima			date,
		retencao			numeric(6)
							constraint tb_replicacao_direcao_591
							check (retencao >	5)
						,
		habilitado		boolean
	)
;

----------------------------------------------
-- TB_REPLICACAO_EXECUCAO
--
-- Tabela que conterá o histórico da replicação
--
-- codigo_historico	:	Código único para cada replicação executada
-- data_inicio		:	Data de inicio da replicação para um determinado ORIGEM x DESTINO
-- data_fim			:	Data final da replicação para determinado ORIGEM x DESTINO
-- database_origem	:	Origem do banco de dados
-- database_destino	:	Destino dos dados
-- mensagem			:	Coluna para armazenamento de informativos sobre a replicação
----------------------------------------------

CREATE TABLE IF NOT EXISTS	tb_replicacao_execucao
	(
		codigo_execucao	serial CONSTRAINT tb_replicacao_historico_pk PRIMARY KEY,
		data_atual		timestamp NOT NULL DEFAULT localtimestamp,
		usuario         text CONSTRAINT tb_replicacao_2 NOT NULL,
		processo		text NOT NULL,
		database_origem	text NOT NULL,
						CONSTRAINT tb_replicacao_execucao_f2 FOREIGN KEY(database_origem)
						REFERENCES tb_replicacao_direcao(database_origem) DEFERRABLE,
		usuario_origem	text NOT NULL,
		database_destino text NOT NULL,
						CONSTRAINT tb_replicacao_execucao_f4 FOREIGN KEY(database_destino)
						REFERENCES tb_replicacao_direcao(database_destino) DEFERRABLE,
		usuario_destino	text NOT NULL,
		inicio_data_hora timestamp NOT NULL,
		fim_data_hora	timestamp,
		data_atual_ate	timestamp,
		ocorrencia		text NOT NULL
	)
;

----------------------------------------------
-- TB_REPLICACAO_TABELA_EXECUCAO
--
-- Tabela que conterá o histórico da replicação e seus respectivas tabelas.
--
-- codigo_historico	:	Código do historico geral
-- codigo_tabela	:	Código sequencial único da tabela
-- data_inicio		:	Data de inicio da replicação para um determinado ORIGEM x DESTINO
-- data_fim			:	Data final da replicação para determinado ORIGEM x DESTINO
-- tabela			:	Tabela que foi replicada
-- linhas			:	Quantidade de linhas que foram puxadas
-- mensagem			:	Coluna para armazenamento de informativos sobre a replicação da tabela
----------------------------------------------

CREATE TABLE IF NOT EXISTS	tb_replicacao_tabela_execucao
	(
		codigo_tabela	serial CONSTRAINT tb_replicacao_tabela_execucao_pk PRIMARY KEY,
		data_atual		timestamp NOT NULL DEFAULT localtimestamp,
		processo		text NOT NULL,
		database_origem	text NOT NULL,
						CONSTRAINT tb_replicacao_tabela_execucao_f2 FOREIGN KEY(database_origem)
						REFERENCES tb_replicacao_direcao(database_origem) DEFERRABLE,
		usuario_origem	text NOT NULL,
		database_destino text NOT NULL,
						CONSTRAINT tb_replicacao_tabela_execucao_f4 FOREIGN KEY(database_destino)
						REFERENCES tb_replicacao_direcao(database_destino) DEFERRABLE,
		usuario_destino	text NOT NULL,
		execucao_inicio_data_hora
						timestamp NOT NULL,
		ordem			Integer NOT NULL,
		inicio_data_hora timestamp NOT NULL,
		fim_data_hora	timestamp,
		data_atual_ate	timestamp,
		linhas_processadas numeric(18),
		sucesso			boolean,
		mensagem		text NOT NULL
	)
;