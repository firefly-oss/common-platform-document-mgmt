package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.interfaces.dtos.DocumentReferenceDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing document references.
 */
public interface DocumentReferenceService {
    /**
     * Filters the document references based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for DocumentReferenceDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of document references
     */
    Mono<PaginationResponse<DocumentReferenceDTO>> filterDocumentReferences(FilterRequest<DocumentReferenceDTO> filterRequest);
    
    /**
     * Creates a new document reference based on the provided information.
     *
     * @param documentReferenceDTO the DTO object containing details of the document reference to be created
     * @return a Mono that emits the created DocumentReferenceDTO object
     */
    Mono<DocumentReferenceDTO> createDocumentReference(DocumentReferenceDTO documentReferenceDTO);
    
    /**
     * Updates an existing document reference with updated information.
     *
     * @param documentReferenceId the unique identifier of the document reference to be updated
     * @param documentReferenceDTO the data transfer object containing the updated details of the document reference
     * @return a reactive Mono containing the updated DocumentReferenceDTO
     */
    Mono<DocumentReferenceDTO> updateDocumentReference(Long documentReferenceId, DocumentReferenceDTO documentReferenceDTO);
    
    /**
     * Deletes a document reference identified by its unique ID.
     *
     * @param documentReferenceId the unique identifier of the document reference to be deleted
     * @return a Mono that completes when the document reference is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deleteDocumentReference(Long documentReferenceId);
    
    /**
     * Retrieves a document reference by its unique identifier.
     *
     * @param documentReferenceId the unique identifier of the document reference to retrieve
     * @return a Mono emitting the {@link DocumentReferenceDTO} representing the document reference if found,
     *         or an empty Mono if the document reference does not exist
     */
    Mono<DocumentReferenceDTO> getDocumentReferenceById(Long documentReferenceId);
}