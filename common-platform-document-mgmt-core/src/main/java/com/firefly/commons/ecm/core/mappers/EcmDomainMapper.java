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
     * Helper method to get a value from DTO or fall back to default configuration.
     */
    private <T> T getValueOrDefault(T dtoValue, T defaultValue) {
        return dtoValue != null ? dtoValue : defaultValue;
    }
}