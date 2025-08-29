package com.firefly.commons.ecm.core.mappers;

import com.firefly.commons.ecm.interfaces.dtos.DocumentMetadataDTO;
import com.firefly.commons.ecm.models.entities.DocumentMetadata;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMetadataMapper {
    DocumentMetadataDTO toDTO(DocumentMetadata entity);
    DocumentMetadata toEntity(DocumentMetadataDTO dto);
}
