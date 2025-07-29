package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.DocumentDTO;
import com.catalis.commons.ecm.models.entities.Document;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the Document entity and DocumentDTO
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocumentMapper {

    /**
     * Converts a Document entity to a DocumentDTO
     *
     * @param entity the Document entity
     * @return the DocumentDTO
     */
    DocumentDTO toDTO(Document entity);

    /**
     * Converts a DocumentDTO to a Document entity
     *
     * @param dto the DocumentDTO
     * @return the Document entity
     */
    Document toEntity(DocumentDTO dto);
}