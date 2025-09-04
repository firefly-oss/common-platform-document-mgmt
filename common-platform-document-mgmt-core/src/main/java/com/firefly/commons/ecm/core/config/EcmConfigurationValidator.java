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


package com.firefly.commons.ecm.core.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Validates ECM configuration separation and provides warnings for potential conflicts.
 * Ensures proper separation between lib-ecm-core provider configuration and
 * microservice business logic configuration.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EcmConfigurationValidator {

    private final Environment environment;
    private final EcmIntegrationProperties ecmIntegrationProperties;

    /**
     * Validates configuration after application startup.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void validateConfiguration() {
        log.info("Validating ECM configuration separation...");

        List<String> warnings = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        // Validate configuration separation
        validateConfigurationSeparation(warnings, errors);

        // Validate business logic defaults
        validateBusinessLogicDefaults(warnings, errors);

        // Log results
        if (!warnings.isEmpty()) {
            log.warn("ECM Configuration Warnings:");
            warnings.forEach(warning -> log.warn("  - {}", warning));
        }

        if (!errors.isEmpty()) {
            log.error("ECM Configuration Errors:");
            errors.forEach(error -> log.error("  - {}", error));
            throw new IllegalStateException("ECM configuration validation failed. See errors above.");
        }

        if (warnings.isEmpty() && errors.isEmpty()) {
            log.info("ECM configuration validation passed successfully");
        }
    }

    /**
     * Validates that configuration is properly separated between lib-ecm-core and business logic.
     */
    private void validateConfigurationSeparation(List<String> warnings, List<String> errors) {
        // Check for direct conflicts with lib-ecm-core EcmProperties
        String[] directConflicts = {
            "firefly.ecm.integration.connection.retry-attempts", // Conflicts with lib-ecm-core connection.retryAttempts
            "firefly.ecm.integration.defaults.max-file-size-mb", // Conflicts with lib-ecm-core defaults.maxFileSizeMb
            "firefly.ecm.integration.defaults.allowed-extensions", // Conflicts with lib-ecm-core defaults.allowedExtensions
            "firefly.ecm.integration.defaults.default-folder", // Conflicts with lib-ecm-core defaults.defaultFolder
            "firefly.ecm.integration.performance.batch-size", // Conflicts with lib-ecm-core performance.batchSize
            "firefly.ecm.integration.performance.cache-enabled" // Conflicts with lib-ecm-core performance.cacheEnabled
        };

        for (String conflictProperty : directConflicts) {
            if (environment.containsProperty(conflictProperty)) {
                errors.add("Property '" + conflictProperty + "' conflicts with lib-ecm-core EcmProperties. " +
                          "Remove this property as it's handled by lib-ecm-core under 'firefly.ecm' prefix");
            }
        }

        // Check for business logic properties accidentally placed under lib-ecm-core prefix
        String[] misplacedBusinessProperties = {
            "firefly.ecm.signature.custom-message",
            "firefly.ecm.signature.language",
            "firefly.ecm.signature.signer-role",
            "firefly.ecm.document.security-level",
            "firefly.ecm.document.retention-days"
        };

        for (String misplacedProperty : misplacedBusinessProperties) {
            if (environment.containsProperty(misplacedProperty)) {
                warnings.add("Business logic property '" + misplacedProperty + "' should be under " +
                           "'firefly.ecm.integration' prefix to separate from lib-ecm-core provider configuration");
            }
        }

        // Validate that integration properties are properly configured
        if (!environment.containsProperty("firefly.ecm.integration.signature.custom-message")) {
            warnings.add("No custom signature message configured. Using default: '" +
                        ecmIntegrationProperties.getSignature().getCustomMessage() + "'");
        }

        // Check for proper lib-ecm-core configuration
        boolean hasEcmEnabled = environment.containsProperty("firefly.ecm.enabled");
        boolean hasAdapterType = environment.containsProperty("firefly.ecm.adapter-type");

        if (!hasEcmEnabled) {
            warnings.add("lib-ecm-core 'firefly.ecm.enabled' not configured. ECM functionality may be disabled.");
        }

        if (!hasAdapterType) {
            warnings.add("lib-ecm-core 'firefly.ecm.adapter-type' not configured. No ECM adapter will be selected.");
        }
    }

    /**
     * Validates business logic default values.
     */
    private void validateBusinessLogicDefaults(List<String> warnings, List<String> errors) {
        var signature = ecmIntegrationProperties.getSignature();
        var document = ecmIntegrationProperties.getDocument();
        var errorHandling = ecmIntegrationProperties.getErrorHandling();

        // Validate signature defaults
        if (signature.getExpirationDays() <= 0) {
            errors.add("Signature expiration days must be positive, got: " + signature.getExpirationDays());
        }

        if (signature.getExpirationDays() > 365) {
            warnings.add("Signature expiration days is very long: " + signature.getExpirationDays() +
                        " days. Consider shorter periods for security.");
        }

        if (signature.getSigningOrder() <= 0) {
            errors.add("Signing order must be positive, got: " + signature.getSigningOrder());
        }

        // Validate document defaults
        if (document.getRetentionDays() <= 0) {
            errors.add("Document retention days must be positive, got: " + document.getRetentionDays());
        }

        // Validate error handling (retry logic is handled by lib-ecm-core)
        if (errorHandling.getFailFast() == null) {
            warnings.add("Error handling fail-fast setting not configured. Using default: " +
                        errorHandling.getFailFast());
        }
    }

    /**
     * Provides configuration summary for debugging.
     */
    public void logConfigurationSummary() {
        log.info("ECM Configuration Summary:");
        log.info("  Business Logic Defaults (firefly.ecm.integration):");
        log.info("    Signature Message: '{}'", ecmIntegrationProperties.getSignature().getCustomMessage());
        log.info("    Default Language: '{}'", ecmIntegrationProperties.getSignature().getLanguage());
        log.info("    Default Signer Role: '{}'", ecmIntegrationProperties.getSignature().getSignerRole());
        log.info("    Expiration Days: {}", ecmIntegrationProperties.getSignature().getExpirationDays());
        log.info("    Document Security Level: '{}'", ecmIntegrationProperties.getDocument().getSecurityLevel());
        log.info("    Document Retention Days: {}", ecmIntegrationProperties.getDocument().getRetentionDays());
        log.info("    Error Handling - Fail Fast: {}", ecmIntegrationProperties.getErrorHandling().getFailFast());
        log.info("    Error Handling - Log Failures: {}", ecmIntegrationProperties.getErrorHandling().getLogFailures());
        log.info("    Error Handling - Include Detailed Errors: {}", ecmIntegrationProperties.getErrorHandling().getIncludeDetailedErrors());

        // Check for provider configuration (without exposing sensitive data)
        boolean hasDocumentProvider = environment.containsProperty("firefly.ecm.document-content.provider");
        boolean hasSignatureProvider = environment.containsProperty("firefly.ecm.signature.provider");

        log.info("  Provider Configuration (firefly.ecm):");
        log.info("    Document Content Provider: {}", hasDocumentProvider ? "Configured" : "Not configured");
        log.info("    Signature Provider: {}", hasSignatureProvider ? "Configured" : "Not configured");
    }
}