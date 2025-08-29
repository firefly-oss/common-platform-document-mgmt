package com.firefly.commons.ecm.core.services;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.interfaces.dtos.DocumentDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing Document entities in the Enterprise Content Management system.
 */
public interface DocumentService {

    /**
     * Get a document by its ID.
     *
     * @param id The document ID
     * @return A Mono emitting the document if found, or empty if not found
     */
    Mono<DocumentDTO> getById(Long id);

    /**
     * Filter documents based on the provided filter request.
     *
     * @param filterRequest The filter request containing filtering and pagination parameters
     * @return A Mono emitting a pagination response with the filtered documents
     */
    Mono<PaginationResponse<DocumentDTO>> filter(FilterRequest<DocumentDTO> filterRequest);

    /**
     * Update an existing document.
     *
     * @param document The document to update
     * @return A Mono emitting the updated document
     */
    Mono<DocumentDTO> update(DocumentDTO document);

    /**
     * Create a new document.
     *
     * @param document The document to create
     * @return A Mono emitting the created document
     */
    Mono<DocumentDTO> create(DocumentDTO document);

    /**
     * Delete a document by its ID.
     *
     * @param id The ID of the document to delete
     * @return A Mono completing when the document is deleted
     */
    Mono<Void> delete(Long id);
}
