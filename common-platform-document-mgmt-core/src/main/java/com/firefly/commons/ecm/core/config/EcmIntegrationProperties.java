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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Configuration properties for ECM integration defaults and business logic behavior.
 *
 * This class provides default values for business logic when not specified in API requests.
 * It is separate from lib-ecm-core EcmProperties which handles ECM provider settings.
 *
 * SEPARATION OF CONCERNS:
 *
 * lib-ecm-core EcmProperties (firefly.ecm.*):
 * - Provider configuration (adapterType, connection settings)
 * - Technical defaults (maxFileSizeMb, allowedExtensions, connectTimeout)
 * - Feature toggles (documentManagement, esignature, versioning)
 * - Performance settings (batchSize, cacheEnabled, compressionEnabled)
 *
 * This class EcmIntegrationProperties (firefly.ecm.integration.*):
 * - Business logic defaults (custom messages, signer roles, languages)
 * - Document workflow settings (security levels, retention policies)
 * - Application-level error handling (fail-fast behavior, logging preferences)
 * - User experience defaults (signing order, expiration preferences)
 */
@Data
@Component
@ConfigurationProperties(prefix = "firefly.ecm.integration")
public class EcmIntegrationProperties {

    /**
     * Default signature request configuration
     */
    private SignatureDefaults signature = new SignatureDefaults();

    /**
     * Default document processing configuration
     */
    private DocumentDefaults document = new DocumentDefaults();

    /**
     * Error handling and retry configuration
     */
    private ErrorHandling errorHandling = new ErrorHandling();

    @Data
    public static class SignatureDefaults {
        /**
         * Default custom message for signature requests
         */
        private String customMessage = "Please review and sign this document";

        /**
         * Default language for signature requests
         */
        private String language = "en";

        /**
         * Default time zone for signature requests
         */
        private String timeZone = "UTC";

        /**
         * Default signer role
         */
        private String signerRole = "Signer";

        /**
         * Default signing order for single signer
         */
        private Integer signingOrder = 1;

        /**
         * Default signature requirement flag
         */
        private Boolean signatureRequired = true;

        /**
         * Default authentication method
         */
        private String authenticationMethod = "EMAIL";

        /**
         * Default expiration duration for signature requests (in days)
         */
        private Integer expirationDays = 30;

        /**
         * Whether to send automatic reminders
         */
        private Boolean sendReminders = true;

        /**
         * Reminder interval in days
         */
        private Integer reminderIntervalDays = 7;
    }

    @Data
    public static class DocumentDefaults {
        /**
         * Default security level for documents when not specified in API requests
         */
        private String securityLevel = "INTERNAL";

        /**
         * Default document type when not specified in API requests
         */
        private String documentType = "DOCUMENT";

        /**
         * Default retention period in days for documents
         */
        private Integer retentionDays = 2555; // 7 years default
    }

    @Data
    public static class ErrorHandling {
        /**
         * Whether to fail fast on ECM operation errors at the business logic level.
         * Note: Technical retry logic is handled by lib-ecm-core connection settings.
         */
        private Boolean failFast = false;

        /**
         * Whether to log ECM operation failures for business logic tracking
         */
        private Boolean logFailures = true;

        /**
         * Whether to include detailed error information in API responses
         */
        private Boolean includeDetailedErrors = false;

        /**
         * Whether to send error notifications to administrators
         */
        private Boolean sendErrorNotifications = false;
    }
}