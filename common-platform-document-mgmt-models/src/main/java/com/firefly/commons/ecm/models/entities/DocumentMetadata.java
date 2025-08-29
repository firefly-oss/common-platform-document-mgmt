package com.firefly.commons.ecm.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entity representing metadata associated with a document in the Enterprise Content Management system.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("document_metadata")
public class DocumentMetadata {

    @Id
    private Long id;

    @Column("document_id")
    private Long documentId;

    @Column("metadata_key")
    private String key;

    @Column("metadata_value")
    private String value;

    @Column("metadata_type")
    private String type;

    @Column("is_searchable")
    private Boolean isSearchable;

    @Column("is_system_metadata")
    private Boolean isSystemMetadata;

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
