package com.catalis.commons.ecm.interfaces.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for SignatureSigner entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignatureSignerDTO {
    private Long id;
    private Long signatureRequestId;
    private String signerEmail;
    private String signerName;
    private Long signOrder;
    private Long statusId;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    private String createdBy;
    private String updatedBy;
}