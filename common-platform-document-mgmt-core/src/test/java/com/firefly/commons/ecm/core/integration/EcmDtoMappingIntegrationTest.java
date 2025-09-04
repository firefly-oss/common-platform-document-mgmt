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


package com.firefly.commons.ecm.core.integration;

import com.firefly.commons.ecm.core.config.EcmIntegrationProperties;
import com.firefly.commons.ecm.core.mappers.EcmDomainMapper;
import com.firefly.commons.ecm.core.validation.EcmParameterValidator;
import com.firefly.commons.ecm.interfaces.dtos.DocumentSignatureDTO;
import com.firefly.commons.ecm.interfaces.enums.SignatureFormat;
import com.firefly.commons.ecm.interfaces.enums.SignatureStatus;
import com.firefly.commons.ecm.interfaces.enums.SignatureType;
import com.firefly.core.ecm.domain.model.esignature.SignatureRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for ECM DTO mapping and validation functionality.
 * Tests the complete flow from internal DTOs to ECM domain objects.
 */
class EcmDtoMappingIntegrationTest {

    private EcmIntegrationProperties ecmProperties;
    private EcmDomainMapper ecmDomainMapper;
    private EcmParameterValidator ecmParameterValidator;

    @BeforeEach
    void setUp() {
        // Initialize with test configuration
        ecmProperties = new EcmIntegrationProperties();

        // Configure signature defaults
        ecmProperties.getSignature().setCustomMessage("Test custom message");
        ecmProperties.getSignature().setLanguage("es");
        ecmProperties.getSignature().setSignerRole("Approver");
        ecmProperties.getSignature().setSigningOrder(2);
        ecmProperties.getSignature().setExpirationDays(15);
        ecmProperties.getSignature().setSignatureRequired(true);

        // Configure document defaults
        ecmProperties.getDocument().setSecurityLevel("CONFIDENTIAL");
        ecmProperties.getDocument().setRetentionDays(1825);

        // Set up error handling without retry (handled by lib-ecm-core)
        ecmProperties.getErrorHandling().setFailFast(true);
        ecmProperties.getErrorHandling().setIncludeDetailedErrors(true);

        ecmDomainMapper = new EcmDomainMapper(ecmProperties);
        ecmParameterValidator = new EcmParameterValidator();
    }

    @Test
    void testMappingWithCustomDtoValues() {
        // Given: DocumentSignatureDTO with custom ECM parameters
        DocumentSignatureDTO signatureDTO = createTestSignatureDTO();
        signatureDTO.setCustomSignatureMessage("Custom message from DTO");
        signatureDTO.setSignerLanguage("fr");
        signatureDTO.setSignerRole("Reviewer");
        signatureDTO.setSigningOrder(3);
        signatureDTO.setSignatureRequired(false);

        UUID documentId = UUID.randomUUID();

        // When: Mapping to ECM domain object
        SignatureRequest ecmRequest = ecmDomainMapper.toEcmSignatureRequest(signatureDTO, documentId);

        // Then: ECM request should use DTO values, not defaults
        assertThat(ecmRequest.getCustomMessage()).isEqualTo("Custom message from DTO");
        assertThat(ecmRequest.getLanguage()).isEqualTo("fr");
        assertThat(ecmRequest.getSignerRole()).isEqualTo("Reviewer");
        assertThat(ecmRequest.getSigningOrder()).isEqualTo(3);
        assertThat(ecmRequest.getRequired()).isFalse();
        assertThat(ecmRequest.getEnvelopeId()).isEqualTo(documentId);
        assertThat(ecmRequest.getSignerEmail()).isEqualTo(signatureDTO.getSignerEmail());
        assertThat(ecmRequest.getSignerName()).isEqualTo(signatureDTO.getSignerName());
    }

