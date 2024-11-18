package br.com.carbigdata.client.service;

import br.com.carbigdata.client.dto.ClientDTO;
import br.com.carbigdata.client.entity.ClientEntity;
import br.com.carbigdata.client.repository.ClientRepository;
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
public class ClientService {

    ClientRepository clientRepository;

    /**
     * Busca todos os registros dos cliente.
     *
     * @param page pagina.
     * @param size tamanho da pagina.
     * @return uma lista de {@link ClientEntity}.
     */
    public Iterable<ClientEntity> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return clientRepository.findAll(pageable);
    }

    /**
     *  Grava as informações do cliente.
     *
     * @param clientDTO informações do cliente.
     */
    public void save(ClientDTO clientDTO) {
        clientRepository.save(clientDTO.convertClientDTOToEntity());
    }

    /**
     * Válida se o código é valido, caso seja valido altera as informações do cliente.
     *
     * @param clientDTO informações do cliente.
     * @param code código do cliente.
     * @return a resposta do servidor.
     */
    public ResponseEntity update(ClientDTO clientDTO, int code) {
        Optional<ClientEntity> entityOptional = clientRepository.findById(code);
        if (entityOptional.isPresent()) {
            ClientEntity entity = clientDTO.convertClientDTOToEntity();
            entity.setCode(code);
            clientRepository.save(entity);
            return ResponseEntity.ok("Alterado com sucesso!");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Deleta um cliente pelo código.
     *
     * @param code informação do código do cliente.
     */
    public void delete(int code) {
        clientRepository.deleteById(code);
    }

    /**
     * Busca o cliente pelo seu código.
     *
     * @param code informação do código do cliente.
     * @return um optional de {@link ClientEntity}.
     */
    public Optional<ClientEntity> findById(int code) {
        return clientRepository.findById(code);
    }

    /**
     * Localiza o cliente pelo seu CPF.
     *
     * @param cpf informação do CPF do cliente.
     * @return um optional de {@link ClientEntity}.
     */
    public Optional<ClientEntity> findByCpf(long cpf) {
        return clientRepository.findByCpf(cpf);
    }
}
