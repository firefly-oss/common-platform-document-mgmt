package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.SignatureRequestDTO;
import com.catalis.commons.ecm.models.entities.SignatureRequest;
import org.mapstruct.Mapper;

/**
 * Mapper for converting between SignatureRequest entity and DTO.
 */
@Mapper(componentModel = "spring")
public interface SignatureRequestMapper {
    SignatureRequestDTO toDTO(SignatureRequest entity);
    SignatureRequest toEntity(SignatureRequestDTO dto);
}
