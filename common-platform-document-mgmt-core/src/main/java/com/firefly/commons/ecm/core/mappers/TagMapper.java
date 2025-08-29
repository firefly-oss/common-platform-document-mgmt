package com.firefly.commons.ecm.core.mappers;

import com.firefly.commons.ecm.interfaces.dtos.TagDTO;
import com.firefly.commons.ecm.models.entities.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagDTO toDTO(Tag entity);
    Tag toEntity(TagDTO dto);
}
