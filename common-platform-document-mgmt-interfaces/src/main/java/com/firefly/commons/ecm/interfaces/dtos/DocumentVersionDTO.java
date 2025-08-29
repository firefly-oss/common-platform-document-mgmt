package com.firefly.commons.ecm.interfaces.dtos;

import com.firefly.annotations.ValidDateTime;
import com.firefly.commons.ecm.interfaces.enums.StorageType;
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
 * Data Transfer Object for DocumentVersion entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Document version data transfer object")
public class DocumentVersionDTO {

    @Schema(description = "Unique identifier of the document version")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Schema(description = "ID of the document this version belongs to")
    private Long documentId;

    @Schema(description = "Version number")
    private Integer versionNumber;

    @Schema(description = "File name of this version")
    private String fileName;

    @Schema(description = "File extension of this version")
    private String fileExtension;

    @Schema(description = "MIME type of this version")
    private String mimeType;

    @Schema(description = "Size of the file in bytes")
    private Long fileSize;

    @Schema(description = "Storage type of this version")
    private StorageType storageType;

    @Schema(description = "Path where this version is stored")
    private String storagePath;

    @Schema(description = "Indicates if this version is encrypted")
    private Boolean isEncrypted;

    @Schema(description = "Summary of changes in this version")
    private String changeSummary;

    @Schema(description = "Indicates if this is a major version")
    private Boolean isMajorVersion;

    @Schema(description = "Tenant ID for multi-tenancy support")
    private String tenantId;

    @Schema(description = "Date and time when this version was created")
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "User who created this version")
    private String createdBy;
}
