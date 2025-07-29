package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.SignatureProofDTO;
import com.catalis.commons.ecm.models.entities.SignatureProof;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the SignatureProof entity and SignatureProofDTO
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SignatureProofMapper {

    /**
     * Converts a SignatureProof entity to a SignatureProofDTO
     *
     * @param entity the SignatureProof entity
     * @return the SignatureProofDTO
     */
    SignatureProofDTO toDTO(SignatureProof entity);

    /**
     * Converts a SignatureProofDTO to a SignatureProof entity
     *
     * @param dto the SignatureProofDTO
     * @return the SignatureProof entity
     */
    SignatureProof toEntity(SignatureProofDTO dto);
}