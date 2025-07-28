package com.catalis.commons.ecm.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for CMIS.
 * Maps to the 'cmis' section in application.yaml.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cmis")
public class CmisProperties {

    /**
     * Repository configuration.
     */
    private Repository repository = new Repository();

    /**
     * Capabilities configuration.
     */
    private Capabilities capabilities = new Capabilities();

    /**
     * Repository configuration properties.
     */
    @Data
    public static class Repository {
        private String id;
        private String name;
        private String description;
        private String vendorName;
        private String productName;
        private String productVersion;
        private String cmisVersion;
    }

    /**
     * Capabilities configuration properties.
     */
    @Data
    public static class Capabilities {
        private Boolean contentStreamUpdatability;
        private Boolean changesCapability;
        private Boolean renditionsCapability;
        private Boolean getDescendantsSupported;
        private Boolean getFolderTreeSupported;
        private Boolean multifilingSupported;
        private Boolean unfilingSupported;
        private Boolean versionSpecificFilingSupported;
        private Boolean pwcUpdatableSupported;
        private Boolean pwcSearchableSupported;
        private Boolean allVersionsSearchableSupported;
        private Boolean querySupported;
        private Boolean joinSupported;
        private Boolean aclSupported;
    }
}