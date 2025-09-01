package com.firefly.commons.ecm.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.firefly.annotations.ValidDateTime;
import com.firefly.commons.ecm.interfaces.enums.SignatureFormat;
import com.firefly.commons.ecm.interfaces.enums.SignatureStatus;
import com.firefly.commons.ecm.interfaces.enums.SignatureType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
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
    private UUID id;

    @Schema(description = "ID of the document being signed")
    private UUID documentId;

    @Schema(description = "ID of the document version being signed")
    private UUID documentVersionId;

    @Schema(description = "ID of the signature provider")
    private UUID signatureProviderId;

    @Schema(description = "ID of the party signing the document")
    private UUID signerPartyId;

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

    // ECM Integration Customizable Fields
    @Schema(
        description = "Custom message displayed to the signer during the signing process. " +
                     "This message will be shown in the ECM provider's interface. " +
                     "If not provided, defaults to configuration value.",
        example = "Please review and sign this important contract by Friday",
        maxLength = 1000
    )
    private String customSignatureMessage;

    @Schema(
        description = "ISO 639-1 language code for the signer's preferred language. " +
                     "Determines the language of the ECM provider's signing interface. " +
                     "Supported values: en, es, fr, de, it, pt, etc.",
        example = "en",
        defaultValue = "en",
        pattern = "^[a-z]{2}$"
    )
    private String signerLanguage;

    @Schema(
        description = "Time zone identifier for the signer. Used for displaying dates and times " +
                     "in the signer's local time zone during the signing process.",
        example = "America/New_York",
        defaultValue = "UTC"
    )
    private String signerTimeZone;

    @Schema(
        description = "Signing order for multiple signers. Determines the sequence in which " +
                     "signers must complete their signatures. Lower numbers sign first.",
        example = "1",
        defaultValue = "1",
        minimum = "1",
        maximum = "100"
    )
    private Integer signingOrder;

    @Schema(
        description = "Role of the signer in the document workflow. Affects permissions and " +
                     "display in the ECM provider interface. " +
                     "Valid values: Signer, Approver, Reviewer, Witness, Notary, CC",
        example = "Approver",
        defaultValue = "Signer",
        allowableValues = {"Signer", "Approver", "Reviewer", "Witness", "Notary", "CC"}
    )
    private String signerRole;

    @Schema(
        description = "Whether this signature is required for document completion. " +
                     "If false, the signature is optional and document can be completed without it.",
        example = "true",
        defaultValue = "true"
    )
    private Boolean signatureRequired;

    @Schema(
        description = "Authentication method required before signing. Determines how the signer " +
                     "must verify their identity before accessing the document. " +
                     "Valid values: EMAIL, SMS, PHONE, ACCESS_CODE, ID_CHECK, NONE",
        example = "EMAIL",
        defaultValue = "EMAIL",
        allowableValues = {"EMAIL", "SMS", "PHONE", "ACCESS_CODE", "ID_CHECK", "NONE"}
    )
    private String authenticationMethod;

    @Schema(
        description = "External signer ID assigned by the ECM provider (e.g., DocuSign envelope ID). " +
                     "This field is populated automatically when the signature request is created " +
                     "through an ECM provider and is used for tracking and status updates.",
        example = "envelope-12345-abcdef",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String externalSignerId;

    @Schema(
        description = "Direct URL for the signer to access the document for signing. " +
                     "Generated by the ECM provider and sent to the signer via email or other means. " +
                     "This URL is typically time-limited and signer-specific.",
        example = "https://demo.docusign.net/signing/startinsession.aspx?t=12345",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String signingUrl;

    @Schema(
        description = "Additional metadata specific to the ECM provider. " +
                     "Can contain provider-specific configuration, tracking information, " +
                     "or custom fields in JSON format. Maximum 5000 characters.",
        example = "{\"provider\": \"docusign\", \"templateId\": \"template-123\", \"customFields\": {\"department\": \"legal\"}}",
        maxLength = 5000
    )
    private String ecmMetadata;

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
