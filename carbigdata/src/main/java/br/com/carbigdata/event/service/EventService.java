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
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventService {

    EventRepository eventRepository;
    ClientService clientService;
    AddressService addressService;
    EventPhotoRepository eventPhotoRepository;
    MinioService minioService;

    /**
     * Busca todos os registros de ocorrência conforme os parametros passado e adiciona a URL da imagem..
     *
     * @param paramsMap mapa de parametros.
     * @return uma lista de {@link EventTuple}.
     */
    public List<EventTuple> getAll(Map<String, String> paramsMap) {
        List<EventTuple> eventTupleList = eventRepository.findAllByParamsMap(paramsMap);

        for (EventTuple eventTuple: eventTupleList) {
            eventTuple.setImageUrl(minioService.getImageUrl(eventTuple.getImageName()));
        }

        return eventTupleList;
    }

    /**
     * Grava as informações da ocorrência, caso o cliente e o endereço sejam validos.
     *
     * @param eventDTO informações da ocorrência.
     * @return a resposta do servidor.
     */
    public ResponseEntity save(EventDTO eventDTO) {
        Optional<ClientEntity> clientEntityOptional = clientService.findById(eventDTO.getClientCode());
        if (clientEntityOptional.isEmpty()) {
            return new ResponseEntity("Cliente não encontrado!", HttpStatus.BAD_REQUEST);
        }
        Optional<AddressEntity> addressEntityOptional = addressService.findById(eventDTO.getAddressCode());
        if (addressEntityOptional.isEmpty()) {
            return new ResponseEntity("Endereço não encontrado!", HttpStatus.BAD_REQUEST);
        }
        eventRepository.save(eventDTO.convertEventDTOtoEntity());

        return ResponseEntity.ok("Cadastrado com sucesso!");
    }

    /**
     * Valida se a ocorrência, cliente e o endereção são válidos, caso sejam é feito a alteração da ocorrência.
     *
     * @param eventDTO informações da ocorrência.
     * @param code código da ocorrência.
     * @return a resposta do servidor.
     */
    public ResponseEntity update(EventDTO eventDTO, int code) {
        Optional<EventEntity> eventEntityOptional = eventRepository.findById(code);
        if (eventEntityOptional.isEmpty()) {
            return new ResponseEntity("Ocorrencia não encontrada!", HttpStatus.OK);
        }
        if (eventEntityOptional.get().getEventType() == EventType.FINALIZADA) {
            return new ResponseEntity("Ocorrência finalizada, não pode ser alterada.", HttpStatus.OK);
        }
        Optional<ClientEntity> clientEntityOptional = clientService.findById(eventDTO.getClientCode());
        if (clientEntityOptional.isEmpty()) {
            return new ResponseEntity("Cliente não encontrado!", HttpStatus.BAD_REQUEST);
        }
        Optional<AddressEntity> addressEntityOptional = addressService.findById(eventDTO.getAddressCode());
        if (addressEntityOptional.isEmpty()) {
            return new ResponseEntity("Endereço não encontrado!", HttpStatus.BAD_REQUEST);
        }

        EventEntity eventEntity = eventDTO.convertEventDTOtoEntity();
        eventEntity.setCode(code);
        eventRepository.save(eventEntity);

        return ResponseEntity.ok("Alterado com sucesso!");
    }

    /**
     * Deleta a ocorrência de acordo com o código.
     *
     * @param code código da ocorrência.
     */
    public void deleteById(int code) {
        eventRepository.deleteById(code);
    }

    /**
     * Busca a ocorrência de acordo com o código.
     *
     * @param code código da ocorrência.
     * @return um optional de {@link EventEntity}.
     */
    public Optional<EventEntity> findById(int code) {
        return eventRepository.findById(code);
    }

    /**
     * Válida se o cliente é valido, caso seja grava as informações do endereço, da ocorrência e da foto.
     *
     * @param cpf informação do CPF do cliente.
     * @param addressDTO informações do endereço.
     * @param file foto/arquivo da ocorrência.
     * @return a resposta do servidor.
     */
    @Transactional
    public ResponseEntity saveEvent(long cpf, AddressDTO addressDTO, MultipartFile file) {
        Optional<ClientEntity> clientEntityOptional = clientService.findByCpf(cpf);
        if (clientEntityOptional.isEmpty()) {
            return new ResponseEntity("Cliente não encontrado!", HttpStatus.BAD_REQUEST);
        }
        AddressEntity addressEntity = addressService.save(addressDTO);

        EventEntity eventEntity = new EventEntity();
        eventEntity.setEventType(EventType.ATIVA);
        eventEntity.setEventDate(LocalDate.now());
        eventEntity.setAddress(addressEntity);
        eventEntity.setClient(clientEntityOptional.get());
        eventRepository.save(eventEntity);

        EventPhotoEntity eventPhotoEntity = new EventPhotoEntity();
        eventPhotoEntity.setEvent(eventEntity);
        eventPhotoEntity.setPathBucket(file.getOriginalFilename());
        eventPhotoEntity.setHash(String.valueOf(file.getOriginalFilename().hashCode()));
        eventPhotoEntity.setCreationDate(LocalDate.now());
        eventPhotoRepository.save(eventPhotoEntity);
        minioService.uploadFile(file);

        return ResponseEntity.ok("Cadastrado com sucesso!");
    }

    /**
     * Finaliza a ocorrência alterando o status para finalizado, conforme o código informado.
     *
     * @param code informação do código.
     * @return a resposta do servidor.
     */
    public ResponseEntity finishedEvent(int code) {
        Optional<EventEntity> eventEntityOptional = eventRepository.findById(code);
        if (eventEntityOptional.isEmpty()) {
            return new ResponseEntity("Ocorrência não encontrada!", HttpStatus.BAD_REQUEST);
        }
        EventEntity entity = eventEntityOptional.get();
        if (entity.getEventType() == EventType.FINALIZADA) {
            return new ResponseEntity("Ocorrência já está finalizada", HttpStatus.CONTINUE);
        }
        entity.setEventType(EventType.FINALIZADA);
        eventRepository.save(entity);

        return ResponseEntity.ok("Ocorrência finalizada");
    }
}
