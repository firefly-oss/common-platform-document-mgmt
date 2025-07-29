package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.interfaces.dtos.DocumentDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing documents.
 */
public interface DocumentService {
    /**
     * Filters the documents based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for DocumentDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of documents
     */
    Mono<PaginationResponse<DocumentDTO>> filterDocuments(FilterRequest<DocumentDTO> filterRequest);
    
    /**
     * Creates a new document based on the provided information.
     *
     * @param documentDTO the DTO object containing details of the document to be created
     * @return a Mono that emits the created DocumentDTO object
     */
    Mono<DocumentDTO> createDocument(DocumentDTO documentDTO);
    
    /**
     * Updates an existing document with updated information.
     *
     * @param documentId the unique identifier of the document to be updated
     * @param documentDTO the data transfer object containing the updated details of the document
     * @return a reactive Mono containing the updated DocumentDTO
     */
    Mono<DocumentDTO> updateDocument(Long documentId, DocumentDTO documentDTO);
    
    /**
     * Deletes a document identified by its unique ID.
     *
     * @param documentId the unique identifier of the document to be deleted
     * @return a Mono that completes when the document is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deleteDocument(Long documentId);
    
    /**
     * Retrieves a document by its unique identifier.
     *
     * @param documentId the unique identifier of the document to retrieve
     * @return a Mono emitting the {@link DocumentDTO} representing the document if found,
     *         or an empty Mono if the document does not exist
     */
    Mono<DocumentDTO> getDocumentById(Long documentId);
}