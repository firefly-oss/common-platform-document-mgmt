package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.ReferenceTypeDTO;
import com.catalis.commons.ecm.models.entities.ReferenceType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the ReferenceType entity and ReferenceTypeDTO
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReferenceTypeMapper {

    /**
     * Converts a ReferenceType entity to a ReferenceTypeDTO
     *
     * @param entity the ReferenceType entity
     * @return the ReferenceTypeDTO
     */
    ReferenceTypeDTO toDTO(ReferenceType entity);

    /**
     * Converts a ReferenceTypeDTO to a ReferenceType entity
     *
     * @param dto the ReferenceTypeDTO
     * @return the ReferenceType entity
     */
    ReferenceType toEntity(ReferenceTypeDTO dto);
}