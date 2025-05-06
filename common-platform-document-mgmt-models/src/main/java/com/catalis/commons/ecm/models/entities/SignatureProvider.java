package com.catalis.commons.ecm.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

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
    private Long id;

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
