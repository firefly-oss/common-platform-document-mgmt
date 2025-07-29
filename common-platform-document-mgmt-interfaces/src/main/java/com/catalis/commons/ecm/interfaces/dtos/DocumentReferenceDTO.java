package com.catalis.commons.ecm.interfaces.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for DocumentReference entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentReferenceDTO {
    private Long id;
    private Long documentId;
    private Long relatedDocumentId;
    private Long referenceTypeId;
    private String note;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    private String createdBy;
    private String updatedBy;
}