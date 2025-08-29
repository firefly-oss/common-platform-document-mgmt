package com.firefly.commons.ecm.core.mappers;

import com.firefly.commons.ecm.interfaces.dtos.SignatureRequestDTO;
import com.firefly.commons.ecm.models.entities.SignatureRequest;
import org.mapstruct.Mapper;

/**
 * Mapper for converting between SignatureRequest entity and DTO.
 */
@Mapper(componentModel = "spring")
public interface SignatureRequestMapper {
    SignatureRequestDTO toDTO(SignatureRequest entity);
    SignatureRequest toEntity(SignatureRequestDTO dto);
}
