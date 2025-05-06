package com.catalis.commons.ecm.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for DocumentTag entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Document tag relationship data transfer object")
public class DocumentTagDTO {

    @Schema(description = "Unique identifier of the document-tag relationship")
    private Long id;

    @Schema(description = "ID of the document")
    private Long documentId;

    @Schema(description = "ID of the tag")
    private Long tagId;

    @Schema(description = "Tenant ID for multi-tenancy support")
    private String tenantId;

    @Schema(description = "Date and time when the relationship was created")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "User who created the relationship")
    private String createdBy;
}
