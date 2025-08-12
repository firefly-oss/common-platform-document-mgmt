package com.catalis.commons.ecm.interfaces.dtos;

import com.catalis.annotations.ValidDateTime;
import com.catalis.commons.ecm.interfaces.enums.PermissionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for DocumentPermission entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Document permission data transfer object")
public class DocumentPermissionDTO {

    @Schema(description = "Unique identifier of the permission")
    private Long id;

    @Schema(description = "ID of the document this permission applies to")
    private Long documentId;

    @Schema(description = "ID of the party this permission applies to")
    private Long partyId;

    @Schema(description = "Type of permission")
    private PermissionType permissionType;

    @Schema(description = "Indicates if the permission is granted or denied")
    private Boolean isGranted;

    @Schema(description = "Date and time when the permission expires")
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expirationDate;

    @Schema(description = "Tenant ID for multi-tenancy support")
    private String tenantId;

    @Schema(description = "Date and time when the permission was created")
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "User who created the permission")
    private String createdBy;

    @Schema(description = "Date and time when the permission was last updated")
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @Schema(description = "User who last updated the permission")
    private String updatedBy;

    @Schema(description = "Version number for optimistic locking")
    private Long version;
}
