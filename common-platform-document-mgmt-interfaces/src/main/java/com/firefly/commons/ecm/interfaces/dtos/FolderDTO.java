package com.firefly.commons.ecm.interfaces.dtos;

import com.firefly.annotations.ValidDateTime;
import com.firefly.commons.ecm.interfaces.enums.SecurityLevel;
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
 * Data Transfer Object for Folder entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Folder data transfer object")
public class FolderDTO {

    @Schema(description = "Unique identifier of the folder")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Schema(description = "Name of the folder")
    private String name;

    @Schema(description = "Description of the folder")
    private String description;

    @Schema(description = "ID of the parent folder")
    private Long parentFolderId;

    @Schema(description = "Path of the folder in the folder hierarchy")
    private String path;

    @Schema(description = "Security level of the folder")
    private SecurityLevel securityLevel;

    @Schema(description = "Indicates if this is a system folder")
    private Boolean isSystemFolder;

    @Schema(description = "Tenant ID for multi-tenancy support")
    private String tenantId;

    @Schema(description = "Date and time when the folder was created")
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "User who created the folder")
    private String createdBy;

    @Schema(description = "Date and time when the folder was last updated")
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @Schema(description = "User who last updated the folder")
    private String updatedBy;

    @Schema(description = "Version number for optimistic locking")
    private Long version;
}
