package br.com.carbigdata.event.repository;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EventTuple {

    String name;
    long cpf;
    int cep;
    String city;
    String state;
    LocalDate eventDate;
    String imageName;
    String imageUrl;

    public EventTuple() {
    }

    public EventTuple(String name, long cpf, int cep, String city, String state, LocalDate eventDate, String imageName) {
        this.name = name;
        this.cpf = cpf;
        this.cep = cep;
        this.city = city;
        this.state = state;
        this.eventDate = eventDate;
        this.imageName = imageName;
    }
}
