package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.DocumentMetadataDTO;
import com.catalis.commons.ecm.models.entities.DocumentMetadata;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMetadataMapper {
    DocumentMetadataDTO toDTO(DocumentMetadata entity);
    DocumentMetadata toEntity(DocumentMetadataDTO dto);
}
