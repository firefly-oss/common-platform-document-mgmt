package com.firefly.commons.ecm.core.mappers;

import com.firefly.commons.ecm.interfaces.dtos.DocumentPermissionDTO;
import com.firefly.commons.ecm.models.entities.DocumentPermission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentPermissionMapper {
    DocumentPermissionDTO toDTO (DocumentPermission entity);
    DocumentPermission toEntity (DocumentPermissionDTO dto);
}
