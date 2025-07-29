package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.interfaces.dtos.NotificationLogDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing notification logs.
 */
public interface NotificationLogService {
    /**
     * Filters the notification logs based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for NotificationLogDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of notification logs
     */
    Mono<PaginationResponse<NotificationLogDTO>> filterNotificationLogs(FilterRequest<NotificationLogDTO> filterRequest);
    
    /**
     * Creates a new notification log based on the provided information.
     *
     * @param notificationLogDTO the DTO object containing details of the notification log to be created
     * @return a Mono that emits the created NotificationLogDTO object
     */
    Mono<NotificationLogDTO> createNotificationLog(NotificationLogDTO notificationLogDTO);
    
    /**
     * Updates an existing notification log with updated information.
     *
     * @param notificationLogId the unique identifier of the notification log to be updated
     * @param notificationLogDTO the data transfer object containing the updated details of the notification log
     * @return a reactive Mono containing the updated NotificationLogDTO
     */
    Mono<NotificationLogDTO> updateNotificationLog(Long notificationLogId, NotificationLogDTO notificationLogDTO);
    
    /**
     * Deletes a notification log identified by its unique ID.
     *
     * @param notificationLogId the unique identifier of the notification log to be deleted
     * @return a Mono that completes when the notification log is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deleteNotificationLog(Long notificationLogId);
    
    /**
     * Retrieves a notification log by its unique identifier.
     *
     * @param notificationLogId the unique identifier of the notification log to retrieve
     * @return a Mono emitting the {@link NotificationLogDTO} representing the notification log if found,
     *         or an empty Mono if the notification log does not exist
     */
    Mono<NotificationLogDTO> getNotificationLogById(Long notificationLogId);
}