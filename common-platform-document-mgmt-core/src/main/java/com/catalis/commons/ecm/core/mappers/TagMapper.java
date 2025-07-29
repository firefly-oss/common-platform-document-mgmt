package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.TagDTO;
import com.catalis.commons.ecm.models.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the Tag entity and TagDTO
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    /**
     * Converts a Tag entity to a TagDTO
     *
     * @param entity the Tag entity
     * @return the TagDTO
     */
    TagDTO toDTO(Tag entity);

    /**
     * Converts a TagDTO to a Tag entity
     *
     * @param dto the TagDTO
     * @return the Tag entity
     */
    Tag toEntity(TagDTO dto);
}