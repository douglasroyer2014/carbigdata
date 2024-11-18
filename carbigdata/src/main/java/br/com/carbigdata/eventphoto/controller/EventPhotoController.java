package br.com.carbigdata.eventphoto.controller;

import br.com.carbigdata.eventphoto.dto.EventPhotoDTO;
import br.com.carbigdata.eventphoto.entity.EventPhotoEntity;
import br.com.carbigdata.eventphoto.service.EventPhotoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eventPhoto")
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventPhotoController {

    EventPhotoService eventPhotoService;
    ObjectMapper objectMapper;

    @GetMapping
    public Iterable<EventPhotoEntity> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return eventPhotoService.getALl(page, size);
    }

    @PostMapping
    public ResponseEntity save(@RequestParam("data") String eventPhotoDTOJson, @RequestParam("file") MultipartFile file) throws JsonProcessingException {
        return eventPhotoService.save(objectMapper.readValue(eventPhotoDTOJson, EventPhotoDTO.class), file);
    }

    @PutMapping("/{code}")
    public ResponseEntity update(@PathVariable int code, @RequestParam("data") String eventPhotoDTOJson, @RequestParam("file") MultipartFile file) throws JsonProcessingException {
        return eventPhotoService.update(code, objectMapper.readValue(eventPhotoDTOJson, EventPhotoDTO.class), file);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity delete(@PathVariable int code) {
        eventPhotoService.deleteById(code);
        return ResponseEntity.ok("Deletado com sucesso!");
    }

}
