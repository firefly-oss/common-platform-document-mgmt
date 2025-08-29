package com.firefly.commons.ecm.interfaces.dtos;

import com.firefly.annotations.ValidDateTime;
import com.firefly.annotations.ValidPhoneNumber;
import com.firefly.commons.ecm.interfaces.enums.SignatureFormat;
import com.firefly.commons.ecm.interfaces.enums.SignatureStatus;
import com.firefly.commons.ecm.interfaces.enums.SignatureType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for DocumentSignature entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Document signature data transfer object")
public class DocumentSignatureDTO {

    @Schema(description = "Unique identifier of the document signature")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Schema(description = "ID of the document being signed")
    private Long documentId;

    @Schema(description = "ID of the document version being signed")
    private Long documentVersionId;

    @Schema(description = "ID of the signature provider")
    private Long signatureProviderId;

    @Schema(description = "ID of the party signing the document")
    private Long signerPartyId;

    @Schema(description = "Name of the signer")
    private String signerName;

    @Schema(description = "Email of the signer")
    @Email
    private String signerEmail;

    @Schema(description = "Type of signature")
    private SignatureType signatureType;

    @Schema(description = "Format of signature")
    private SignatureFormat signatureFormat;

    @Schema(description = "Status of the signature")
    private SignatureStatus signatureStatus;

    @Schema(description = "Signature data (base64 encoded)")
    private String signatureData;

    @Schema(description = "Signature certificate (base64 encoded)")
    private String signatureCertificate;

    @Schema(description = "X position of the signature on the document")
    private Integer signaturePositionX;

    @Schema(description = "Y position of the signature on the document")
    private Integer signaturePositionY;

    @Schema(description = "Page number where the signature appears")
    private Integer signaturePage;

    @Schema(description = "Width of the signature")
    private Integer signatureWidth;

    @Schema(description = "Height of the signature")
    private Integer signatureHeight;

    @Schema(description = "Reason for signing")
    private String signatureReason;

    @Schema(description = "Location where the document was signed")
    private String signatureLocation;

    @Schema(description = "Contact information of the signer")
    private String signatureContactInfo;

    @Schema(description = "Date and time when the signature expires")
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expirationDate;

    @Schema(description = "Date and time when the document was signed")
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime signedAt;

    @Schema(description = "Tenant ID for multi-tenancy support")
    private String tenantId;

    @Schema(description = "Date and time when the document signature was created")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "User who created the document signature")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @Schema(description = "Date and time when the document signature was last updated")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @Schema(description = "User who last updated the document signature")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @Schema(description = "Version number for optimistic locking")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long version;
}
