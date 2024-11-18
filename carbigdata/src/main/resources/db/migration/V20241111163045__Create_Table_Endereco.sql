create table endereco (
	cod_endereco int,
	nme_logradouro varchar(255),
	nme_bairro varchar(255),
	nro_cep int not null,
	nme_cidade varchar(255),
	nme_estado varchar(255)
);

alter table endereco add primary key (cod_endereco);

CREATE SEQUENCE endereco_seq;
ALTER TABLE endereco ALTER COLUMN cod_endereco SET DEFAULT nextval('endereco_seq');
