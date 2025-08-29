package com.firefly.commons.ecm.core.services;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.interfaces.dtos.DocumentVersionDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing DocumentVersion entities in the Enterprise Content Management system.
 */
public interface DocumentVersionService {

    /**
     * Get a document version by its ID.
     *
     * @param id The document version ID
     * @return A Mono emitting the document version if found, or empty if not found
     */
    Mono<DocumentVersionDTO> getById(Long id);

    /**
     * Filter document versions based on the provided filter request.
     *
     * @param filterRequest The filter request containing filtering and pagination parameters
     * @return A Mono emitting a pagination response with the filtered document versions
     */
    Mono<PaginationResponse<DocumentVersionDTO>> filter(FilterRequest<DocumentVersionDTO> filterRequest);

    /**
     * Update an existing document version.
     *
     * @param documentVersion The document version to update
     * @return A Mono emitting the updated document version
     */
    Mono<DocumentVersionDTO> update(DocumentVersionDTO documentVersion);

    /**
     * Create a new document version.
     *
     * @param documentVersion The document version to create
     * @return A Mono emitting the created document version
     */
    Mono<DocumentVersionDTO> create(DocumentVersionDTO documentVersion);

    /**
     * Delete a document version by its ID.
     *
     * @param id The ID of the document version to delete
     * @return A Mono completing when the document version is deleted
     */
    Mono<Void> delete(Long id);
}
