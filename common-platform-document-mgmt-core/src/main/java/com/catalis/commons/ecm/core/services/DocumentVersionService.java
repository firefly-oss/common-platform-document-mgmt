package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.models.entities.DocumentVersion;
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
    Mono<DocumentVersion> getById(Long id);

    /**
     * Filter document versions based on the provided filter request.
     *
     * @param filterRequest The filter request containing filtering and pagination parameters
     * @return A Mono emitting a pagination response with the filtered document versions
     */
    Mono<PaginationResponse<DocumentVersion>> filter(FilterRequest<DocumentVersion> filterRequest);

    /**
     * Update an existing document version.
     *
     * @param documentVersion The document version to update
     * @return A Mono emitting the updated document version
     */
    Mono<DocumentVersion> update(DocumentVersion documentVersion);

    /**
     * Create a new document version.
     *
     * @param documentVersion The document version to create
     * @return A Mono emitting the created document version
     */
    Mono<DocumentVersion> create(DocumentVersion documentVersion);

    /**
     * Delete a document version by its ID.
     *
     * @param id The ID of the document version to delete
     * @return A Mono completing when the document version is deleted
     */
    Mono<Void> delete(Long id);
}
