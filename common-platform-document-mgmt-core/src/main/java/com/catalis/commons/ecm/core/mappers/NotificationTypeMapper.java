package com.catalis.commons.ecm.core.mappers;

import com.catalis.commons.ecm.interfaces.dtos.NotificationTypeDTO;
import com.catalis.commons.ecm.models.entities.NotificationType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the NotificationType entity and NotificationTypeDTO
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationTypeMapper {

    /**
     * Converts a NotificationType entity to a NotificationTypeDTO
     *
     * @param entity the NotificationType entity
     * @return the NotificationTypeDTO
     */
    NotificationTypeDTO toDTO(NotificationType entity);

    /**
     * Converts a NotificationTypeDTO to a NotificationType entity
     *
     * @param dto the NotificationTypeDTO
     * @return the NotificationType entity
     */
    NotificationType toEntity(NotificationTypeDTO dto);
}