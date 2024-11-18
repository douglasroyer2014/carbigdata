package br.com.carbigdata.event.service;

import br.com.carbigdata.address.dto.AddressDTO;
import br.com.carbigdata.address.entity.AddressEntity;
import br.com.carbigdata.address.service.AddressService;
import br.com.carbigdata.client.entity.ClientEntity;
import br.com.carbigdata.client.service.ClientService;
import br.com.carbigdata.event.dto.EventDTO;
import br.com.carbigdata.event.entity.EventEntity;
import br.com.carbigdata.event.enums.EventType;
import br.com.carbigdata.event.repository.EventRepository;
import br.com.carbigdata.event.repository.EventTuple;
import br.com.carbigdata.eventphoto.entity.EventPhotoEntity;
import br.com.carbigdata.eventphoto.repository.EventPhotoRepository;
import br.com.carbigdata.minio.service.MinioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private AddressService addressService;

    @Mock
    private MinioService minioService;

    @Mock
    private EventPhotoRepository eventPhotoRepository;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page", "1");
        paramsMap.put("size", "10");

        EventTuple event1 = new EventTuple();
        event1.setImageName("image1.jpg");
        EventTuple event2 = new EventTuple();
        event2.setImageName("image2.jpg");
        List<EventTuple> eventTupleList = Arrays.asList(event1, event2);

        when(eventRepository.findAllByParamsMap(paramsMap)).thenReturn(eventTupleList);
        when(minioService.getImageUrl("image1.jpg")).thenReturn("http://minio.com/image1.jpg");
        when(minioService.getImageUrl("image2.jpg")).thenReturn("http://minio.com/image2.jpg");

        List<EventTuple> result = eventService.getAll(paramsMap);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("http://minio.com/image1.jpg", result.get(0).getImageUrl());
        assertEquals("http://minio.com/image2.jpg", result.get(1).getImageUrl());
    }

    @Test
    void testSaveClientNotFound() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setClientCode(1);
        eventDTO.setAddressCode(1);

        when(clientService.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<?> result = eventService.save(eventDTO);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Cliente não encontrado!", result.getBody());
    }

    @Test
    void testSaveAddressNotFound() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setClientCode(1);
        eventDTO.setAddressCode(1);

        when(clientService.findById(1)).thenReturn(Optional.of(new ClientEntity()));
        when(addressService.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<?> result = eventService.save(eventDTO);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Endereço não encontrado!", result.getBody());
    }

    @Test
    void testSaveSuccess() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setClientCode(1);
        eventDTO.setAddressCode(1);

        when(clientService.findById(1)).thenReturn(Optional.of(new ClientEntity()));
        when(addressService.findById(1)).thenReturn(Optional.of(new AddressEntity()));

        ResponseEntity<?> result = eventService.save(eventDTO);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Cadastrado com sucesso!", result.getBody());

        verify(eventRepository, times(1)).save(any());
    }

    @Test
    void testUpdateEventNotFound() {
        EventDTO eventDTO = new EventDTO();
        int code = 1;

        when(eventRepository.findById(code)).thenReturn(Optional.empty());

        ResponseEntity<?> result = eventService.update(eventDTO, code);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Ocorrencia não encontrada!", result.getBody());
    }

    @Test
    void testUpdateEventFinalized() {
        EventDTO eventDTO = new EventDTO();
        int code = 1;
        EventEntity eventEntity = new EventEntity();
        eventEntity.setEventType(EventType.FINALIZADA);

        when(eventRepository.findById(code)).thenReturn(Optional.of(eventEntity));

        ResponseEntity<?> result = eventService.update(eventDTO, code);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Ocorrência finalizada, não pode ser alterada.", result.getBody());
    }

    @Test
    void testUpdateClientNotFound() {
        EventDTO eventDTO = new EventDTO();
        int code = 1;
        EventEntity eventEntity = new EventEntity();
        eventEntity.setEventType(EventType.ATIVA);

        when(eventRepository.findById(code)).thenReturn(Optional.of(eventEntity));
        when(clientService.findById(eventDTO.getClientCode())).thenReturn(Optional.empty());

        ResponseEntity<?> result = eventService.update(eventDTO, code);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Cliente não encontrado!", result.getBody());
    }

    @Test
    void testUpdateAddressNotFound() {
        EventDTO eventDTO = new EventDTO();
        int code = 1;
        EventEntity eventEntity = new EventEntity();
        eventEntity.setEventType(EventType.ATIVA);

        when(eventRepository.findById(code)).thenReturn(Optional.of(eventEntity));
        when(clientService.findById(eventDTO.getClientCode())).thenReturn(Optional.of(new ClientEntity()));
        when(addressService.findById(eventDTO.getAddressCode())).thenReturn(Optional.empty());

        ResponseEntity<?> result = eventService.update(eventDTO, code);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Endereço não encontrado!", result.getBody());
    }

    @Test
    void testUpdateSuccess() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setClientCode(1);
        eventDTO.setAddressCode(1);
        int code = 1;
        EventEntity eventEntity = new EventEntity();
        eventEntity.setEventType(EventType.ATIVA);

        when(eventRepository.findById(code)).thenReturn(Optional.of(eventEntity));
        when(clientService.findById(eventDTO.getClientCode())).thenReturn(Optional.of(new ClientEntity()));
        when(addressService.findById(eventDTO.getAddressCode())).thenReturn(Optional.of(new AddressEntity()));

        ResponseEntity<?> result = eventService.update(eventDTO, code);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Alterado com sucesso!", result.getBody());

        verify(eventRepository, times(1)).save(any());
    }

    @Test
    void testFindByIdEventFound() {
        int code = 1;
        EventEntity eventEntity = new EventEntity();
        eventEntity.setCode(code);

        when(eventRepository.findById(code)).thenReturn(Optional.of(eventEntity));

        Optional<EventEntity> result = eventService.findById(code);

        assertTrue(result.isPresent());
        assertEquals(code, result.get().getCode());

        verify(eventRepository, times(1)).findById(code);
    }

    @Test
    void testFindByIdEventNotFound() {
        int code = 1;

        when(eventRepository.findById(code)).thenReturn(Optional.empty());

        Optional<EventEntity> result = eventService.findById(code);

        assertFalse(result.isPresent());

        verify(eventRepository, times(1)).findById(code);
    }

    @Test
    void testDeleteByIdSuccess() {
        int code = 1;

        eventService.deleteById(code);

        verify(eventRepository, times(1)).deleteById(code);
    }

    @Test
    void testSaveEventClientNotFound() {
        long cpf = 123456789;
        AddressDTO addressDTO = new AddressDTO();
        when(clientService.findByCpf(cpf)).thenReturn(Optional.empty());

        ResponseEntity response = eventService.saveEvent(cpf, addressDTO, file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Cliente não encontrado!", response.getBody());
        verify(clientService, times(1)).findByCpf(cpf);
        verifyNoInteractions(addressService, eventRepository, eventPhotoRepository, minioService);
    }

    @Test
    void testSaveEventSuccess() {
        long cpf = 123456789;
        AddressDTO addressDTO = new AddressDTO();
        ClientEntity clientEntity = new ClientEntity();
        AddressEntity addressEntity = new AddressEntity();

        when(clientService.findByCpf(cpf)).thenReturn(Optional.of(clientEntity));
        when(addressService.save(addressDTO)).thenReturn(addressEntity);
        when(file.getOriginalFilename()).thenReturn("photo.jpg");

        ResponseEntity response = eventService.saveEvent(cpf, addressDTO, file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cadastrado com sucesso!", response.getBody());

        verify(clientService, times(1)).findByCpf(cpf);
        verify(addressService, times(1)).save(addressDTO);
        verify(eventRepository, times(1)).save(any(EventEntity.class));
        verify(eventPhotoRepository, times(1)).save(any(EventPhotoEntity.class));
        verify(minioService, times(1)).uploadFile(file);
    }

    @Test
    void testSaveEventFailedToSaveAddress() {
        long cpf = 123456789;
        AddressDTO addressDTO = new AddressDTO();
        ClientEntity clientEntity = new ClientEntity();

        when(clientService.findByCpf(cpf)).thenReturn(Optional.of(clientEntity));
        when(addressService.save(addressDTO)).thenThrow(new RuntimeException("Failed to save address"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eventService.saveEvent(cpf, addressDTO, file);
        });

        assertEquals("Failed to save address", exception.getMessage());

        verify(clientService, times(1)).findByCpf(cpf);
        verify(addressService, times(1)).save(addressDTO);
        verifyNoInteractions(eventRepository, eventPhotoRepository, minioService);
    }

    @Test
    void testFinishedEventEventNotFound() {
        int code = 1;
        when(eventRepository.findById(code)).thenReturn(Optional.empty());

        ResponseEntity<?> response = eventService.finishedEvent(code);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Ocorrência não encontrada!", response.getBody());
        verify(eventRepository, times(1)).findById(code);  // Verifica se o findById foi chamado
    }

    @Test
    void testFinishedEventEventAlreadyFinalized() {
        int code = 1;
        EventEntity eventEntity = new EventEntity();
        eventEntity.setEventType(EventType.FINALIZADA);
        when(eventRepository.findById(code)).thenReturn(Optional.of(eventEntity));

        ResponseEntity<?> response = eventService.finishedEvent(code);

        assertEquals(HttpStatus.CONTINUE, response.getStatusCode());
        assertEquals("Ocorrência já está finalizada", response.getBody());
        verify(eventRepository, times(1)).findById(code);  // Verifica se o findById foi chamado
    }

    @Test
    void testFinishedEventSuccess() {
        int code = 1;
        EventEntity eventEntity = new EventEntity();
        eventEntity.setEventType(EventType.ATIVA);
        when(eventRepository.findById(code)).thenReturn(Optional.of(eventEntity));
        when(eventRepository.save(any(EventEntity.class))).thenReturn(eventEntity);

        ResponseEntity<?> response = eventService.finishedEvent(code);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ocorrência finalizada", response.getBody());
        assertEquals(EventType.FINALIZADA, eventEntity.getEventType());
        verify(eventRepository, times(1)).findById(code);
        verify(eventRepository, times(1)).save(eventEntity);
    }
}
