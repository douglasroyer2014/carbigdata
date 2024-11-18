package br.com.carbigdata.event.dto;

import br.com.carbigdata.address.entity.AddressEntity;
import br.com.carbigdata.client.entity.ClientEntity;
import br.com.carbigdata.event.entity.EventEntity;
import br.com.carbigdata.event.enums.EventType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EventDTO {

    @NotNull(message = "O campo código do cliente é obrigatório!")
    int clientCode;
    @NotNull(message = "O campo código do endereço é obrigatório!")
    int addressCode;
    LocalDate eventDate;

    /**
     * Converte as informações constante do DTO para um {@link EventEntity}.
     *
     * @return um {@link EventEntity}.
     */
    public EventEntity convertEventDTOtoEntity() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setCode(this.getClientCode());

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setCode(this.getAddressCode());

        EventEntity entity = new EventEntity();
        entity.setClient(clientEntity);
        entity.setAddress(addressEntity);
        entity.setEventDate(this.getEventDate());
        entity.setEventType(EventType.ATIVA);

        return entity;
    }

}
