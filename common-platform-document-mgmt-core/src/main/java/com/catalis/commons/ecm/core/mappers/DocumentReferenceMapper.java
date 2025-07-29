package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.DocumentReferenceDTO;
import com.catalis.commons.ecm.models.entities.DocumentReference;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the DocumentReference entity and DocumentReferenceDTO
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocumentReferenceMapper {

    /**
     * Converts a DocumentReference entity to a DocumentReferenceDTO
     *
     * @param entity the DocumentReference entity
     * @return the DocumentReferenceDTO
     */
    DocumentReferenceDTO toDTO(DocumentReference entity);

    /**
     * Converts a DocumentReferenceDTO to a DocumentReference entity
     *
     * @param dto the DocumentReferenceDTO
     * @return the DocumentReference entity
     */
    DocumentReference toEntity(DocumentReferenceDTO dto);
}