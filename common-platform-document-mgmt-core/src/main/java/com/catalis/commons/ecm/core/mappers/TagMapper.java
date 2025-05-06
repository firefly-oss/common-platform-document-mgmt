package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.TagDTO;
import com.catalis.commons.ecm.models.entities.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagDTO toDTO(Tag entity);
    Tag toEntity(TagDTO dto);
}
