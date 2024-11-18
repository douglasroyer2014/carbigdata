package br.com.carbigdata.client.controller;

import br.com.carbigdata.client.dto.ClientDTO;
import br.com.carbigdata.client.entity.ClientEntity;
import br.com.carbigdata.client.service.ClientService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientController {

    ClientService clientService;

    @GetMapping
    public Iterable<ClientEntity> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return clientService.getAll(page, size);
    }

    @PostMapping
    public ResponseEntity save(@Valid @RequestBody ClientDTO clientDTO) {
        clientService.save(clientDTO);
        return ResponseEntity.ok("Cadastrado com sucesso!");
    }

    @PutMapping("/{code}")
    public ResponseEntity update(@Valid @RequestBody ClientDTO clientDTO, @PathVariable int code) {
        return clientService.update(clientDTO, code);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity delete(@PathVariable int code) {
        clientService.delete(code);
        return ResponseEntity.ok("Deletado com sucesso!");
    }
}
