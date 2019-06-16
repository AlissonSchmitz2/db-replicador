alter table tb_replicacao_direcao drop constraint tb_replicacao_direcao_database_origem_key cascade;
alter table tb_replicacao_direcao drop constraint tb_replicacao_direcao_database_destino_key cascade;

alter table tb_replicacao_direcao alter column database_origem type integer using database_origem::integer;
alter table tb_replicacao_direcao alter column database_destino type integer using database_destino::integer;

alter table tb_replicacao_direcao
	add constraint tb_replicacao_direcao_tb_replicacao_codigo_replicacao_fk
		foreign key (database_origem) references tb_replicacao
			on update cascade on delete cascade
			deferrable;

alter table tb_replicacao_direcao
	add constraint tb_replicacao_direcao_tb_replicacao_codigo_replicacao_fk_2
		foreign key (database_destino) references tb_replicacao
			on update cascade on delete cascade
			deferrable;