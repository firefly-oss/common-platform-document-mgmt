package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.models.entities.DocumentTag;
import reactor.core.publisher.Mono;

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
    Mono<DocumentTag> getById(Long id);

    /**
     * Filter document tags based on the provided filter request.
     *
     * @param filterRequest The filter request containing filtering and pagination parameters
     * @return A Mono emitting a pagination response with the filtered document tags
     */
    Mono<PaginationResponse<DocumentTag>> filter(FilterRequest<DocumentTag> filterRequest);

    /**
     * Update an existing document tag.
     *
     * @param documentTag The document tag to update
     * @return A Mono emitting the updated document tag
     */
    Mono<DocumentTag> update(DocumentTag documentTag);

    /**
     * Create a new document tag.
     *
     * @param documentTag The document tag to create
     * @return A Mono emitting the created document tag
     */
    Mono<DocumentTag> create(DocumentTag documentTag);

    /**
     * Delete a document tag by its ID.
     *
     * @param id The ID of the document tag to delete
     * @return A Mono completing when the document tag is deleted
     */
    Mono<Void> delete(Long id);
}
