package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.FolderDTO;
import com.catalis.commons.ecm.models.entities.Folder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FolderMapper {
    FolderDTO toDTO(Folder entity);
    Folder toEntity(FolderDTO dto);
}
