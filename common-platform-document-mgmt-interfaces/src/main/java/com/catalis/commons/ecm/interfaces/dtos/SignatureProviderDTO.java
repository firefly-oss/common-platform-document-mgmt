package com.catalis.commons.ecm.interfaces.dtos;

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
    private Long id;

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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "User who created the signature provider")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @Schema(description = "Date and time when the signature provider was last updated")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;

    @Schema(description = "User who last updated the signature provider")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @Schema(description = "Version number for optimistic locking")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long version;
}
