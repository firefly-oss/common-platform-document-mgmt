package com.catalis.commons.ecm.models.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table("document_version")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentVersion {

    @Id
    private Long id;

    @Column("document_id")
    private Long documentId;

    @Column("version_number")
    private Long versionNumber;

    @Column("change_summary")
    private String changeSummary;

    @Column("file_name")
    private String fileName;

    @Column("blob_storage_url")
    private String blobStorageUrl;

    @Column("effective_date")
    private LocalDateTime effectiveDate;

    @CreatedDate
    @Column("date_created")
    private LocalDateTime dateCreated;

    @LastModifiedDate
    @Column("date_updated")
    private LocalDateTime dateUpdated;

    @CreatedBy
    @Column("created_by")
    private String createdBy;

    @LastModifiedBy
    @Column("updated_by")
    private String updatedBy;
}