create table foto_ocorrencia(
	cod_foto_ocorrencia int,
	cod_ocorrencia int,
	dta_criacao date,
	dsc_path_bucket varchar(255),
	dsc_hash varchar(255)
);

alter table foto_ocorrencia add primary key (cod_foto_ocorrencia);
alter table foto_ocorrencia add constraint cod_ocorrencia foreign key (cod_ocorrencia) references ocorrencia (cod_ocorrencia);

CREATE SEQUENCE foto_ocorrencia_seq;
ALTER TABLE foto_ocorrencia ALTER COLUMN cod_foto_ocorrencia SET DEFAULT nextval('foto_ocorrencia_seq');