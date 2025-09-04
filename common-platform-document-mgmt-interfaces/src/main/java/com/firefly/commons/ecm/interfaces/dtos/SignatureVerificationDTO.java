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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.firefly.annotations.ValidDateTime;
import com.firefly.commons.ecm.interfaces.enums.VerificationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
/**
 * Data Transfer Object for SignatureVerification entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Signature verification data transfer object")
public class SignatureVerificationDTO {

    @Schema(description = "Unique identifier of the signature verification")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @Schema(description = "ID of the document signature being verified")
    private UUID documentSignatureId;

    @Schema(description = "Status of the verification")
    private VerificationStatus verificationStatus;

    @Schema(description = "Details of the verification")
    private String verificationDetails;

    @Schema(description = "Provider used for verification")
    private String verificationProvider;

    @Schema(description = "Timestamp of the verification")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @ValidDateTime
    private LocalDateTime verificationTimestamp;

    @Schema(description = "Indicates if the certificate is valid")
    private Boolean certificateValid;

    @Schema(description = "Details of the certificate")
    private String certificateDetails;

    @Schema(description = "Issuer of the certificate")
    private String certificateIssuer;

    @Schema(description = "Subject of the certificate")
    private String certificateSubject;

    @Schema(description = "Date and time from which the certificate is valid")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @ValidDateTime
    private LocalDateTime certificateValidFrom;

    @Schema(description = "Date and time until which the certificate is valid")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime certificateValidUntil;

    @Schema(description = "Indicates if the document integrity is valid")
    private Boolean documentIntegrityValid;

    @Schema(description = "Tenant ID for multi-tenancy support")
    private String tenantId;

    @Schema(description = "Date and time when the signature verification was created")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "User who created the signature verification")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @Schema(description = "Date and time when the signature verification was last updated")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @Schema(description = "User who last updated the signature verification")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @Schema(description = "Version number for optimistic locking")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long version;
}
