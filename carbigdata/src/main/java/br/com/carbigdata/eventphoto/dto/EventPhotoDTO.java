package br.com.carbigdata.eventphoto.dto;

import br.com.carbigdata.event.entity.EventEntity;
import br.com.carbigdata.eventphoto.entity.EventPhotoEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EventPhotoDTO {

    @NotNull(message = "Código do evento é obrigatório!")
    int eventCode;
    LocalDate creationDate;

    /**
     * Converte as informações constante do DTO para um {@link EventPhotoEntity}.
     *
     * @param pathBucket informação do arquivo.
     * @param hash hash do arquivo.
     * @return um {@link EventPhotoEntity}.
     */
    public EventPhotoEntity convertDTOToEntity(String pathBucket, String hash) {
        EventEntity eventEntity = new EventEntity();
        eventEntity.setCode(this.getEventCode());

        EventPhotoEntity entity = new EventPhotoEntity();
        entity.setEvent(eventEntity);
        entity.setCreationDate(this.getCreationDate());
        entity.setPathBucket(pathBucket);
        entity.setHash(hash);

        return entity;
    }
}
