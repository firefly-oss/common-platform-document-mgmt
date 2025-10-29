/*
 * Copyright 2025 Firefly Software Solutions Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.firefly.commons.ecm.core.mappers;

import com.firefly.commons.ecm.core.config.EcmIntegrationProperties;
import com.firefly.commons.ecm.interfaces.dtos.DocumentSignatureDTO;
import com.firefly.commons.ecm.interfaces.dtos.DocumentDTO;
import com.firefly.commons.ecm.interfaces.enums.DocumentStatus;
import com.firefly.commons.ecm.models.entities.Document;
import com.firefly.core.ecm.domain.model.esignature.SignatureRequest;
import com.firefly.core.ecm.domain.enums.esignature.SignatureRequestStatus;
import com.firefly.core.ecm.domain.enums.esignature.SignatureRequestType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 * Mapper utility for converting between internal DTOs and ECM domain objects.
 * This mapper ensures that our internal DTOs remain the source of truth for API contracts
 * while ECM domain objects are used only as transport objects for ECM port communication.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EcmDomainMapper {

    private final EcmIntegrationProperties ecmProperties;

    /**
     * Maps a DocumentSignatureDTO to an ECM SignatureRequest domain object.
     * Uses values from the DTO when available, falls back to configuration defaults.
     *
     * @param signatureDTO The internal document signature DTO
     * @param documentId The document ID to use as envelope ID
     * @return ECM SignatureRequest domain object ready for ECM port communication
     */
    public SignatureRequest toEcmSignatureRequest(DocumentSignatureDTO signatureDTO, UUID documentId) {
        log.debug("Mapping DocumentSignatureDTO to ECM SignatureRequest for signature: {}", signatureDTO.getId());

        // Use DTO values when available, fall back to configuration defaults
        String customMessage = getValueOrDefault(
            signatureDTO.getCustomSignatureMessage(),
            ecmProperties.getSignature().getCustomMessage()
        );

        String language = getValueOrDefault(
            signatureDTO.getSignerLanguage(),
            ecmProperties.getSignature().getLanguage()
        );

        String signerRole = getValueOrDefault(
            signatureDTO.getSignerRole(),
            ecmProperties.getSignature().getSignerRole()
        );

        Integer signingOrder = getValueOrDefault(
            signatureDTO.getSigningOrder(),
            ecmProperties.getSignature().getSigningOrder()
        );

        Boolean signatureRequired = getValueOrDefault(
            signatureDTO.getSignatureRequired(),
            ecmProperties.getSignature().getSignatureRequired()
        );

        // Convert expiration date to Instant
        Instant expiresAt = null;
        if (signatureDTO.getExpirationDate() != null) {
            expiresAt = signatureDTO.getExpirationDate().toInstant(ZoneOffset.UTC);
        } else {
            // Calculate default expiration based on configuration
            LocalDateTime defaultExpiration = LocalDateTime.now()
                .plusDays(ecmProperties.getSignature().getExpirationDays());
            expiresAt = defaultExpiration.toInstant(ZoneOffset.UTC);
        }

        return SignatureRequest.builder()
                .id(UUID.fromString(signatureDTO.getId().toString()))
                .envelopeId(documentId)
                .signerEmail(signatureDTO.getSignerEmail())
                .signerName(signatureDTO.getSignerName())
                .signerRole(signerRole)
                .signingOrder(signingOrder)
                .status(SignatureRequestStatus.CREATED)
                .requestType(SignatureRequestType.SIGNATURE)
                .required(signatureRequired)
                .createdAt(Instant.now())
                .expiresAt(expiresAt)
                .customMessage(customMessage)
                .language(language)
                .build();
    }

    /**
     * Updates a DocumentSignatureDTO with ECM-specific response data.
     * This method is used to populate read-only fields after ECM operations.
     *
     * @param signatureDTO The internal DTO to update
     * @param externalSignerId The external signer ID from ECM provider
     * @param signingUrl The signing URL from ECM provider (optional)
     * @return Updated DocumentSignatureDTO
     */
    public DocumentSignatureDTO updateWithEcmResponse(DocumentSignatureDTO signatureDTO,
                                                     String externalSignerId,
                                                     String signingUrl) {
        log.debug("Updating DocumentSignatureDTO with ECM response data for signature: {}", signatureDTO.getId());

        signatureDTO.setExternalSignerId(externalSignerId);
        if (signingUrl != null) {
            signatureDTO.setSigningUrl(signingUrl);
        }

        return signatureDTO;
    }

    /**
     * Map an ECM domain Document to our API DocumentDTO (read-only projection for search results).
     */
    public DocumentDTO fromEcmDocument(com.firefly.core.ecm.domain.model.document.Document ecmDoc) {
        if (ecmDoc == null) return null;
        return DocumentDTO.builder()
                .id(ecmDoc.getId())
                .name(ecmDoc.getName())
                .description(ecmDoc.getDescription())
                .mimeType(ecmDoc.getMimeType())
                .fileExtension(ecmDoc.getExtension())
                .fileSize(ecmDoc.getSize())
                .storagePath(ecmDoc.getStoragePath())
                .createdAt(ecmDoc.getCreatedAt() != null ? LocalDateTime.ofInstant(ecmDoc.getCreatedAt(), ZoneOffset.UTC) : null)
                .updatedAt(ecmDoc.getModifiedAt() != null ? LocalDateTime.ofInstant(ecmDoc.getModifiedAt(), ZoneOffset.UTC) : null)
                .folderId(ecmDoc.getFolderId())
                .checksum(ecmDoc.getChecksum())
                .version(ecmDoc.getVersion() != null ? ecmDoc.getVersion().longValue() : null)
                .build();
    }

    /**
     * Map our local Document entity to ECM domain Document for indexing and interop.
     */
    public com.firefly.core.ecm.domain.model.document.Document toEcmDocument(Document doc) {
        if (doc == null) return null;
        com.firefly.core.ecm.domain.enums.document.DocumentStatus ecmStatus = mapStatus(doc.getDocumentStatus());
        return com.firefly.core.ecm.domain.model.document.Document.builder()
                .id(doc.getId())
                .name(doc.getName() != null ? doc.getName() : doc.getFileName())
                .description(doc.getDescription())
                .mimeType(doc.getMimeType())
                .extension(doc.getFileExtension())
                .size(doc.getFileSize())
                .storagePath(doc.getStoragePath())
                .checksum(doc.getChecksum())
                .checksumAlgorithm(null)
                .version(doc.getVersion() != null ? doc.getVersion().intValue() : null)
                .status(ecmStatus)
                .folderId(doc.getFolderId())
                .ownerId(null)
                .createdBy(null)
                .modifiedBy(null)
                .createdAt(doc.getCreatedAt() != null ? doc.getCreatedAt().toInstant(ZoneOffset.UTC) : null)
                .modifiedAt(doc.getUpdatedAt() != null ? doc.getUpdatedAt().toInstant(ZoneOffset.UTC) : null)
                .expiresAt(doc.getExpirationDate() != null ? doc.getExpirationDate().toInstant(ZoneOffset.UTC) : null)
                .metadata(null)
                .tags(null)
                .encrypted(doc.getIsEncrypted())
                .contentType(null)
                .retentionPolicyId(null)
                .legalHold(null)
                .build();
    }

    private com.firefly.core.ecm.domain.enums.document.DocumentStatus mapStatus(DocumentStatus status) {
        if (status == null) return com.firefly.core.ecm.domain.enums.document.DocumentStatus.ACTIVE;
        return switch (status) {
            case DRAFT -> com.firefly.core.ecm.domain.enums.document.DocumentStatus.CREATING;
            case UNDER_REVIEW -> com.firefly.core.ecm.domain.enums.document.DocumentStatus.UNDER_REVIEW;
            case APPROVED -> com.firefly.core.ecm.domain.enums.document.DocumentStatus.APPROVED;
            case REJECTED -> com.firefly.core.ecm.domain.enums.document.DocumentStatus.REJECTED;
            case PUBLISHED -> com.firefly.core.ecm.domain.enums.document.DocumentStatus.ACTIVE;
            case ARCHIVED -> com.firefly.core.ecm.domain.enums.document.DocumentStatus.ARCHIVED;
            case MARKED_FOR_DELETION, DELETED -> com.firefly.core.ecm.domain.enums.document.DocumentStatus.DELETED;
            case LOCKED -> com.firefly.core.ecm.domain.enums.document.DocumentStatus.LOCKED;
            case EXPIRED -> com.firefly.core.ecm.domain.enums.document.DocumentStatus.EXPIRED;
        };
    }

    /**
     * Helper method to get a value from DTO or fall back to default configuration.
     */
    private <T> T getValueOrDefault(T dtoValue, T defaultValue) {
        return dtoValue != null ? dtoValue : defaultValue;
    }
}