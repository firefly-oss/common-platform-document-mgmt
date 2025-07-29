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

@Table("signature_proof")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignatureProof {

    @Id
    private Long id;

    @Column("signature_request_id")
    private Long signatureRequestId;

    @Column("proof_url")
    private String proofUrl;

    @Column("proof_type_id")
    private Long proofTypeId;

    @Column("proof_date")
    private LocalDateTime proofDate;

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