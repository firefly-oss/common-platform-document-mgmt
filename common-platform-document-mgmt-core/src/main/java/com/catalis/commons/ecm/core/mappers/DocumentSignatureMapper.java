package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.DocumentSignatureDTO;
import com.catalis.commons.ecm.models.entities.DocumentSignature;
import org.mapstruct.Mapper;

/**
 * Mapper for converting between DocumentSignature entity and DTO.
 */
@Mapper(componentModel = "spring")
public interface DocumentSignatureMapper {
    DocumentSignatureDTO toDTO(DocumentSignature entity);
    DocumentSignature toEntity(DocumentSignatureDTO dto);
}
