package com.catalis.commons.ecm.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

/**
 * Configuration properties for e-signature providers.
 */
@Configuration
@ConfigurationProperties(prefix = "esignature")
@Validated
public class ESignatureProviderConfig {

    private String defaultProvider;
    private LogaltyConfig logalty;

    public String getDefaultProvider() {
        return defaultProvider;
    }

    public void setDefaultProvider(String defaultProvider) {
        this.defaultProvider = defaultProvider;
    }

    public LogaltyConfig getLogalty() {
        return logalty;
    }

    public void setLogalty(LogaltyConfig logalty) {
        this.logalty = logalty;
    }

    /**
     * Configuration properties for Logalty e-signature provider.
     */
    public static class LogaltyConfig {
        @NotBlank
        private String apiUrl;
        
        @NotBlank
        private String apiKey;
        
        @NotBlank
        private String apiSecret;

        public String getApiUrl() {
            return apiUrl;
        }

        public void setApiUrl(String apiUrl) {
            this.apiUrl = apiUrl;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getApiSecret() {
            return apiSecret;
        }

        public void setApiSecret(String apiSecret) {
            this.apiSecret = apiSecret;
        }
    }
}