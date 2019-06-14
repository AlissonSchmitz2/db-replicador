alter table tb_replicacao_tabela add coluna_controle text;

alter table tb_replicacao_tabela drop constraint tb_replicacao_tabela_f2;

alter table tb_replicacao_tabela
	add constraint tb_replicacao_tabela_f2
		foreign key (processo) references tb_replicacao_processo
			on update cascade on delete cascade
			deferrable;
			
alter table tb_replicacao_tabela rename column operacao to backup_incremental;