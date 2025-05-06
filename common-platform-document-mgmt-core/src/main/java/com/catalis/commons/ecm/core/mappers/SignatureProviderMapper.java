package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.SignatureProviderDTO;
import com.catalis.commons.ecm.models.entities.SignatureProvider;
import org.mapstruct.Mapper;

/**
 * Mapper for converting between SignatureProvider entity and DTO.
 */
@Mapper(componentModel = "spring")
public interface SignatureProviderMapper {
    SignatureProviderDTO toDTO(SignatureProvider entity);
    SignatureProvider toEntity(SignatureProviderDTO dto);
}
