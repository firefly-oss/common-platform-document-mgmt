package com.firefly.commons.ecm.core.mappers;

import com.firefly.commons.ecm.interfaces.dtos.DocumentTagDTO;
import com.firefly.commons.ecm.models.entities.DocumentTag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentTagMapper {
    DocumentTagDTO toDTO(DocumentTag entity);
    DocumentTag toEntity(DocumentTagDTO dto);
}
