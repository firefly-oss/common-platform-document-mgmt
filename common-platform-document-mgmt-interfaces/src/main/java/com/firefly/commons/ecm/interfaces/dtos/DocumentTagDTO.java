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
import com.firefly.annotations.ValidDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
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
    private UUID id;

    @Schema(description = "ID of the document")
    private UUID documentId;

    @Schema(description = "ID of the tag")
    private UUID tagId;

    @Schema(description = "Tenant ID for multi-tenancy support")
    private String tenantId;

    @Schema(description = "Date and time when the relationship was created")
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "User who created the relationship")
    private String createdBy;
}
