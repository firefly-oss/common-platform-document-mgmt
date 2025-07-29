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

@Table("signature_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignatureRequest {

    @Id
    private Long id;

    @Column("document_id")
    private Long documentId;

    @Column("request_date")
    private LocalDateTime requestDate;

    @Column("status_id")
    private Long statusId;

    @Column("signatory_order")
    private Long signatoryOrder;

    @Column("intervening_parties")
    private String interveningParties;

    @Column("external_signature_id")
    private String externalSignatureId;

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