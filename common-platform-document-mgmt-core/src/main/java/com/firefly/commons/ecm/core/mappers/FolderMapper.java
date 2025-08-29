package com.firefly.commons.ecm.core.mappers;

import com.firefly.commons.ecm.interfaces.dtos.FolderDTO;
import com.firefly.commons.ecm.models.entities.Folder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FolderMapper {
    FolderDTO toDTO(Folder entity);
    Folder toEntity(FolderDTO dto);
}
