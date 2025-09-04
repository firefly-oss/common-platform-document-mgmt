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
import com.firefly.commons.ecm.interfaces.dtos.DocumentMetadataDTO;
import reactor.core.publisher.Mono;
import java.util.UUID;
/**
 * Service interface for managing DocumentMetadata entities in the Enterprise Content Management system.
 */
public interface DocumentMetadataService {

    /**
     * Get a document metadata by its ID.
     *
     * @param id The document metadata ID
     * @return A Mono emitting the document metadata if found, or empty if not found
     */
    Mono<DocumentMetadataDTO> getById(UUID id);

    /**
     * Filter document metadata based on the provided filter request.
     *
     * @param filterRequest The filter request containing filtering and pagination parameters
     * @return A Mono emitting a pagination response with the filtered document metadata
     */
    Mono<PaginationResponse<DocumentMetadataDTO>> filter(FilterRequest<DocumentMetadataDTO> filterRequest);

    /**
     * Update an existing document metadata.
     *
     * @param documentMetadata The document metadata to update
     * @return A Mono emitting the updated document metadata
     */
    Mono<DocumentMetadataDTO> update(DocumentMetadataDTO documentMetadata);

    /**
     * Create a new document metadata.
     *
     * @param documentMetadata The document metadata to create
     * @return A Mono emitting the created document metadata
     */
    Mono<DocumentMetadataDTO> create(DocumentMetadataDTO documentMetadata);

    /**
     * Delete a document metadata by its ID.
     *
     * @param id The ID of the document metadata to delete
     * @return A Mono completing when the document metadata is deleted
     */
    Mono<Void> delete(UUID id);
}
