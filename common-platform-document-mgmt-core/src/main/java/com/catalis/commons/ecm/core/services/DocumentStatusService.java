package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.interfaces.dtos.DocumentStatusDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing document statuses.
 */
public interface DocumentStatusService {
    /**
     * Filters the document statuses based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for DocumentStatusDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of document statuses
     */
    Mono<PaginationResponse<DocumentStatusDTO>> filterDocumentStatuses(FilterRequest<DocumentStatusDTO> filterRequest);
    
    /**
     * Creates a new document status based on the provided information.
     *
     * @param documentStatusDTO the DTO object containing details of the document status to be created
     * @return a Mono that emits the created DocumentStatusDTO object
     */
    Mono<DocumentStatusDTO> createDocumentStatus(DocumentStatusDTO documentStatusDTO);
    
    /**
     * Updates an existing document status with updated information.
     *
     * @param documentStatusId the unique identifier of the document status to be updated
     * @param documentStatusDTO the data transfer object containing the updated details of the document status
     * @return a reactive Mono containing the updated DocumentStatusDTO
     */
    Mono<DocumentStatusDTO> updateDocumentStatus(Long documentStatusId, DocumentStatusDTO documentStatusDTO);
    
    /**
     * Deletes a document status identified by its unique ID.
     *
     * @param documentStatusId the unique identifier of the document status to be deleted
     * @return a Mono that completes when the document status is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deleteDocumentStatus(Long documentStatusId);
    
    /**
     * Retrieves a document status by its unique identifier.
     *
     * @param documentStatusId the unique identifier of the document status to retrieve
     * @return a Mono emitting the {@link DocumentStatusDTO} representing the document status if found,
     *         or an empty Mono if the document status does not exist
     */
    Mono<DocumentStatusDTO> getDocumentStatusById(Long documentStatusId);
}