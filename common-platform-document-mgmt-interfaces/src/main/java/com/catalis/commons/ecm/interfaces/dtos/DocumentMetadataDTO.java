package com.catalis.commons.ecm.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for DocumentMetadata entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Document metadata data transfer object")
public class DocumentMetadataDTO {

    @Schema(description = "Unique identifier of the metadata")
    private UUID id;

    @Schema(description = "ID of the document this metadata belongs to")
    private UUID documentId;

    @Schema(description = "Metadata key")
    private String key;

    @Schema(description = "Metadata value")
    private String value;

    @Schema(description = "Type of the metadata value")
    private String type;

    @Schema(description = "Indicates if this metadata is searchable")
    private Boolean isSearchable;

    @Schema(description = "Indicates if this is system metadata")
    private Boolean isSystemMetadata;

    @Schema(description = "Tenant ID for multi-tenancy support")
    private String tenantId;

    @Schema(description = "Date and time when this metadata was created")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "User who created this metadata")
    private String createdBy;

    @Schema(description = "Date and time when this metadata was last updated")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @Schema(description = "User who last updated this metadata")
    private String updatedBy;

    @Schema(description = "Version number for optimistic locking")
    private Long version;
}
