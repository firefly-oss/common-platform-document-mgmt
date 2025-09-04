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
 * Entity representing a Qualified Trust Service Provider (QTSP) for digital signatures.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("signature_providers")
public class SignatureProvider {

    @Id
    @Column("id")
    private UUID id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("provider_code")
    private String providerCode;

    @Column("is_active")
    private Boolean isActive;

    @Column("is_default")
    private Boolean isDefault;

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
