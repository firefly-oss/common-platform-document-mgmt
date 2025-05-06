package com.catalis.commons.ecm.models.entities;

import com.catalis.commons.ecm.interfaces.enums.SecurityLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entity representing a folder in the Enterprise Content Management system.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("folders")
public class Folder {

    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("parent_folder_id")
    private Long parentFolderId;

    @Column("path")
    private String path;

    @Column("security_level")
    private SecurityLevel securityLevel;

    @Column("is_system_folder")
    private Boolean isSystemFolder;

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
