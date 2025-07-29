package com.catalis.commons.ecm.core.providers.storage;

import com.catalis.commons.ecm.core.config.FileStorageProviderConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Registry for file storage providers.
 * This class manages the available file storage providers and provides methods to retrieve them.
 */
@Component
public class FileStorageProviderRegistry {

    private static final Logger log = LoggerFactory.getLogger(FileStorageProviderRegistry.class);
    
    private final Map<String, FileStorageProvider> providers = new HashMap<>();
    private final FileStorageProviderConfig config;

    /**
     * Constructor for FileStorageProviderRegistry.
     *
     * @param providers the list of available file storage providers
     * @param config the configuration for file storage providers
     */
    public FileStorageProviderRegistry(List<FileStorageProvider> providers, FileStorageProviderConfig config) {
        this.config = config;
        
        for (FileStorageProvider provider : providers) {
            this.providers.put(provider.getProviderName().toLowerCase(), provider);
            log.info("Registered file storage provider: {}", provider.getProviderName());
        }
    }

    /**
     * Gets the default file storage provider as configured in the application properties.
     *
     * @return the default file storage provider
     * @throws IllegalStateException if the default provider is not configured or not found
     */
    public FileStorageProvider getDefaultProvider() {
        String defaultProviderName = Optional.ofNullable(config.getDefaultProvider())
                .orElseThrow(() -> new IllegalStateException("Default file storage provider not configured"));
        
        return getProvider(defaultProviderName);
    }

    /**
     * Gets a file storage provider by name.
     *
     * @param providerName the name of the provider (case-insensitive)
     * @return the file storage provider
     * @throws IllegalArgumentException if the provider is not found
     */
    public FileStorageProvider getProvider(String providerName) {
        FileStorageProvider provider = providers.get(providerName.toLowerCase());
        
        if (provider == null) {
            throw new IllegalArgumentException("File storage provider not found: " + providerName);
        }
        
        return provider;
    }

    /**
     * Checks if a provider with the given name exists.
     *
     * @param providerName the name of the provider (case-insensitive)
     * @return true if the provider exists, false otherwise
     */
    public boolean hasProvider(String providerName) {
        return providers.containsKey(providerName.toLowerCase());
    }

    /**
     * Gets the names of all registered providers.
     *
     * @return the names of all registered providers
     */
    public List<String> getProviderNames() {
        return providers.keySet().stream().toList();
    }
}