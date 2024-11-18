package br.com.carbigdata.eventphoto.entity;

import br.com.carbigdata.event.entity.EventEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "foto_ocorrencia")
@Data
public class EventPhotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_foto_ocorrencia")
    int code;

    @ManyToOne
    @JoinColumn(name = "cod_ocorrencia")
    EventEntity event;

    @Column(name = "dta_criacao")
    LocalDate creationDate;

    @Column(name = "dsc_path_bucket")
    String pathBucket;

    @Column(name = "dsc_hash")
    String hash;
}
