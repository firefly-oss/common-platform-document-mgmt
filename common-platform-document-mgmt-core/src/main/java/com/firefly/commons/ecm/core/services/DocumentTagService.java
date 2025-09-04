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
import com.firefly.commons.ecm.interfaces.dtos.DocumentTagDTO;
import reactor.core.publisher.Mono;
import java.util.UUID;
/**
 * Service interface for managing DocumentTag entities in the Enterprise Content Management system.
 */
public interface DocumentTagService {

    /**
     * Get a document tag by its ID.
     *
     * @param id The document tag ID
     * @return A Mono emitting the document tag if found, or empty if not found
     */
    Mono<DocumentTagDTO> getById(UUID id);

    /**
     * Filter document tags based on the provided filter request.
     *
     * @param filterRequest The filter request containing filtering and pagination parameters
     * @return A Mono emitting a pagination response with the filtered document tags
     */
    Mono<PaginationResponse<DocumentTagDTO>> filter(FilterRequest<DocumentTagDTO> filterRequest);

    /**
     * Update an existing document tag.
     *
     * @param documentTag The document tag to update
     * @return A Mono emitting the updated document tag
     */
    Mono<DocumentTagDTO> update(DocumentTagDTO documentTag);

    /**
     * Create a new document tag.
     *
     * @param documentTag The document tag to create
     * @return A Mono emitting the created document tag
     */
    Mono<DocumentTagDTO> create(DocumentTagDTO documentTag);

    /**
     * Delete a document tag by its ID.
     *
     * @param id The ID of the document tag to delete
     * @return A Mono completing when the document tag is deleted
     */
    Mono<Void> delete(UUID id);
}
