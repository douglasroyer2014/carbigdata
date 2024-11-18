package br.com.carbigdata.event.dto;

import br.com.carbigdata.event.entity.EventEntity;
import br.com.carbigdata.event.enums.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EventDTOTest {

    private EventDTO eventDTO;

    @BeforeEach
    public void setUp() {
        eventDTO = new EventDTO();
        eventDTO.setClientCode(1);
        eventDTO.setAddressCode(2);
        eventDTO.setEventDate(LocalDate.of(2023, 11, 13));
    }

    @Test
    public void testConvertEventDTOtoEntity() {
        EventEntity eventEntity = eventDTO.convertEventDTOtoEntity();

        assertNotNull(eventEntity.getClient());
        assertEquals(1, eventEntity.getClient().getCode());

        assertNotNull(eventEntity.getAddress());
        assertEquals(2, eventEntity.getAddress().getCode());

        assertEquals(LocalDate.of(2023, 11, 13), eventEntity.getEventDate());

        assertEquals(EventType.ATIVA, eventEntity.getEventType());
    }
}
