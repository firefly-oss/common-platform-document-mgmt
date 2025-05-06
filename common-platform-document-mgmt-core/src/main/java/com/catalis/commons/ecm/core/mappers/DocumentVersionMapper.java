package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.DocumentVersionDTO;
import com.catalis.commons.ecm.models.entities.DocumentVersion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentVersionMapper {
    DocumentVersionDTO toDTO(DocumentVersion entity);
    DocumentVersion toEntity(DocumentVersionDTO dto);
}
