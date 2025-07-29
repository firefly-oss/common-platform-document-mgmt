package com.catalis.commons.ecm.models.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table("file")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class File {

    @Id
    private Long id;

    @Column("document_id")
    private Long documentId;

    @Column("file_name")
    private String fileName;

    @Column("file_type")
    private String fileType;

    @Column("blob_storage_url")
    private String blobStorageUrl;

    @Column("upload_date")
    private LocalDateTime uploadDate;

    @Column("uploaded_by")
    private String uploadedBy;

    @CreatedDate
    @Column("date_created")
    private LocalDateTime dateCreated;

    @LastModifiedDate
    @Column("date_updated")
    private LocalDateTime dateUpdated;
}