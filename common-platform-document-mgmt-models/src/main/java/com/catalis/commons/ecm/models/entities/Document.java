package com.catalis.commons.ecm.models.entities;

import com.catalis.commons.ecm.interfaces.enums.DocumentStatus;
import com.catalis.commons.ecm.interfaces.enums.DocumentType;
import com.catalis.commons.ecm.interfaces.enums.SecurityLevel;
import com.catalis.commons.ecm.interfaces.enums.StorageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entity representing a document in the Enterprise Content Management system.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("documents")
public class Document {

    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("file_name")
    private String fileName;

    @Column("file_extension")
    private String fileExtension;

    @Column("mime_type")
    private String mimeType;

    @Column("file_size")
    private Long fileSize;

    @Column("document_type")
    private DocumentType documentType;

    @Column("document_status")
    private DocumentStatus documentStatus;

    @Column("storage_type")
    private StorageType storageType;

    @Column("storage_path")
    private String storagePath;

    @Column("security_level")
    private SecurityLevel securityLevel;

    @Column("folder_id")
    private Long folderId;

    @Column("is_encrypted")
    private Boolean isEncrypted;

    @Column("is_indexed")
    private Boolean isIndexed;

    @Column("is_locked")
    private Boolean isLocked;

    @Column("locked_by")
    private String lockedBy;

    @Column("locked_until")
    private LocalDateTime lockedUntil;

    @Column("expiration_date")
    private LocalDateTime expirationDate;

    @Column("retention_date")
    private LocalDateTime retentionDate;

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

    @Column("checksum")
    private String checksum;
}
