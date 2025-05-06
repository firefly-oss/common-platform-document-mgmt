package com.catalis.commons.ecm.models.entities;

import com.catalis.commons.ecm.interfaces.enums.SignatureFormat;
import com.catalis.commons.ecm.interfaces.enums.SignatureStatus;
import com.catalis.commons.ecm.interfaces.enums.SignatureType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entity representing a document signature in the Enterprise Content Management system.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("document_signatures")
public class DocumentSignature {

    @Id
    private Long id;

    @Column("document_id")
    private Long documentId;

    @Column("document_version_id")
    private Long documentVersionId;

    @Column("signature_provider_id")
    private Long signatureProviderId;

    @Column("signer_party_id")
    private Long signerPartyId;

    @Column("signer_name")
    private String signerName;

    @Column("signer_email")
    private String signerEmail;

    @Column("signature_type")
    private SignatureType signatureType;

    @Column("signature_format")
    private SignatureFormat signatureFormat;

    @Column("signature_status")
    private SignatureStatus signatureStatus;

    @Column("signature_data")
    private String signatureData;

    @Column("signature_certificate")
    private String signatureCertificate;

    @Column("signature_position_x")
    private Integer signaturePositionX;

    @Column("signature_position_y")
    private Integer signaturePositionY;

    @Column("signature_page")
    private Integer signaturePage;

    @Column("signature_width")
    private Integer signatureWidth;

    @Column("signature_height")
    private Integer signatureHeight;

    @Column("signature_reason")
    private String signatureReason;

    @Column("signature_location")
    private String signatureLocation;

    @Column("signature_contact_info")
    private String signatureContactInfo;

    @Column("expiration_date")
    private LocalDateTime expirationDate;

    @Column("signed_at")
    private LocalDateTime signedAt;

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
