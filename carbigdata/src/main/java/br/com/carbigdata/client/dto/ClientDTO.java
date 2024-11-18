package br.com.carbigdata.client.dto;

import br.com.carbigdata.client.entity.ClientEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ClientDTO {

    String name;
    LocalDate birthday;
    @NotNull(message = "O campo cpf é obrigatório!")
    long cpf;
    LocalDate creationDate;

    /**
     * Converte as informações constante do DTO para um {@link ClientEntity}.
     *
     * @return um {@link ClientEntity}.
     */
    public ClientEntity convertClientDTOToEntity() {
        ClientEntity entity = new ClientEntity();
        entity.setName(this.getName());
        entity.setBirthday(this.getBirthday());
        entity.setCpf(this.getCpf());
        entity.setCreationDate(this.getCreationDate());

        return entity;
    }
}
