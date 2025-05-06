package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.interfaces.dtos.DocumentMetadataDTO;
import reactor.core.publisher.Mono;

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
    Mono<DocumentMetadataDTO> getById(Long id);

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
    Mono<Void> delete(Long id);
}
