/*
 * Copyright 2025 Firefly Software Solutions Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.firefly.commons.ecm.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.firefly.annotations.ValidDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "User who created this metadata")
    private String createdBy;

    @Schema(description = "Date and time when this metadata was last updated")
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @Schema(description = "User who last updated this metadata")
    private String updatedBy;

    @Schema(description = "Version number for optimistic locking")
    private Long version;
}
