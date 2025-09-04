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


package com.firefly.commons.ecm.core.services;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.interfaces.dtos.SignatureProviderDTO;
import reactor.core.publisher.Mono;
import java.util.UUID;
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
    Mono<SignatureProviderDTO> getById(UUID id);

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
    Mono<Void> delete(UUID id);

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
    Mono<SignatureProviderDTO> setAsDefault(UUID id, String tenantId);
}
