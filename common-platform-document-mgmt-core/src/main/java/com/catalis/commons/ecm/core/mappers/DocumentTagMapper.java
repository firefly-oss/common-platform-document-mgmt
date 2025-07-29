package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.DocumentTagDTO;
import com.catalis.commons.ecm.models.entities.DocumentTag;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the DocumentTag entity and DocumentTagDTO
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocumentTagMapper {

    /**
     * Converts a DocumentTag entity to a DocumentTagDTO
     *
     * @param entity the DocumentTag entity
     * @return the DocumentTagDTO
     */
    DocumentTagDTO toDTO(DocumentTag entity);

    /**
     * Converts a DocumentTagDTO to a DocumentTag entity
     *
     * @param dto the DocumentTagDTO
     * @return the DocumentTag entity
     */
    DocumentTag toEntity(DocumentTagDTO dto);
}