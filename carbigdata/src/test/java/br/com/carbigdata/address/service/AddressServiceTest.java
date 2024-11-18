package br.com.carbigdata.address.service;

import br.com.carbigdata.address.dto.AddressDTO;
import br.com.carbigdata.address.entity.AddressEntity;
import br.com.carbigdata.address.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);
        AddressEntity address1 = new AddressEntity("Rua A", "Bairro A", 12345678, "Cidade A", "Estado A");
        AddressEntity address2 = new AddressEntity("Rua B", "Bairro B", 98765432, "Cidade B", "Estado B");
        List<AddressEntity> addressList = Arrays.asList(address1, address2);
        Page<AddressEntity> pageResult = new PageImpl<>(addressList);

        when(addressRepository.findAll(pageable)).thenReturn(pageResult);

        Iterable<AddressEntity> result = addressService.getAll(page, size);

        assertNotNull(result);

        List<AddressEntity> addressEntities = (List<AddressEntity>) ((Page) result).getContent();

        assertEquals(2, addressEntities.size());

        AddressEntity firstAddress = addressEntities.get(0);
        assertEquals("Rua A", firstAddress.getAddress());
        assertEquals("Bairro A", firstAddress.getNeighborhood());
        assertEquals(12345678, firstAddress.getCep());
        assertEquals("Cidade A", firstAddress.getCity());
        assertEquals("Estado A", firstAddress.getState());
    }

    @Test
    public void testSave() {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setAddress("Rua das Palmeiras");
        addressDTO.setNeighborhood("Bairro Novo");
        addressDTO.setCep(65432100);
        addressDTO.setCity("Blumenau");
        addressDTO.setState("SC");

        AddressEntity addressEntity = addressDTO.convertAddressDTOToEntity();

        when(addressRepository.save(any(AddressEntity.class))).thenReturn(addressEntity);

        AddressEntity savedAddress = addressService.save(addressDTO);

        verify(addressRepository, times(1)).save(any(AddressEntity.class));

        assertNotNull(savedAddress);
        assertEquals("Rua das Palmeiras", savedAddress.getAddress());
        assertEquals("Bairro Novo", savedAddress.getNeighborhood());
        assertEquals(65432100, savedAddress.getCep());
        assertEquals("Blumenau", savedAddress.getCity());
        assertEquals("SC", savedAddress.getState());
    }

    @Test
    public void testUpdate_AddressFound() {
        int code = 123;
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setAddress("Rua das Flores");
        addressDTO.setNeighborhood("Bairro das Flores");
        addressDTO.setCep(12345678);
        addressDTO.setCity("Cidade Flores");
        addressDTO.setState("DF");

        AddressEntity existingAddress = new AddressEntity("Rua Antiga", "Bairro Antigo", 98765432, "Cidade Antiga", "SP");
        existingAddress.setCode(code);

        when(addressRepository.findById(code)).thenReturn(Optional.of(existingAddress));
        when(addressRepository.save(any(AddressEntity.class))).thenReturn(existingAddress);

        ResponseEntity response = addressService.update(addressDTO, code);

        verify(addressRepository, times(1)).save(any(AddressEntity.class));

        assertEquals("Alterado com sucesso!", response.getBody());
        assertEquals(200, response.getStatusCodeValue()); // HTTP 200 OK
    }

    @Test
    public void testUpdate_AddressNotFound() {
        int code = 123;
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setAddress("Rua das Flores");
        addressDTO.setNeighborhood("Bairro das Flores");
        addressDTO.setCep(12345678);
        addressDTO.setCity("Cidade Flores");
        addressDTO.setState("DF");

        when(addressRepository.findById(code)).thenReturn(Optional.empty());

        ResponseEntity response = addressService.update(addressDTO, code);

        verify(addressRepository, times(0)).save(any(AddressEntity.class));

        assertEquals(404, response.getStatusCodeValue()); // HTTP 404 NOT FOUND
    }

    @Test
    public void testDelete_AddressExists() {
        int code = 123;

        addressService.delete(code);

        verify(addressRepository, times(1)).deleteById(code);
    }

    @Test
    public void testDelete_AddressNotExists() {
        int code = 999;

        addressService.delete(code);

        verify(addressRepository, times(1)).deleteById(code);
    }

    @Test
    public void testFindById_AddressFound() {
        int code = 123;

        AddressEntity addressEntity = new AddressEntity("Rua das Palmeiras", "Bairro Novo", 12345678, "Cidade Flores", "DF");
        addressEntity.setCode(code);

        when(addressRepository.findById(code)).thenReturn(Optional.of(addressEntity));

        Optional<AddressEntity> foundAddress = addressService.findById(code);

        assertTrue(foundAddress.isPresent());
        assertEquals(code, foundAddress.get().getCode());
    }

    @Test
    public void testFindById_AddressNotFound() {
        int code = 123;

        when(addressRepository.findById(code)).thenReturn(Optional.empty());

        Optional<AddressEntity> foundAddress = addressService.findById(code);

        assertFalse(foundAddress.isPresent());
    }
}

