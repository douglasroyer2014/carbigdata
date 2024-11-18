package br.com.carbigdata.client.dto;

import br.com.carbigdata.client.entity.ClientEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClientDTOTest {

    @Test
    public void testConvertClientDTOToEntity() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("João Silva");
        clientDTO.setBirthday(LocalDate.of(1990, 5, 15));
        clientDTO.setCpf(12345678900L);
        clientDTO.setCreationDate(LocalDate.of(2023, 11, 13));

        ClientEntity clientEntity = clientDTO.convertClientDTOToEntity();

        assertNotNull(clientEntity);
        assertEquals("João Silva", clientEntity.getName());
        assertEquals(LocalDate.of(1990, 5, 15), clientEntity.getBirthday());
        assertEquals(12345678900L, clientEntity.getCpf());
        assertEquals(LocalDate.of(2023, 11, 13), clientEntity.getCreationDate());
    }
}
