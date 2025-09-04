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


package com.firefly.commons.ecm.models.entities;

import com.firefly.commons.ecm.interfaces.enums.SignatureStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;
/**
 * Entity representing a signature request sent to a signer in the Enterprise Content Management system.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("signature_requests")
public class SignatureRequest {

    @Id
    @Column("id")
    private UUID id;

    @Column("document_signature_id")
    private UUID documentSignatureId;

    @Column("request_reference")
    private String requestReference;

    @Column("request_status")
    private SignatureStatus requestStatus;

    @Column("request_message")
    private String requestMessage;

    @Column("notification_sent")
    private Boolean notificationSent;

    @Column("notification_sent_at")
    private LocalDateTime notificationSentAt;

    @Column("reminder_sent")
    private Boolean reminderSent;

    @Column("reminder_sent_at")
    private LocalDateTime reminderSentAt;

    @Column("expiration_date")
    private LocalDateTime expirationDate;

    @Column("completed_at")
    private LocalDateTime completedAt;

    @Column("tenant_id")
    private String tenantId;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @CreatedBy
    @Column("created_by")
    private String createdBy;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column("updated_by")
    private String updatedBy;

    @Version
    private Long version;
}
