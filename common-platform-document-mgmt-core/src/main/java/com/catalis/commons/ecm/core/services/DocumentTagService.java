package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.interfaces.dtos.DocumentTagDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing document tags.
 */
public interface DocumentTagService {
    /**
     * Filters the document tags based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for DocumentTagDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of document tags
     */
    Mono<PaginationResponse<DocumentTagDTO>> filterDocumentTags(FilterRequest<DocumentTagDTO> filterRequest);
    
    /**
     * Creates a new document tag based on the provided information.
     *
     * @param documentTagDTO the DTO object containing details of the document tag to be created
     * @return a Mono that emits the created DocumentTagDTO object
     */
    Mono<DocumentTagDTO> createDocumentTag(DocumentTagDTO documentTagDTO);
    
    /**
     * Updates an existing document tag with updated information.
     *
     * @param documentTagId the unique identifier of the document tag to be updated
     * @param documentTagDTO the data transfer object containing the updated details of the document tag
     * @return a reactive Mono containing the updated DocumentTagDTO
     */
    Mono<DocumentTagDTO> updateDocumentTag(Long documentTagId, DocumentTagDTO documentTagDTO);
    
    /**
     * Deletes a document tag identified by its unique ID.
     *
     * @param documentTagId the unique identifier of the document tag to be deleted
     * @return a Mono that completes when the document tag is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deleteDocumentTag(Long documentTagId);
    
    /**
     * Retrieves a document tag by its unique identifier.
     *
     * @param documentTagId the unique identifier of the document tag to retrieve
     * @return a Mono emitting the {@link DocumentTagDTO} representing the document tag if found,
     *         or an empty Mono if the document tag does not exist
     */
    Mono<DocumentTagDTO> getDocumentTagById(Long documentTagId);
}