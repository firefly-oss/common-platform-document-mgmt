package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.SignatureRequestStatusDTO;
import com.catalis.commons.ecm.models.entities.SignatureRequestStatus;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the SignatureRequestStatus entity and SignatureRequestStatusDTO
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SignatureRequestStatusMapper {

    /**
     * Converts a SignatureRequestStatus entity to a SignatureRequestStatusDTO
     *
     * @param entity the SignatureRequestStatus entity
     * @return the SignatureRequestStatusDTO
     */
    SignatureRequestStatusDTO toDTO(SignatureRequestStatus entity);

    /**
     * Converts a SignatureRequestStatusDTO to a SignatureRequestStatus entity
     *
     * @param dto the SignatureRequestStatusDTO
     * @return the SignatureRequestStatus entity
     */
    SignatureRequestStatus toEntity(SignatureRequestStatusDTO dto);
}