package br.com.carbigdata.eventphoto.service;

import br.com.carbigdata.event.entity.EventEntity;
import br.com.carbigdata.event.service.EventService;
import br.com.carbigdata.eventphoto.dto.EventPhotoDTO;
import br.com.carbigdata.eventphoto.entity.EventPhotoEntity;
import br.com.carbigdata.eventphoto.repository.EventPhotoRepository;
import br.com.carbigdata.minio.service.MinioService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventPhotoService {

    EventPhotoRepository eventPhotoRepository;
    EventService eventService;
    MinioService minioService;

    /**
     * Busca todas as informações das fotos.
     *
     * @param page pagina.
     * @param size tamanho da pagina.
     * @return lista de {@link EventPhotoEntity}.
     */
    public Iterable<EventPhotoEntity> getALl(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventPhotoRepository.findAll(pageable);
    }

    /**
     * Válida se a ocorrência é valido, caso seja válido grava o arquivo e as informações do arquivo.
     *
     * @param eventPhotoDTO informações da foto.
     * @param file arquivo.
     * @return resposta de sucesso ou erro.
     */
    @Transactional
    public ResponseEntity save(EventPhotoDTO eventPhotoDTO, MultipartFile file) {
        Optional<EventEntity> eventEntityOptional = eventService.findById(eventPhotoDTO.getEventCode());
        if (eventEntityOptional.isEmpty()) {
            return new ResponseEntity("Não foi encontrado a ocorrencia informada!", HttpStatus.BAD_REQUEST);
        }
        eventPhotoRepository.save(eventPhotoDTO.convertDTOToEntity(file.getOriginalFilename(), String.valueOf(file.getOriginalFilename().hashCode())));
        minioService.uploadFile(file);

        return ResponseEntity.ok("Cadastrado com sucesso!");
    }

    /**
     * Válida se a ocorrência é valida e a as informações da foto, caso seja válido faz a alteração.
     *
     * @param code informação do código do arquivo.
     * @param eventPhotoDTO informações do arquivo.
     * @param file arquivo.
     * @return resposta de sucesso ou erro.
     */
    public ResponseEntity update(int code, EventPhotoDTO eventPhotoDTO, MultipartFile file) {
        Optional<EventPhotoEntity> eventPhotoEntityOptional = eventPhotoRepository.findById(code);
        if (eventPhotoEntityOptional.isEmpty()) {
            return new ResponseEntity("Não foi encontrado a foto da ocorrencia informada!", HttpStatus.BAD_REQUEST);
        }
        Optional<EventEntity> eventEntityOptional = eventService.findById(eventPhotoDTO.getEventCode());
        if (eventEntityOptional.isEmpty()) {
            return new ResponseEntity("Não foi encontrado a ocorrencia informada!", HttpStatus.BAD_REQUEST);
        }
        EventPhotoEntity eventPhotoEntity = eventPhotoDTO.convertDTOToEntity(file.getOriginalFilename(), String.valueOf(file.getOriginalFilename().hashCode()));
        eventPhotoEntity.setCode(code);
        eventPhotoRepository.save(eventPhotoEntity);

        return ResponseEntity.ok("Alterado com sucesso!");
    }

    /**
     * Deleta as informações do arquivo.
     *
     * @param code informação do código.
     */
    public void deleteById(int code) {
        eventPhotoRepository.deleteById(code);
    }

    /**
     * Grava as informações do arquivo.
     *
     * @param eventPhotoEntity informações do arquivo.
     */
    public void save(EventPhotoEntity eventPhotoEntity) {
        eventPhotoRepository.save(eventPhotoEntity);
    }
}
