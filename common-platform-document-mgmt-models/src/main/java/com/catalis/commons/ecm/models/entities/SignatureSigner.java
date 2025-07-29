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

@Table("signature_signer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignatureSigner {

    @Id
    private Long id;

    @Column("signature_request_id")
    private Long signatureRequestId;

    @Column("signer_email")
    private String signerEmail;

    @Column("signer_name")
    private String signerName;

    @Column("sign_order")
    private Long signOrder;

    @Column("status_id")
    private Long statusId;

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