package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.SignatureVerificationDTO;
import com.catalis.commons.ecm.models.entities.SignatureVerification;
import org.mapstruct.Mapper;

/**
 * Mapper for converting between SignatureVerification entity and DTO.
 */
@Mapper(componentModel = "spring")
public interface SignatureVerificationMapper {
    SignatureVerificationDTO toDTO(SignatureVerification entity);
    SignatureVerification toEntity(SignatureVerificationDTO dto);
}
