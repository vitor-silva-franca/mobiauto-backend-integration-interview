package com.vitorsilvafranca.mobiauto_backend_integration_interview.model.mapper;

import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Cliente;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.dto.ClienteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);

    Cliente toCliente(ClienteDTO clienteDTO);

    @Mapping(source = "endereco.cep", target = "cep")
    ClienteDTO toClienteDTO(Cliente cliente);
}
