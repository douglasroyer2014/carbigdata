package br.com.carbigdata.client.service;

import br.com.carbigdata.client.dto.ClientDTO;
import br.com.carbigdata.client.entity.ClientEntity;
import br.com.carbigdata.client.repository.ClientRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        ClientEntity client1 = new ClientEntity();
        client1.setName("João Silva");

        ClientEntity client2 = new ClientEntity();
        client2.setName("Maria Souza");

        List<ClientEntity> clients = List.of(client1, client2);
        Page<ClientEntity> page = new PageImpl<>(clients);

        Pageable pageable = PageRequest.of(0, 2);
        when(clientRepository.findAll(pageable)).thenReturn(page);

        Iterable<ClientEntity> result = clientService.getAll(0, 2);

        assertNotNull(result);
        assertEquals(2, ((Page<ClientEntity>) result).getTotalElements());
        verify(clientRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testSave() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("João Silva");

        ClientEntity clientEntity = clientDTO.convertClientDTOToEntity();

        when(clientRepository.save(any(ClientEntity.class))).thenReturn(clientEntity);

        clientService.save(clientDTO);

        verify(clientRepository, times(1)).save(clientEntity);
    }

    @Test
    public void testUpdateClientExists() {
        int code = 1;
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("João Silva");

        ClientEntity existingClient = new ClientEntity();
        existingClient.setCode(code);
        existingClient.setName("Antigo Nome");

        ClientEntity updatedClient = clientDTO.convertClientDTOToEntity();
        updatedClient.setCode(code);

        when(clientRepository.findById(code)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(ClientEntity.class))).thenReturn(updatedClient);

        ResponseEntity<?> response = clientService.update(clientDTO, code);

        assertEquals(ResponseEntity.ok("Alterado com sucesso!"), response);
        verify(clientRepository, times(1)).findById(code);
        verify(clientRepository, times(1)).save(updatedClient);
    }

    @Test
    public void testUpdateClientDoesNotExist() {
        int code = 1;
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("João Silva");

        when(clientRepository.findById(code)).thenReturn(Optional.empty());

        ResponseEntity<?> response = clientService.update(clientDTO, code);

        assertEquals(ResponseEntity.notFound().build(), response);
        verify(clientRepository, times(1)).findById(code);
        verify(clientRepository, never()).save(any(ClientEntity.class));
    }

    @Test
    public void testDelete() {
        int code = 1;

        clientService.delete(code);

        verify(clientRepository, times(1)).deleteById(code);
    }

    @Test
    public void testFindByIdClientExists() {
        int code = 1;
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setCode(code);
        clientEntity.setName("João Silva");

        when(clientRepository.findById(code)).thenReturn(Optional.of(clientEntity));

        Optional<ClientEntity> result = clientService.findById(code);

        assertTrue(result.isPresent());
        assertEquals(clientEntity, result.get());
        verify(clientRepository, times(1)).findById(code);
    }

    @Test
    public void testFindByIdClientDoesNotExist() {
        int code = 1;

        when(clientRepository.findById(code)).thenReturn(Optional.empty());

        Optional<ClientEntity> result = clientService.findById(code);

        assertFalse(result.isPresent());
        verify(clientRepository, times(1)).findById(code);
    }

    @Test
    public void testFindByCpfClientExists() {
        long cpf = 12345678901L;
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setCpf(cpf);
        clientEntity.setName("Maria Souza");

        when(clientRepository.findByCpf(cpf)).thenReturn(Optional.of(clientEntity));

        Optional<ClientEntity> result = clientService.findByCpf(cpf);

        assertTrue(result.isPresent());
        assertEquals(clientEntity, result.get());
        verify(clientRepository, times(1)).findByCpf(cpf);
    }

    @Test
    public void testFindByCpfClientDoesNotExist() {
        long cpf = 12345678901L;

        when(clientRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        Optional<ClientEntity> result = clientService.findByCpf(cpf);

        assertFalse(result.isPresent());
        verify(clientRepository, times(1)).findByCpf(cpf);
    }
}
