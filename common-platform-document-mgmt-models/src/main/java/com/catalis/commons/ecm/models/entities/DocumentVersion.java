package com.catalis.commons.ecm.models;

import com.catalis.commons.ecm.interfaces.enums.StorageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a version of a document in the Enterprise Content Management system.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("document_versions")
public class DocumentVersion {

    @Id
    private UUID id;

    @Column("document_id")
    private UUID documentId;

    @Column("version_number")
    private Integer versionNumber;

    @Column("file_name")
    private String fileName;

    @Column("file_extension")
    private String fileExtension;

    @Column("mime_type")
    private String mimeType;

    @Column("file_size")
    private Long fileSize;

    @Column("storage_type")
    private StorageType storageType;

    @Column("storage_path")
    private String storagePath;

    @Column("is_encrypted")
    private Boolean isEncrypted;

    @Column("change_summary")
    private String changeSummary;

    @Column("is_major_version")
    private Boolean isMajorVersion;

    @Column("tenant_id")
    private String tenantId;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @CreatedBy
    @Column("created_by")
    private String createdBy;
}
