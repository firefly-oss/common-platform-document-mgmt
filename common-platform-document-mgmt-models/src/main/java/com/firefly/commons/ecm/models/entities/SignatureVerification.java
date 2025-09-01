package com.firefly.commons.ecm.models.entities;

import com.firefly.commons.ecm.interfaces.enums.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;
/**
 * Entity representing a verification of a document signature in the Enterprise Content Management system.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("signature_verifications")
public class SignatureVerification {

    @Id
    @Column("id")
    private UUID id;

    @Column("document_signature_id")
    private UUID documentSignatureId;

    @Column("verification_status")
    private VerificationStatus verificationStatus;

    @Column("verification_details")
    private String verificationDetails;

    @Column("verification_provider")
    private String verificationProvider;

    @Column("verification_timestamp")
    private LocalDateTime verificationTimestamp;

    @Column("certificate_valid")
    private Boolean certificateValid;

    @Column("certificate_details")
    private String certificateDetails;

    @Column("certificate_issuer")
    private String certificateIssuer;

    @Column("certificate_subject")
    private String certificateSubject;

    @Column("certificate_valid_from")
    private LocalDateTime certificateValidFrom;

    @Column("certificate_valid_until")
    private LocalDateTime certificateValidUntil;

    @Column("document_integrity_valid")
    private Boolean documentIntegrityValid;

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
