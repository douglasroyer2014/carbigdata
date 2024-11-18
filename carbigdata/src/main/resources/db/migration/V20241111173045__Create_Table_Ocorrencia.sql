create table ocorrencia(
	cod_ocorrencia int,
	cod_cliente int,
	cod_endereco int,
	dta_ocorrencia date,
	sta_ocorrencia varchar(255) not null
);

alter table ocorrencia add primary key (cod_ocorrencia);
alter table ocorrencia add constraint cod_cliente foreign key (cod_cliente) references cliente(cod_cliente);
alter table ocorrencia add constraint cod_endereco foreign key (cod_endereco) references endereco(cod_endereco);

CREATE SEQUENCE ocorrencia_seq;
ALTER TABLE ocorrencia ALTER COLUMN cod_ocorrencia SET DEFAULT nextval('ocorrencia_seq');