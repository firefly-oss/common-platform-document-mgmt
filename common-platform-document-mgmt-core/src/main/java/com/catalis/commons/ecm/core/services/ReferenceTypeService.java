package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.interfaces.dtos.ReferenceTypeDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing reference types.
 */
public interface ReferenceTypeService {
    /**
     * Filters the reference types based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for ReferenceTypeDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of reference types
     */
    Mono<PaginationResponse<ReferenceTypeDTO>> filterReferenceTypes(FilterRequest<ReferenceTypeDTO> filterRequest);
    
    /**
     * Creates a new reference type based on the provided information.
     *
     * @param referenceTypeDTO the DTO object containing details of the reference type to be created
     * @return a Mono that emits the created ReferenceTypeDTO object
     */
    Mono<ReferenceTypeDTO> createReferenceType(ReferenceTypeDTO referenceTypeDTO);
    
    /**
     * Updates an existing reference type with updated information.
     *
     * @param referenceTypeId the unique identifier of the reference type to be updated
     * @param referenceTypeDTO the data transfer object containing the updated details of the reference type
     * @return a reactive Mono containing the updated ReferenceTypeDTO
     */
    Mono<ReferenceTypeDTO> updateReferenceType(Long referenceTypeId, ReferenceTypeDTO referenceTypeDTO);
    
    /**
     * Deletes a reference type identified by its unique ID.
     *
     * @param referenceTypeId the unique identifier of the reference type to be deleted
     * @return a Mono that completes when the reference type is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deleteReferenceType(Long referenceTypeId);
    
    /**
     * Retrieves a reference type by its unique identifier.
     *
     * @param referenceTypeId the unique identifier of the reference type to retrieve
     * @return a Mono emitting the {@link ReferenceTypeDTO} representing the reference type if found,
     *         or an empty Mono if the reference type does not exist
     */
    Mono<ReferenceTypeDTO> getReferenceTypeById(Long referenceTypeId);
}