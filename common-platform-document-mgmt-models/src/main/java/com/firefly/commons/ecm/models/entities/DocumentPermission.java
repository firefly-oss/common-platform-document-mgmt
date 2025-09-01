package com.firefly.commons.ecm.models.entities;

import com.firefly.commons.ecm.interfaces.enums.PermissionType;
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
 * Entity representing permissions for documents in the Enterprise Content Management system.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("document_permissions")
public class DocumentPermission {

    @Id
    @Column("id")
    private UUID id;

    @Column("document_id")
    private UUID documentId;

    @Column("party_id")
    private UUID partyId; // The unique identifier of the party (customer)

    @Column("permission_type")
    private PermissionType permissionType;

    @Column("is_granted")
    private Boolean isGranted;

    @Column("expiration_date")
    private LocalDateTime expirationDate;

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
