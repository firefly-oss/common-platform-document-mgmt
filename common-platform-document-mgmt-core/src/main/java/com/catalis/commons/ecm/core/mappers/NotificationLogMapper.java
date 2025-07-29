package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.NotificationLogDTO;
import com.catalis.commons.ecm.models.entities.NotificationLog;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the NotificationLog entity and NotificationLogDTO
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationLogMapper {

    /**
     * Converts a NotificationLog entity to a NotificationLogDTO
     *
     * @param entity the NotificationLog entity
     * @return the NotificationLogDTO
     */
    NotificationLogDTO toDTO(NotificationLog entity);

    /**
     * Converts a NotificationLogDTO to a NotificationLog entity
     *
     * @param dto the NotificationLogDTO
     * @return the NotificationLog entity
     */
    NotificationLog toEntity(NotificationLogDTO dto);
}