package com.catalis.commons.ecm.interfaces.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for DocumentVersion entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentVersionDTO {
    private Long id;
    private Long documentId;
    private Long versionNumber;
    private String changeSummary;
    private String fileName;
    private String blobStorageUrl;
    private LocalDateTime effectiveDate;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    private String createdBy;
    private String updatedBy;
}