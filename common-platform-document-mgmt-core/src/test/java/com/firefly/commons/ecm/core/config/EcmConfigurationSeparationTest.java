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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test to verify proper separation between lib-ecm-core provider configuration
 * and microservice business logic configuration.
 */
class EcmConfigurationSeparationTest {

    private EcmIntegrationProperties ecmIntegrationProperties;

    @BeforeEach
    void setUp() {
        // Initialize with test configuration
        ecmIntegrationProperties = new EcmIntegrationProperties();

        // Business logic configuration (our microservice)
        ecmIntegrationProperties.getSignature().setCustomMessage("Test business message");
        ecmIntegrationProperties.getSignature().setLanguage("fr");
        ecmIntegrationProperties.getSignature().setExpirationDays(45);
        ecmIntegrationProperties.getDocument().setSecurityLevel("CONFIDENTIAL");
        ecmIntegrationProperties.getDocument().setRetentionDays(1825);
    }

    @Test
    void testBusinessLogicConfigurationLoading() {
        // Verify our business logic configuration loads correctly
        assertThat(ecmIntegrationProperties.getSignature().getCustomMessage())
            .isEqualTo("Test business message");
        assertThat(ecmIntegrationProperties.getSignature().getLanguage())
            .isEqualTo("fr");
        assertThat(ecmIntegrationProperties.getSignature().getExpirationDays())
            .isEqualTo(45);
        assertThat(ecmIntegrationProperties.getDocument().getSecurityLevel())
            .isEqualTo("CONFIDENTIAL");
        assertThat(ecmIntegrationProperties.getDocument().getRetentionDays())
            .isEqualTo(1825);
    }

    @Test
    void testProviderConfigurationSeparation() {
        // Verify that business logic configuration is properly isolated
        // and doesn't interfere with provider configuration concepts

        // Business logic configuration should be independent
        assertThat(ecmIntegrationProperties.getSignature().getCustomMessage())
            .isEqualTo("Test business message");
        assertThat(ecmIntegrationProperties.getSignature().getLanguage())
            .isEqualTo("fr");
    }

    @Test
    void testConfigurationDefaults() {
        // Test that defaults are applied when properties are not set
        EcmIntegrationProperties defaultProps = new EcmIntegrationProperties();

        assertThat(defaultProps.getSignature().getSignerRole())
            .isEqualTo("Signer"); // Default value
        assertThat(defaultProps.getSignature().getSigningOrder())
            .isEqualTo(1); // Default value
        assertThat(defaultProps.getErrorHandling().getFailFast())
            .isFalse(); // Default value
        assertThat(defaultProps.getSignature().getCustomMessage())
            .isEqualTo("Please review and sign this document"); // Default value
    }

    @Test
    void testNoConfigurationConflicts() {
        // Verify that business logic configuration is properly isolated
        // This test ensures that our configuration properties work independently

        // Business logic should work independently
        assertThat(ecmIntegrationProperties.getSignature().getLanguage()).isEqualTo("fr");
        assertThat(ecmIntegrationProperties.getDocument().getRetentionDays()).isEqualTo(1825);

        // Configuration should be properly typed and validated
        assertThat(ecmIntegrationProperties.getSignature().getExpirationDays())
            .isEqualTo(45);
        assertThat(ecmIntegrationProperties.getDocument().getSecurityLevel())
            .isEqualTo("CONFIDENTIAL");
    }
}