    @Test
    void testMappingWithDefaultConfigurationValues() {
        // Given: DocumentSignatureDTO with minimal ECM parameters (nulls)
        DocumentSignatureDTO signatureDTO = createTestSignatureDTO();
        signatureDTO.setCustomSignatureMessage(null);
        signatureDTO.setSignerLanguage(null);
        signatureDTO.setSignerRole(null);
        signatureDTO.setSigningOrder(null);
        signatureDTO.setSignatureRequired(null);

        UUID documentId = UUID.randomUUID();

        // When: Mapping to ECM domain object
        SignatureRequest ecmRequest = ecmDomainMapper.toEcmSignatureRequest(signatureDTO, documentId);

        // Then: ECM request should use configuration defaults
        assertThat(ecmRequest.getCustomMessage()).isEqualTo("Test custom message");
        assertThat(ecmRequest.getLanguage()).isEqualTo("es");
        assertThat(ecmRequest.getSignerRole()).isEqualTo("Approver");
        assertThat(ecmRequest.getSigningOrder()).isEqualTo(2);
        assertThat(ecmRequest.getRequired()).isTrue(); // Default from config
    }

    @Test
    void testValidationWithValidParameters() {
        // Given: DocumentSignatureDTO with valid ECM parameters
        DocumentSignatureDTO signatureDTO = createTestSignatureDTO();
        signatureDTO.setSignerLanguage("en");
        signatureDTO.setSigningOrder(5);
        signatureDTO.setSignerRole("Signer");
        signatureDTO.setAuthenticationMethod("EMAIL");
        signatureDTO.setExpirationDate(LocalDateTime.now().plusDays(30));
        signatureDTO.setCustomSignatureMessage("Valid message");

        // When: Validating parameters
        List<String> errors = ecmParameterValidator.validateDocumentSignatureEcmParameters(signatureDTO);

        // Then: No validation errors
        assertThat(errors).isEmpty();
    }

    @Test
    void testValidationWithInvalidParameters() {
        // Given: DocumentSignatureDTO with invalid ECM parameters
        DocumentSignatureDTO signatureDTO = createTestSignatureDTO();
        signatureDTO.setSignerLanguage("invalid");
        signatureDTO.setSigningOrder(150); // Too high
        signatureDTO.setSignerRole("InvalidRole");
        signatureDTO.setAuthenticationMethod("INVALID_METHOD");
        signatureDTO.setExpirationDate(LocalDateTime.now().minusDays(1)); // Past date
        signatureDTO.setCustomSignatureMessage("x".repeat(1001)); // Too long

        // When: Validating parameters
        List<String> errors = ecmParameterValidator.validateDocumentSignatureEcmParameters(signatureDTO);

        // Then: Multiple validation errors
        assertThat(errors).hasSize(6);
        assertThat(errors).anyMatch(error -> error.contains("Invalid language code"));
        assertThat(errors).anyMatch(error -> error.contains("Signing order must be between 1 and 100"));
        assertThat(errors).anyMatch(error -> error.contains("Invalid signer role"));
        assertThat(errors).anyMatch(error -> error.contains("Invalid authentication method"));
        assertThat(errors).anyMatch(error -> error.contains("Expiration date cannot be in the past"));
        assertThat(errors).anyMatch(error -> error.contains("Custom signature message cannot exceed 1000 characters"));
    }

    @Test
    void testEcmResponseUpdate() {
        // Given: DocumentSignatureDTO and ECM response data
        DocumentSignatureDTO signatureDTO = createTestSignatureDTO();
        String externalSignerId = "envelope-12345";
        String signingUrl = "https://demo.docusign.net/signing/12345";

        // When: Updating DTO with ECM response
        DocumentSignatureDTO updatedDTO = ecmDomainMapper.updateWithEcmResponse(
            signatureDTO, externalSignerId, signingUrl);

        // Then: DTO should contain ECM response data
        assertThat(updatedDTO.getExternalSignerId()).isEqualTo(externalSignerId);
        assertThat(updatedDTO.getSigningUrl()).isEqualTo(signingUrl);
        assertThat(updatedDTO.getId()).isEqualTo(signatureDTO.getId()); // Original data preserved
    }

    private DocumentSignatureDTO createTestSignatureDTO() {
        return DocumentSignatureDTO.builder()
                .id(UUID.randomUUID())
                .documentId(UUID.randomUUID())
                .documentVersionId(UUID.randomUUID())
                .signatureProviderId(UUID.randomUUID())
                .signerPartyId(UUID.randomUUID())
                .signerName("John Doe")
                .signerEmail("john.doe@example.com")
                .signatureType(SignatureType.DIGITAL)
                .signatureFormat(SignatureFormat.PADES)
                .signatureStatus(SignatureStatus.PENDING)
                .signatureReason("Contract approval")
                .signatureLocation("New York, NY")
                .tenantId("test-tenant")
                .build();
    }
}