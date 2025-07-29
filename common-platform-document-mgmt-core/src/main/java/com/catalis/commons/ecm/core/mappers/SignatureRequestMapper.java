package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.SignatureRequestDTO;
import com.catalis.commons.ecm.models.entities.SignatureRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the SignatureRequest entity and SignatureRequestDTO
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SignatureRequestMapper {

    /**
     * Converts a SignatureRequest entity to a SignatureRequestDTO
     *
     * @param entity the SignatureRequest entity
     * @return the SignatureRequestDTO
     */
    SignatureRequestDTO toDTO(SignatureRequest entity);

    /**
     * Converts a SignatureRequestDTO to a SignatureRequest entity
     *
     * @param dto the SignatureRequestDTO
     * @return the SignatureRequest entity
     */
    SignatureRequest toEntity(SignatureRequestDTO dto);
}