package br.com.carbigdata.event.controller;

import br.com.carbigdata.address.dto.AddressDTO;
import br.com.carbigdata.event.dto.EventDTO;
import br.com.carbigdata.event.repository.EventTuple;
import br.com.carbigdata.event.service.EventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/event")
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventController {

    EventService eventService;
    ObjectMapper objectMapper;

    @GetMapping
    public List<EventTuple> getAll(@RequestParam Map<String, String> paramsMap) {
        return eventService.getAll(paramsMap);
    }

    @PostMapping
    public ResponseEntity save(@Valid @RequestBody EventDTO eventDTO) {
        return eventService.save(eventDTO);
    }

    @PutMapping("/{code}")
    public ResponseEntity update(@Valid @RequestBody EventDTO eventDTO, @PathVariable int code) {
        return eventService.update(eventDTO, code);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity delete(@PathVariable int code) {
        eventService.deleteById(code);
        return ResponseEntity.ok("Deletado com sucesso!");
    }

    @PostMapping("/address/{cpf}")
    public ResponseEntity saveAddress(@PathVariable long cpf, @RequestParam("data") String addressDTOJSON, @RequestParam("file") MultipartFile file) throws JsonProcessingException {
        return eventService.saveEvent(cpf, objectMapper.readValue(addressDTOJSON, AddressDTO.class), file);
    }

    @PostMapping("/{code}")
    public ResponseEntity finishedEvent(@PathVariable int code) {
        return eventService.finishedEvent(code);
    }

}
