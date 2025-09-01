package com.firefly.commons.ecm.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.firefly.annotations.ValidDateTime;
import com.firefly.commons.ecm.interfaces.enums.DocumentStatus;
import com.firefly.commons.ecm.interfaces.enums.DocumentType;
import com.firefly.commons.ecm.interfaces.enums.SecurityLevel;
import com.firefly.commons.ecm.interfaces.enums.StorageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
/**
 * Data Transfer Object for Document entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Document data transfer object")
public class DocumentDTO {

    @Schema(description = "Unique identifier of the document")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @Schema(description = "Name of the document")
    private String name;

    @Schema(description = "Description of the document")
    private String description;

    @Schema(description = "Original file name of the document")
    private String fileName;

    @Schema(description = "File extension of the document")
    private String fileExtension;

    @Schema(description = "MIME type of the document")
    private String mimeType;

    @Schema(description = "Size of the document file in bytes")
    private Long fileSize;

    @Schema(description = "Type of the document")
    private DocumentType documentType;

    @Schema(description = "Current status of the document")
    private DocumentStatus documentStatus;

    @Schema(description = "Storage type of the document")
    private StorageType storageType;

    @Schema(description = "Path where the document is stored")
    private String storagePath;

    @Schema(description = "Security level of the document")
    private SecurityLevel securityLevel;

    @Schema(description = "ID of the folder containing the document")
    private UUID folderId;

    @Schema(description = "Indicates if the document is encrypted")
    private Boolean isEncrypted;

    @Schema(description = "Indicates if the document is indexed for search")
    private Boolean isIndexed;

    @Schema(description = "Indicates if the document is locked for editing")
    private Boolean isLocked;

    @Schema(description = "User who locked the document")
    private String lockedBy;

    @Schema(description = "Date and time until the document is locked")
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lockedUntil;

    @Schema(description = "Date and time when the document expires")
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expirationDate;

    @Schema(description = "Date and time until the document should be retained")
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime retentionDate;

    @Schema(description = "Tenant ID for multi-tenancy support")
    private String tenantId;

    @Schema(description = "Date and time when the document was created")
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "User who created the document")
    private String createdBy;

    @Schema(description = "Date and time when the document was last updated")
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @Schema(description = "User who last updated the document")
    private String updatedBy;

    @Schema(description = "Version number for optimistic locking")
    private Long version;

    @Schema(description = "Checksum of the document file for integrity verification")
    private String checksum;
}
