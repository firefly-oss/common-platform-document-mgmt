package com.catalis.commons.ecm.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

/**
 * Configuration properties for file storage providers.
 */
@Configuration
@ConfigurationProperties(prefix = "storage")
@Validated
public class FileStorageProviderConfig {

    private String defaultProvider;
    private S3Config s3;

    public String getDefaultProvider() {
        return defaultProvider;
    }

    public void setDefaultProvider(String defaultProvider) {
        this.defaultProvider = defaultProvider;
    }

    public S3Config getS3() {
        return s3;
    }

    public void setS3(S3Config s3) {
        this.s3 = s3;
    }

    /**
     * Configuration properties for Amazon S3 file storage provider.
     */
    public static class S3Config {
        @NotBlank
        private String endpoint;
        
        @NotBlank
        private String region;
        
        @NotBlank
        private String accessKey;
        
        @NotBlank
        private String secretKey;
        
        @NotBlank
        private String privateBucketName;
        
        @NotBlank
        private String publicBucketName;

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getPrivateBucketName() {
            return privateBucketName;
        }

        public void setPrivateBucketName(String privateBucketName) {
            this.privateBucketName = privateBucketName;
        }
        
        public String getPublicBucketName() {
            return publicBucketName;
        }

        public void setPublicBucketName(String publicBucketName) {
            this.publicBucketName = publicBucketName;
        }
    }
}