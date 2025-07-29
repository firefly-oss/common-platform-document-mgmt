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

@Table("document")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    private Long id;

    @Column("title")
    private String title;

    @Column("description")
    private String description;

    @Column("creation_date")
    private LocalDateTime creationDate;

    @Column("owner_department")
    private String ownerDepartment;

    @Column("status_id")
    private Long statusId;

    @Column("document_type_id")
    private Long documentTypeId;

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