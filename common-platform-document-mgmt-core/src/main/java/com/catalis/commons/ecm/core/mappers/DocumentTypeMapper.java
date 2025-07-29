package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.DocumentTypeDTO;
import com.catalis.commons.ecm.models.entities.DocumentType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the DocumentType entity and DocumentTypeDTO
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocumentTypeMapper {

    /**
     * Converts a DocumentType entity to a DocumentTypeDTO
     *
     * @param entity the DocumentType entity
     * @return the DocumentTypeDTO
     */
    DocumentTypeDTO toDTO(DocumentType entity);

    /**
     * Converts a DocumentTypeDTO to a DocumentType entity
     *
     * @param dto the DocumentTypeDTO
     * @return the DocumentType entity
     */
    DocumentType toEntity(DocumentTypeDTO dto);
}