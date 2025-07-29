package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.SignatureSignerDTO;
import com.catalis.commons.ecm.models.entities.SignatureSigner;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the SignatureSigner entity and SignatureSignerDTO
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SignatureSignerMapper {

    /**
     * Converts a SignatureSigner entity to a SignatureSignerDTO
     *
     * @param entity the SignatureSigner entity
     * @return the SignatureSignerDTO
     */
    SignatureSignerDTO toDTO(SignatureSigner entity);

    /**
     * Converts a SignatureSignerDTO to a SignatureSigner entity
     *
     * @param dto the SignatureSignerDTO
     * @return the SignatureSigner entity
     */
    SignatureSigner toEntity(SignatureSignerDTO dto);
}