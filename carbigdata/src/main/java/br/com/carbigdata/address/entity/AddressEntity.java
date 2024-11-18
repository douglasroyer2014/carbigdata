package br.com.carbigdata.address.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "endereco")
@Data
public class AddressEntity {

    @Id
    @Column(name = "cod_endereco")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer code;

    @Column(name = "nme_logradouro")
    String address;

    @Column(name = "nme_bairro")
    String neighborhood;

    @Column(name = "nro_cep")
    int cep;

    @Column(name = "nme_cidade")
    String city;

    @Column(name = "nme_estado")
    String state;

    public AddressEntity() {
    }

    public AddressEntity(String address, String neighborhood, int cep, String city, String state) {
        this.address = address;
        this.neighborhood = neighborhood;
        this.cep = cep;
        this.city = city;
        this.state = state;
    }
}
