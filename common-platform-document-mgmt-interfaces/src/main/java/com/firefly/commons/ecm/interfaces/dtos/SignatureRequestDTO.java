package com.firefly.commons.ecm.interfaces.dtos;

import com.firefly.annotations.ValidDateTime;
import com.firefly.commons.ecm.interfaces.enums.SignatureStatus;
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
 * Data Transfer Object for SignatureRequest entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Signature request data transfer object")
public class SignatureRequestDTO {

    @Schema(description = "Unique identifier of the signature request")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Schema(description = "ID of the document signature this request is for")
    private Long documentSignatureId;

    @Schema(description = "Reference code for the signature request")
    private String requestReference;

    @Schema(description = "Status of the signature request")
    private SignatureStatus requestStatus;

    @Schema(description = "Message to include in the signature request")
    private String requestMessage;

    @Schema(description = "Indicates if a notification has been sent to the signer")
    private Boolean notificationSent;

    @Schema(description = "Date and time when the notification was sent")
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime notificationSentAt;

    @Schema(description = "Indicates if a reminder has been sent to the signer")
    private Boolean reminderSent;

    @Schema(description = "Date and time when the reminder was sent")
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reminderSentAt;

    @Schema(description = "Date and time when the signature request expires")
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expirationDate;

    @Schema(description = "Date and time when the signature request was completed")
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime completedAt;

    @Schema(description = "Tenant ID for multi-tenancy support")
    private String tenantId;

    @Schema(description = "Date and time when the signature request was created")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "User who created the signature request")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @Schema(description = "Date and time when the signature request was last updated")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ValidDateTime(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @Schema(description = "User who last updated the signature request")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @Schema(description = "Version number for optimistic locking")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long version;
}
