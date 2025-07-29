package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.interfaces.dtos.DocumentVersionDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing document versions.
 */
public interface DocumentVersionService {
    /**
     * Filters the document versions based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for DocumentVersionDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of document versions
     */
    Mono<PaginationResponse<DocumentVersionDTO>> filterDocumentVersions(FilterRequest<DocumentVersionDTO> filterRequest);
    
    /**
     * Creates a new document version based on the provided information.
     *
     * @param documentVersionDTO the DTO object containing details of the document version to be created
     * @return a Mono that emits the created DocumentVersionDTO object
     */
    Mono<DocumentVersionDTO> createDocumentVersion(DocumentVersionDTO documentVersionDTO);
    
    /**
     * Updates an existing document version with updated information.
     *
     * @param documentVersionId the unique identifier of the document version to be updated
     * @param documentVersionDTO the data transfer object containing the updated details of the document version
     * @return a reactive Mono containing the updated DocumentVersionDTO
     */
    Mono<DocumentVersionDTO> updateDocumentVersion(Long documentVersionId, DocumentVersionDTO documentVersionDTO);
    
    /**
     * Deletes a document version identified by its unique ID.
     *
     * @param documentVersionId the unique identifier of the document version to be deleted
     * @return a Mono that completes when the document version is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deleteDocumentVersion(Long documentVersionId);
    
    /**
     * Retrieves a document version by its unique identifier.
     *
     * @param documentVersionId the unique identifier of the document version to retrieve
     * @return a Mono emitting the {@link DocumentVersionDTO} representing the document version if found,
     *         or an empty Mono if the document version does not exist
     */
    Mono<DocumentVersionDTO> getDocumentVersionById(Long documentVersionId);
}