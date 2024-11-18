package br.com.carbigdata.address.dto;

import br.com.carbigdata.address.entity.AddressEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddressDTOTest {

    @Test
    public void testConvertAddressDTOToEntity() {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setAddress("Rua das Flores");
        addressDTO.setNeighborhood("Centro");
        addressDTO.setCep(12345678);
        addressDTO.setCity("Blumenau");
        addressDTO.setState("SC");

        AddressEntity addressEntity = addressDTO.convertAddressDTOToEntity();

        assertEquals("Rua das Flores", addressEntity.getAddress());
        assertEquals("Centro", addressEntity.getNeighborhood());
        assertEquals(12345678, addressEntity.getCep());
        assertEquals("Blumenau", addressEntity.getCity());
        assertEquals("SC", addressEntity.getState());
    }
}
