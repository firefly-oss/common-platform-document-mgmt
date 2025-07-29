package com.catalis.commons.ecm.interfaces.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for NotificationType entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTypeDTO {
    private Long id;
    private String code;
    private String description;
}