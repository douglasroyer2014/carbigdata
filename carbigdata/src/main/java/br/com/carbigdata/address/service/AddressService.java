package br.com.carbigdata.address.service;

import br.com.carbigdata.address.dto.AddressDTO;
import br.com.carbigdata.address.entity.AddressEntity;
import br.com.carbigdata.address.repository.AddressRepository;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressService {

    AddressRepository addressRepository;

    /**
     * Busca todos os registros paginado do endereço.
     *
     * @param page pagina.
     * @param size quantidade de registro.
     * @return lista de endereço.
     */
    public Iterable<AddressEntity> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return addressRepository.findAll(pageable);
    }

    /**
     * Grava as informações do endereço.
     *
     * @param addressDTO informações do endereço.
     * @return um {@link AddressEntity}.
     */
    public AddressEntity save(AddressDTO addressDTO) {
        return addressRepository.save(addressDTO.convertAddressDTOToEntity());
    }

    /**
     * Valida se existe um endereço válido com o código passado, caso exista altera as informações e caso não exista da mensage de erro.
     *
     * @param addressDTO informações do endereço
     * @param code código do endereço.
     * @return a resposta da aquisição.
     */
    public ResponseEntity update(AddressDTO addressDTO, int code) {
        Optional<AddressEntity> addressEntity = addressRepository.findById(code);
        if (addressEntity.isPresent()) {
            AddressEntity entity = addressDTO.convertAddressDTOToEntity();
            entity.setCode(code);
            addressRepository.save(entity);

            return ResponseEntity.ok("Alterado com sucesso!");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Deleta um registro com o código.
     *
     * @param code informação do código do endereço.
     */
    public void delete(int code) {
        addressRepository.deleteById(code);
    }

    /**
     * Busca registro com o código.
     *
     * @param code informação do código.
     * @return um {@link Optional<AddressEntity>}.
     */
    public Optional<AddressEntity> findById(int code) {
        return addressRepository.findById(code);
    }
}
