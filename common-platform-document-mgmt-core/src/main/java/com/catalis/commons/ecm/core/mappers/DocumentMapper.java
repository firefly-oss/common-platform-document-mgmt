package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.DocumentDTO;
import com.catalis.commons.ecm.models.entities.Document;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    DocumentDTO toDTO(Document entity);
    Document toEntity(DocumentDTO dto);
}