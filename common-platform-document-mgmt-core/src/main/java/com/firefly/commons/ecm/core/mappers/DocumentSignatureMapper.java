package com.firefly.commons.ecm.core.mappers;

import com.firefly.commons.ecm.interfaces.dtos.DocumentSignatureDTO;
import com.firefly.commons.ecm.models.entities.DocumentSignature;
import org.mapstruct.Mapper;

/**
 * Mapper for converting between DocumentSignature entity and DTO.
 */
@Mapper(componentModel = "spring")
public interface DocumentSignatureMapper {
    DocumentSignatureDTO toDTO(DocumentSignature entity);
    DocumentSignature toEntity(DocumentSignatureDTO dto);
}
