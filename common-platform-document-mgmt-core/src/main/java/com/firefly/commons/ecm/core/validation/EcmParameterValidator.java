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


package com.firefly.commons.ecm.core.validation;

import com.firefly.commons.ecm.interfaces.dtos.DocumentSignatureDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Validator for ECM-specific parameters in DTOs.
 * Ensures data integrity and proper error handling for ECM integration.
 */
@Slf4j
@Component
public class EcmParameterValidator {

    /**
     * Validates ECM parameters in DocumentSignatureDTO.
     *
     * @param signatureDTO The DTO to validate
     * @return List of validation errors (empty if valid)
     */
    public List<String> validateDocumentSignatureEcmParameters(DocumentSignatureDTO signatureDTO) {
        List<String> errors = new ArrayList<>();

        // Validate language code
        if (signatureDTO.getSignerLanguage() != null) {
            if (!isValidLanguageCode(signatureDTO.getSignerLanguage())) {
                errors.add("Invalid language code: " + signatureDTO.getSignerLanguage() +
                          ". Must be a valid ISO 639-1 language code (e.g., 'en', 'es', 'fr')");
            }
        }

        // Validate signing order
        if (signatureDTO.getSigningOrder() != null) {
            if (signatureDTO.getSigningOrder() < 1 || signatureDTO.getSigningOrder() > 100) {
                errors.add("Signing order must be between 1 and 100, got: " + signatureDTO.getSigningOrder());
            }
        }

        // Validate signer role
        if (signatureDTO.getSignerRole() != null) {
            if (!isValidSignerRole(signatureDTO.getSignerRole())) {
                errors.add("Invalid signer role: " + signatureDTO.getSignerRole() +
                          ". Must be one of: Signer, Approver, Reviewer, Witness, Notary");
            }
        }

        // Validate authentication method
        if (signatureDTO.getAuthenticationMethod() != null) {
            if (!isValidAuthenticationMethod(signatureDTO.getAuthenticationMethod())) {
                errors.add("Invalid authentication method: " + signatureDTO.getAuthenticationMethod() +
                          ". Must be one of: EMAIL, SMS, PHONE, ACCESS_CODE, ID_CHECK");
            }
        }

        // Validate expiration date
        if (signatureDTO.getExpirationDate() != null) {
            if (signatureDTO.getExpirationDate().isBefore(LocalDateTime.now())) {
                errors.add("Expiration date cannot be in the past: " + signatureDTO.getExpirationDate());
            }
            if (signatureDTO.getExpirationDate().isAfter(LocalDateTime.now().plusYears(1))) {
                errors.add("Expiration date cannot be more than 1 year in the future: " + signatureDTO.getExpirationDate());
            }
        }

        // Validate custom message length
        if (signatureDTO.getCustomSignatureMessage() != null) {
            if (signatureDTO.getCustomSignatureMessage().length() > 1000) {
                errors.add("Custom signature message cannot exceed 1000 characters, got: " +
                          signatureDTO.getCustomSignatureMessage().length());
            }
        }

        // Validate ECM metadata
        if (signatureDTO.getEcmMetadata() != null) {
            if (signatureDTO.getEcmMetadata().length() > 5000) {
                errors.add("ECM metadata cannot exceed 5000 characters, got: " +
                          signatureDTO.getEcmMetadata().length());
            }
        }

        if (!errors.isEmpty()) {
            log.warn("ECM parameter validation failed for signature DTO: {}", errors);
        }

        return errors;
    }

    /**
     * Validates if the language code is a valid ISO 639-1 code.
     */
    private boolean isValidLanguageCode(String languageCode) {
        if (languageCode == null || languageCode.length() != 2) {
            return false;
        }

        try {
            Locale locale = new Locale(languageCode.toLowerCase());
            return locale.getISO3Language() != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validates if the signer role is valid.
     */
    private boolean isValidSignerRole(String signerRole) {
        return List.of("Signer", "Approver", "Reviewer", "Witness", "Notary", "CC")
                   .contains(signerRole);
    }

    /**
     * Validates if the authentication method is valid.
     */
    private boolean isValidAuthenticationMethod(String authMethod) {
        return List.of("EMAIL", "SMS", "PHONE", "ACCESS_CODE", "ID_CHECK", "NONE")
                   .contains(authMethod);
    }
}