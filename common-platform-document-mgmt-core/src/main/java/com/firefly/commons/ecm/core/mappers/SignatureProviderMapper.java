package com.firefly.commons.ecm.core.mappers;

import com.firefly.commons.ecm.interfaces.dtos.SignatureProviderDTO;
import com.firefly.commons.ecm.models.entities.SignatureProvider;
import org.mapstruct.Mapper;

/**
 * Mapper for converting between SignatureProvider entity and DTO.
 */
@Mapper(componentModel = "spring")
public interface SignatureProviderMapper {
    SignatureProviderDTO toDTO(SignatureProvider entity);
    SignatureProvider toEntity(SignatureProviderDTO dto);
}
