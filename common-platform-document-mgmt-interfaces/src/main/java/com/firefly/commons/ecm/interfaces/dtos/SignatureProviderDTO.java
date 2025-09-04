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
/**
 * Data Transfer Object for SignatureProvider entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Signature provider data transfer object")
public class SignatureProviderDTO {

    @Schema(description = "Unique identifier of the signature provider")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @Schema(description = "Name of the signature provider")
    private String name;

    @Schema(description = "Description of the signature provider")
    private String description;

    @Schema(description = "Provider code for identifying the QTSP integration")
    private String providerCode;

    @Schema(description = "Indicates if the signature provider is active")
    private Boolean isActive;

    @Schema(description = "Indicates if this is the default signature provider")
    private Boolean isDefault;

    @Schema(description = "Tenant ID for multi-tenancy support")
    private String tenantId;

    @Schema(description = "Date and time when the signature provider was created")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "User who created the signature provider")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @Schema(description = "Date and time when the signature provider was last updated")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @Schema(description = "User who last updated the signature provider")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @Schema(description = "Version number for optimistic locking")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long version;
}
