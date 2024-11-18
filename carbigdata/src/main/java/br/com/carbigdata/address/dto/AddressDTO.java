package br.com.carbigdata.address.dto;

import br.com.carbigdata.address.entity.AddressEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressDTO {

    /**
     * Logradouro.
     */
    String address;
    /**
     * Bairro.
     */
    String neighborhood;
    /**
     * CEP.
     */
    @NotNull(message = "O campo cep é obrigatório!")
    int cep;
    /**
     * Cidade.
     */
    String city;
    /**
     * Estado.
     */
    String state;

    /**
     * Converte as informações constante do DTO para {@link AddressEntity}.
     *
     * @return um {@link AddressEntity}.
     */
    public AddressEntity convertAddressDTOToEntity() {
        AddressEntity entity = new AddressEntity();
        entity.setAddress(this.address);
        entity.setNeighborhood(this.neighborhood);
        entity.setCep(this.cep);
        entity.setCity(this.city);
        entity.setState(this.state);

        return entity;
    }
}
