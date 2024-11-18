package br.com.carbigdata.eventphoto.dto;

import br.com.carbigdata.eventphoto.entity.EventPhotoEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EventPhotoDTOTest {

    @Test
    void testConvertDTOToEntity() {
        EventPhotoDTO eventPhotoDTO = new EventPhotoDTO();
        eventPhotoDTO.setEventCode(1);
        eventPhotoDTO.setCreationDate(LocalDate.now());

        String pathBucket = "path/to/file.jpg";
        String hash = "abc123hash";

        EventPhotoEntity eventPhotoEntity = eventPhotoDTO.convertDTOToEntity(pathBucket, hash);

        assertNotNull(eventPhotoEntity);
        assertEquals(1, eventPhotoEntity.getEvent().getCode());
        assertEquals(pathBucket, eventPhotoEntity.getPathBucket());
        assertEquals(hash, eventPhotoEntity.getHash());
        assertEquals(eventPhotoDTO.getCreationDate(), eventPhotoEntity.getCreationDate());
    }
}
