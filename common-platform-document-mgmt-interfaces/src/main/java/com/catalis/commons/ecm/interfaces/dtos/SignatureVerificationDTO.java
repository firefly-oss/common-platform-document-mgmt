package com.catalis.commons.ecm.interfaces.dtos;

import com.catalis.commons.ecm.interfaces.enums.VerificationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for SignatureVerification entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Signature verification data transfer object")
public class SignatureVerificationDTO {

    @Schema(description = "Unique identifier of the signature verification")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Schema(description = "ID of the document signature being verified")
    private Long documentSignatureId;

    @Schema(description = "Status of the verification")
    private VerificationStatus verificationStatus;

    @Schema(description = "Details of the verification")
    private String verificationDetails;

    @Schema(description = "Provider used for verification")
    private String verificationProvider;

    @Schema(description = "Timestamp of the verification")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime verificationTimestamp;

    @Schema(description = "Indicates if the certificate is valid")
    private Boolean certificateValid;

    @Schema(description = "Details of the certificate")
    private String certificateDetails;

    @Schema(description = "Issuer of the certificate")
    private String certificateIssuer;

    @Schema(description = "Subject of the certificate")
    private String certificateSubject;

    @Schema(description = "Date and time from which the certificate is valid")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime certificateValidFrom;

    @Schema(description = "Date and time until which the certificate is valid")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime certificateValidUntil;

    @Schema(description = "Indicates if the document integrity is valid")
    private Boolean documentIntegrityValid;

    @Schema(description = "Tenant ID for multi-tenancy support")
    private String tenantId;

    @Schema(description = "Date and time when the signature verification was created")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "User who created the signature verification")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @Schema(description = "Date and time when the signature verification was last updated")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;

    @Schema(description = "User who last updated the signature verification")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @Schema(description = "Version number for optimistic locking")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long version;
}
