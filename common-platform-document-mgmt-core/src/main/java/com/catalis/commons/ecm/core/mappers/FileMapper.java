package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.FileDTO;
import com.catalis.commons.ecm.models.entities.File;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the File entity and FileDTO
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileMapper {

    /**
     * Converts a File entity to a FileDTO
     *
     * @param entity the File entity
     * @return the FileDTO
     */
    FileDTO toDTO(File entity);

    /**
     * Converts a FileDTO to a File entity
     *
     * @param dto the FileDTO
     * @return the File entity
     */
    File toEntity(FileDTO dto);
}