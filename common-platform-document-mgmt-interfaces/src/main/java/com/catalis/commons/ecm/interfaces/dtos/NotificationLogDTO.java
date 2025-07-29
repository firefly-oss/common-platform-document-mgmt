package com.catalis.commons.ecm.interfaces.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for NotificationLog entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationLogDTO {
    private Long id;
    private Long documentId;
    private Long signatureRequestId;
    private Long notificationTypeId;
    private String recipient;
    private String message;
    private LocalDateTime sentAt;
    private Boolean success;
    private String externalId;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
}