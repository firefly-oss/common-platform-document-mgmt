package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.DocumentVersionDTO;
import com.catalis.commons.ecm.models.entities.DocumentVersion;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the DocumentVersion entity and DocumentVersionDTO
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocumentVersionMapper {

    /**
     * Converts a DocumentVersion entity to a DocumentVersionDTO
     *
     * @param entity the DocumentVersion entity
     * @return the DocumentVersionDTO
     */
    DocumentVersionDTO toDTO(DocumentVersion entity);

    /**
     * Converts a DocumentVersionDTO to a DocumentVersion entity
     *
     * @param dto the DocumentVersionDTO
     * @return the DocumentVersion entity
     */
    DocumentVersion toEntity(DocumentVersionDTO dto);
}