package com.catalis.commons.ecm.interfaces.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for SignatureRequest entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignatureRequestDTO {
    private Long id;
    private Long documentId;
    private LocalDateTime requestDate;
    private Long statusId;
    private Long signatoryOrder;
    private String interveningParties;
    private String externalSignatureId;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    private String createdBy;
    private String updatedBy;
}