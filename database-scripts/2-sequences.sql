--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	INSERT,
	UPDATE,
	DELETE		ON	tb_replicacao
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	INSERT,
	UPDATE,
	DELETE		ON	tb_replicacao_processo
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	INSERT,
	UPDATE,
	DELETE		ON	tb_replicacao_tabela
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	INSERT,
	UPDATE,
	DELETE		ON	tb_replicacao_direcao
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	INSERT,
	UPDATE,
	DELETE		ON	tb_replicacao_execucao
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	INSERT,
	UPDATE,
	DELETE		ON	tb_replicacao_tabela_execucao
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	UPDATE		ON	tb_replicacao_codigo_replicacao_seq
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	UPDATE		ON	tb_replicacao_processo_codigo_processo_seq
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	UPDATE		ON	tb_replicacao_tabela_codigo_replicacao_seq
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	UPDATE		ON	tb_replicacao_direcao_codigo_direcao_seq
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	UPDATE		ON	tb_replicacao_execucao_codigo_execucao_seq
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	UPDATE		ON	tb_replicacao_tabela_execucao_codigo_tabela_seq
			TO	admin
	;