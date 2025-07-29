package com.catalis.commons.ecm.interfaces.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Document entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime creationDate;
    private String ownerDepartment;
    private Long statusId;
    private Long documentTypeId;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    private String createdBy;
    private String updatedBy;
}