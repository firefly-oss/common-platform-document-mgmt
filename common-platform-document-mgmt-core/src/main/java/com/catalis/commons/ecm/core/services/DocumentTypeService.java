package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.interfaces.dtos.DocumentTypeDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing document types.
 */
public interface DocumentTypeService {
    /**
     * Filters the document types based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for DocumentTypeDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of document types
     */
    Mono<PaginationResponse<DocumentTypeDTO>> filterDocumentTypes(FilterRequest<DocumentTypeDTO> filterRequest);
    
    /**
     * Creates a new document type based on the provided information.
     *
     * @param documentTypeDTO the DTO object containing details of the document type to be created
     * @return a Mono that emits the created DocumentTypeDTO object
     */
    Mono<DocumentTypeDTO> createDocumentType(DocumentTypeDTO documentTypeDTO);
    
    /**
     * Updates an existing document type with updated information.
     *
     * @param documentTypeId the unique identifier of the document type to be updated
     * @param documentTypeDTO the data transfer object containing the updated details of the document type
     * @return a reactive Mono containing the updated DocumentTypeDTO
     */
    Mono<DocumentTypeDTO> updateDocumentType(Long documentTypeId, DocumentTypeDTO documentTypeDTO);
    
    /**
     * Deletes a document type identified by its unique ID.
     *
     * @param documentTypeId the unique identifier of the document type to be deleted
     * @return a Mono that completes when the document type is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deleteDocumentType(Long documentTypeId);
    
    /**
     * Retrieves a document type by its unique identifier.
     *
     * @param documentTypeId the unique identifier of the document type to retrieve
     * @return a Mono emitting the {@link DocumentTypeDTO} representing the document type if found,
     *         or an empty Mono if the document type does not exist
     */
    Mono<DocumentTypeDTO> getDocumentTypeById(Long documentTypeId);
}