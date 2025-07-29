package com.catalis.commons.ecm.core.providers.esignature;

import com.catalis.commons.ecm.core.config.ESignatureProviderConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Registry for e-signature providers.
 * This class manages the available e-signature providers and provides methods to retrieve them.
 */
@Component
public class ESignatureProviderRegistry {

    private static final Logger log = LoggerFactory.getLogger(ESignatureProviderRegistry.class);
    
    private final Map<String, ESignatureProvider> providers = new HashMap<>();
    private final ESignatureProviderConfig config;

    /**
     * Constructor for ESignatureProviderRegistry.
     *
     * @param providers the list of available e-signature providers
     * @param config the configuration for e-signature providers
     */
    public ESignatureProviderRegistry(List<ESignatureProvider> providers, ESignatureProviderConfig config) {
        this.config = config;
        
        for (ESignatureProvider provider : providers) {
            this.providers.put(provider.getProviderName().toLowerCase(), provider);
            log.info("Registered e-signature provider: {}", provider.getProviderName());
        }
    }

    /**
     * Gets the default e-signature provider as configured in the application properties.
     *
     * @return the default e-signature provider
     * @throws IllegalStateException if the default provider is not configured or not found
     */
    public ESignatureProvider getDefaultProvider() {
        String defaultProviderName = Optional.ofNullable(config.getDefaultProvider())
                .orElseThrow(() -> new IllegalStateException("Default e-signature provider not configured"));
        
        return getProvider(defaultProviderName);
    }

    /**
     * Gets an e-signature provider by name.
     *
     * @param providerName the name of the provider (case-insensitive)
     * @return the e-signature provider
     * @throws IllegalArgumentException if the provider is not found
     */
    public ESignatureProvider getProvider(String providerName) {
        ESignatureProvider provider = providers.get(providerName.toLowerCase());
        
        if (provider == null) {
            throw new IllegalArgumentException("E-signature provider not found: " + providerName);
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