create table cliente(
	cod_cliente int,
	nme_cliente varchar(255),
	dta_nascimento date,
	nro_cpf bigint not null,
	dta_criacao date
);

alter table cliente add primary key (cod_cliente);

CREATE SEQUENCE cliente_seq;
ALTER TABLE cliente ALTER COLUMN cod_cliente SET DEFAULT nextval('cliente_seq');
