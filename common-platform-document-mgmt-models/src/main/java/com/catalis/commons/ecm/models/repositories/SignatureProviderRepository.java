package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.entities.SignatureProvider;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Repository for managing SignatureProvider entities in the Enterprise Content Management system.
 */
@Repository
public interface SignatureProviderRepository extends BaseRepository<SignatureProvider, Long> {
    
    /**
     * Find the default signature provider for a tenant.
     *
     * @param tenantId The tenant ID
     * @return A Mono emitting the default signature provider if found, or empty if not found
     */
    Mono<SignatureProvider> findByIsDefaultTrueAndTenantId(String tenantId);
    
    /**
     * Find a signature provider by name and tenant ID.
     *
     * @param name The name of the signature provider
     * @param tenantId The tenant ID
     * @return A Mono emitting the signature provider if found, or empty if not found
     */
    Mono<SignatureProvider> findByNameAndTenantId(String name, String tenantId);
}
