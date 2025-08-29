package com.firefly.commons.ecm.core.mappers;

import com.firefly.commons.ecm.interfaces.dtos.DocumentDTO;
import com.firefly.commons.ecm.models.entities.Document;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    DocumentDTO toDTO(Document entity);
    Document toEntity(DocumentDTO dto);
}