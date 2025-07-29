package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.SignatureStatusDTO;
import com.catalis.commons.ecm.models.entities.SignatureStatus;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the SignatureStatus entity and SignatureStatusDTO
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SignatureStatusMapper {

    /**
     * Converts a SignatureStatus entity to a SignatureStatusDTO
     *
     * @param entity the SignatureStatus entity
     * @return the SignatureStatusDTO
     */
    SignatureStatusDTO toDTO(SignatureStatus entity);

    /**
     * Converts a SignatureStatusDTO to a SignatureStatus entity
     *
     * @param dto the SignatureStatusDTO
     * @return the SignatureStatus entity
     */
    SignatureStatus toEntity(SignatureStatusDTO dto);
}