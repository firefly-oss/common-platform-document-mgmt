package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.DocumentTagDTO;
import com.catalis.commons.ecm.models.entities.DocumentTag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentTagMapper {
    DocumentTagDTO toDTO(DocumentTag entity);
    DocumentTag toEntity(DocumentTagDTO dto);
}
