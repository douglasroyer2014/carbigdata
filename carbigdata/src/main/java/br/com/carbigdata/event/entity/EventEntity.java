package br.com.carbigdata.event.entity;

import br.com.carbigdata.address.entity.AddressEntity;
import br.com.carbigdata.client.entity.ClientEntity;
import br.com.carbigdata.event.enums.EventType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "ocorrencia")
@Data
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_ocorrencia")
    int code;

    @ManyToOne
    @JoinColumn(name = "cod_cliente")
    ClientEntity client;

    @ManyToOne
    @JoinColumn(name = "cod_endereco")
    AddressEntity address;

    @Column(name = "dta_ocorrencia")
    LocalDate eventDate;

    @Column(name = "sta_ocorrencia")
    EventType eventType;

    public EventEntity() {
    }

    public EventEntity(ClientEntity client, AddressEntity address, LocalDate eventDate, EventType eventType) {
        this.client = client;
        this.address = address;
        this.eventDate = eventDate;
        this.eventType = eventType;
    }
}
