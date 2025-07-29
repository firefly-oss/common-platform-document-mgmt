package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.ProofTypeDTO;
import com.catalis.commons.ecm.models.entities.ProofType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the ProofType entity and ProofTypeDTO
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProofTypeMapper {

    /**
     * Converts a ProofType entity to a ProofTypeDTO
     *
     * @param entity the ProofType entity
     * @return the ProofTypeDTO
     */
    ProofTypeDTO toDTO(ProofType entity);

    /**
     * Converts a ProofTypeDTO to a ProofType entity
     *
     * @param dto the ProofTypeDTO
     * @return the ProofType entity
     */
    ProofType toEntity(ProofTypeDTO dto);
}