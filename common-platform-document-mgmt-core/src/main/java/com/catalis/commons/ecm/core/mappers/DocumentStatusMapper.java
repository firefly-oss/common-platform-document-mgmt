package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.DocumentStatusDTO;
import com.catalis.commons.ecm.models.entities.DocumentStatus;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the DocumentStatus entity and DocumentStatusDTO
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocumentStatusMapper {

    /**
     * Converts a DocumentStatus entity to a DocumentStatusDTO
     *
     * @param entity the DocumentStatus entity
     * @return the DocumentStatusDTO
     */
    DocumentStatusDTO toDTO(DocumentStatus entity);

    /**
     * Converts a DocumentStatusDTO to a DocumentStatus entity
     *
     * @param dto the DocumentStatusDTO
     * @return the DocumentStatus entity
     */
    DocumentStatus toEntity(DocumentStatusDTO dto);
}