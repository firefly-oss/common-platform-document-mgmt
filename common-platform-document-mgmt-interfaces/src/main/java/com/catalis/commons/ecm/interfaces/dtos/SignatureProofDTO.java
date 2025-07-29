package com.catalis.commons.ecm.interfaces.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for SignatureProof entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignatureProofDTO {
    private Long id;
    private Long signatureRequestId;
    private String proofUrl;
    private Long proofTypeId;
    private LocalDateTime proofDate;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    private String createdBy;
    private String updatedBy;
}