package br.com.carbigdata.address.controller;

import br.com.carbigdata.address.dto.AddressDTO;
import br.com.carbigdata.address.entity.AddressEntity;
import br.com.carbigdata.address.service.AddressService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressController {

    AddressService addressService;

    @GetMapping
    public Iterable<AddressEntity> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return addressService.getAll(page, size);
    }

    @PostMapping
    public ResponseEntity save(@Valid @RequestBody AddressDTO addressDTO) {
        addressService.save(addressDTO);
        return ResponseEntity.ok("Cadastrado com sucesso!");
    }

    @PutMapping("/{code}")
    public ResponseEntity update(@Valid @RequestBody AddressDTO addressDTO, @PathVariable int code) {
        return addressService.update(addressDTO, code);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity delete(@PathVariable int code) {
        addressService.delete(code);
        return ResponseEntity.ok("Deletado com sucesso!");
    }
}
