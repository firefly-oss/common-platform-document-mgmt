package com.firefly.commons.ecm.core.services;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.interfaces.dtos.SignatureProviderDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing SignatureProvider entities in the Enterprise Content Management system.
 */
public interface SignatureProviderService {

    /**
     * Get a signature provider by its ID.
     *
     * @param id The signature provider ID
     * @return A Mono emitting the signature provider if found, or empty if not found
     */
    Mono<SignatureProviderDTO> getById(Long id);

    /**
     * Filter signature providers based on the provided filter request.
     *
     * @param filterRequest The filter request containing filtering and pagination parameters
     * @return A Mono emitting a pagination response with the filtered signature providers
     */
    Mono<PaginationResponse<SignatureProviderDTO>> filter(FilterRequest<SignatureProviderDTO> filterRequest);

    /**
     * Update an existing signature provider.
     *
     * @param signatureProvider The signature provider to update
     * @return A Mono emitting the updated signature provider
     */
    Mono<SignatureProviderDTO> update(SignatureProviderDTO signatureProvider);

    /**
     * Create a new signature provider.
     *
     * @param signatureProvider The signature provider to create
     * @return A Mono emitting the created signature provider
     */
    Mono<SignatureProviderDTO> create(SignatureProviderDTO signatureProvider);

    /**
     * Delete a signature provider by its ID.
     *
     * @param id The ID of the signature provider to delete
     * @return A Mono completing when the signature provider is deleted
     */
    Mono<Void> delete(Long id);

    /**
     * Get the default signature provider for a tenant.
     *
     * @param tenantId The tenant ID
     * @return A Mono emitting the default signature provider if found, or empty if not found
     */
    Mono<SignatureProviderDTO> getDefaultProvider(String tenantId);

    /**
     * Set a signature provider as the default for a tenant.
     *
     * @param id The ID of the signature provider to set as default
     * @param tenantId The tenant ID
     * @return A Mono emitting the updated signature provider
     */
    Mono<SignatureProviderDTO> setAsDefault(Long id, String tenantId);
}
