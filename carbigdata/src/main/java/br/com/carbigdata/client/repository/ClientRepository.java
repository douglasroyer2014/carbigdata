package br.com.carbigdata.client.repository;

import br.com.carbigdata.client.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {

    /**
     * Busca o cliente que tem o CPF informado.
     *
     * @param cpf informação do CPF.
     * @return um {@link Optional<ClientEntity}.
     */
    Optional<ClientEntity> findByCpf(long cpf);
}
