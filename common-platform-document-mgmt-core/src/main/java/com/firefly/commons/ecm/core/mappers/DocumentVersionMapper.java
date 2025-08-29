package com.firefly.commons.ecm.core.mappers;

import com.firefly.commons.ecm.interfaces.dtos.DocumentVersionDTO;
import com.firefly.commons.ecm.models.entities.DocumentVersion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentVersionMapper {
    DocumentVersionDTO toDTO(DocumentVersion entity);
    DocumentVersion toEntity(DocumentVersionDTO dto);
}
