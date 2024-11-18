package br.com.carbigdata.client.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "cliente")
@Data
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_cliente")
    int code;

    @Column(name = "nme_cliente")
    String name;

    @Column(name = "dta_nascimento")
    LocalDate birthday;

    @Column(name = "nro_cpf")
    long cpf;

    @Column(name = "dta_criacao")
    LocalDate creationDate;

    public ClientEntity() {
    }

    public ClientEntity(String name, LocalDate birthday, long cpf, LocalDate creationDate) {
        this.name = name;
        this.birthday = birthday;
        this.cpf = cpf;
        this.creationDate = creationDate;
    }
}
