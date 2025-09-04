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


package com.firefly.commons.ecm.models.repositories;

import com.firefly.commons.ecm.models.entities.SignatureProvider;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for managing SignatureProvider entities in the Enterprise Content Management system.
 */
@Repository
public interface SignatureProviderRepository extends BaseRepository<SignatureProvider, UUID> {
    
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
