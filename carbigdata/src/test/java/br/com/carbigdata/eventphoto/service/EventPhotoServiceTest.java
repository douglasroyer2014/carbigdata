package br.com.carbigdata.eventphoto.service;

import br.com.carbigdata.event.entity.EventEntity;
import br.com.carbigdata.event.service.EventService;
import br.com.carbigdata.eventphoto.dto.EventPhotoDTO;
import br.com.carbigdata.eventphoto.entity.EventPhotoEntity;
import br.com.carbigdata.eventphoto.repository.EventPhotoRepository;
import br.com.carbigdata.minio.service.MinioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EventPhotoServiceTest {

    @Mock
    private EventPhotoRepository eventPhotoRepository;

    @Mock
    private EventService eventService;

    @Mock
    private MinioService minioService;

    @InjectMocks
    private EventPhotoService eventPhotoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        Page<EventPhotoEntity> pageResponse = mock(Page.class);
        when(eventPhotoRepository.findAll(pageable)).thenReturn(pageResponse);

        Iterable<EventPhotoEntity> result = eventPhotoService.getALl(page, size);

        verify(eventPhotoRepository, times(1)).findAll(pageable);
    }

    @Test
    void testSaveSuccess() {
        EventPhotoDTO eventPhotoDTO = new EventPhotoDTO();
        eventPhotoDTO.setEventCode(1);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test-photo.jpg");

        EventEntity eventEntity = new EventEntity();
        eventEntity.setCode(1);

        when(eventService.findById(eventPhotoDTO.getEventCode())).thenReturn(Optional.of(eventEntity));

        ResponseEntity<?> response = eventPhotoService.save(eventPhotoDTO, file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cadastrado com sucesso!", response.getBody());
        verify(eventPhotoRepository, times(1)).save(Mockito.any(EventPhotoEntity.class));
        verify(minioService, times(1)).uploadFile(file);
    }

    @Test
    void testSaveEventNotFound() {
        EventPhotoDTO eventPhotoDTO = new EventPhotoDTO();
        eventPhotoDTO.setEventCode(1);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test-photo.jpg");

        when(eventService.findById(eventPhotoDTO.getEventCode())).thenReturn(Optional.empty());

        ResponseEntity<?> response = eventPhotoService.save(eventPhotoDTO, file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Não foi encontrado a ocorrencia informada!", response.getBody());
        verify(eventPhotoRepository, times(0)).save(Mockito.any(EventPhotoEntity.class));
        verify(minioService, times(0)).uploadFile(file);
    }

    @Test
    void testUpdateSuccess() {
        int code = 1;
        EventPhotoDTO eventPhotoDTO = new EventPhotoDTO();
        eventPhotoDTO.setEventCode(1);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("updated-photo.jpg");

        EventPhotoEntity existingPhotoEntity = new EventPhotoEntity();
        existingPhotoEntity.setCode(code);

        EventEntity eventEntity = new EventEntity();
        eventEntity.setCode(1);

        when(eventPhotoRepository.findById(code)).thenReturn(Optional.of(existingPhotoEntity));
        when(eventService.findById(eventPhotoDTO.getEventCode())).thenReturn(Optional.of(eventEntity));

        ResponseEntity<?> response = eventPhotoService.update(code, eventPhotoDTO, file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Alterado com sucesso!", response.getBody());
        verify(eventPhotoRepository, times(1)).save(Mockito.any(EventPhotoEntity.class));
    }

    @Test
    void testUpdate_PhotoNotFound() {
        int code = 1;
        EventPhotoDTO eventPhotoDTO = new EventPhotoDTO();
        eventPhotoDTO.setEventCode(1);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("updated-photo.jpg");

        when(eventPhotoRepository.findById(code)).thenReturn(Optional.empty());

        ResponseEntity<?> response = eventPhotoService.update(code, eventPhotoDTO, file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Não foi encontrado a foto da ocorrencia informada!", response.getBody());
        verify(eventPhotoRepository, times(0)).save(Mockito.any(EventPhotoEntity.class));
    }

    @Test
    void testUpdateEventNotFound() {
        int code = 1;
        EventPhotoDTO eventPhotoDTO = new EventPhotoDTO();
        eventPhotoDTO.setEventCode(1);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("updated-photo.jpg");

        EventPhotoEntity existingPhotoEntity = new EventPhotoEntity();
        existingPhotoEntity.setCode(code);

        when(eventPhotoRepository.findById(code)).thenReturn(Optional.of(existingPhotoEntity));
        when(eventService.findById(eventPhotoDTO.getEventCode())).thenReturn(Optional.empty());

        ResponseEntity<?> response = eventPhotoService.update(code, eventPhotoDTO, file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Não foi encontrado a ocorrencia informada!", response.getBody());
        verify(eventPhotoRepository, times(0)).save(Mockito.any(EventPhotoEntity.class));
    }

    @Test
    void testDeleteById() {
        int code = 1;

        eventPhotoService.deleteById(code);

        verify(eventPhotoRepository, times(1)).deleteById(code);
    }

    @Test
    void testSave() {
        EventPhotoEntity eventPhotoEntity = new EventPhotoEntity();
        eventPhotoEntity.setCode(1);
        eventPhotoEntity.setPathBucket("path/to/file.jpg");
        eventPhotoEntity.setHash("1234567890");

        eventPhotoService.save(eventPhotoEntity);

        verify(eventPhotoRepository, times(1)).save(eventPhotoEntity); // Verifica se o método save foi chamado uma vez com o entity correto
    }
}
