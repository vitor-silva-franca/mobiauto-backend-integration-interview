package com.vitorsilvafranca.mobiauto_backend_integration_interview.model.mapper;

import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Anunciante;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.dto.AnuncianteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AnuncianteMapper {

    AnuncianteMapper INSTANCE = Mappers.getMapper(AnuncianteMapper.class);

    Anunciante toAnunciante(AnuncianteDTO anuncianteDTO);

    AnuncianteDTO toAnuncianteDTO(Anunciante anunciante);
}
