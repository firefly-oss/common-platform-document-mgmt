package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.interfaces.dtos.NotificationTypeDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing notification types.
 */
public interface NotificationTypeService {
    /**
     * Filters the notification types based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for NotificationTypeDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of notification types
     */
    Mono<PaginationResponse<NotificationTypeDTO>> filterNotificationTypes(FilterRequest<NotificationTypeDTO> filterRequest);
    
    /**
     * Creates a new notification type based on the provided information.
     *
     * @param notificationTypeDTO the DTO object containing details of the notification type to be created
     * @return a Mono that emits the created NotificationTypeDTO object
     */
    Mono<NotificationTypeDTO> createNotificationType(NotificationTypeDTO notificationTypeDTO);
    
    /**
     * Updates an existing notification type with updated information.
     *
     * @param notificationTypeId the unique identifier of the notification type to be updated
     * @param notificationTypeDTO the data transfer object containing the updated details of the notification type
     * @return a reactive Mono containing the updated NotificationTypeDTO
     */
    Mono<NotificationTypeDTO> updateNotificationType(Long notificationTypeId, NotificationTypeDTO notificationTypeDTO);
    
    /**
     * Deletes a notification type identified by its unique ID.
     *
     * @param notificationTypeId the unique identifier of the notification type to be deleted
     * @return a Mono that completes when the notification type is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deleteNotificationType(Long notificationTypeId);
    
    /**
     * Retrieves a notification type by its unique identifier.
     *
     * @param notificationTypeId the unique identifier of the notification type to retrieve
     * @return a Mono emitting the {@link NotificationTypeDTO} representing the notification type if found,
     *         or an empty Mono if the notification type does not exist
     */
    Mono<NotificationTypeDTO> getNotificationTypeById(Long notificationTypeId);
}