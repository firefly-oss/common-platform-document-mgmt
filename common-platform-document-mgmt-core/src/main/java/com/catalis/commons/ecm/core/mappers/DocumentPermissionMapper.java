package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.DocumentPermissionDTO;
import com.catalis.commons.ecm.models.entities.DocumentPermission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentPermissionMapper {
    DocumentPermissionDTO toDTO (DocumentPermission entity);
    DocumentPermission toEntity (DocumentPermissionDTO dto);
}
