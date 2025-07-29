package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.interfaces.dtos.ProofTypeDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing proof types.
 */
public interface ProofTypeService {
    /**
     * Filters the proof types based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for ProofTypeDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of proof types
     */
    Mono<PaginationResponse<ProofTypeDTO>> filterProofTypes(FilterRequest<ProofTypeDTO> filterRequest);
    
    /**
     * Creates a new proof type based on the provided information.
     *
     * @param proofTypeDTO the DTO object containing details of the proof type to be created
     * @return a Mono that emits the created ProofTypeDTO object
     */
    Mono<ProofTypeDTO> createProofType(ProofTypeDTO proofTypeDTO);
    
    /**
     * Updates an existing proof type with updated information.
     *
     * @param proofTypeId the unique identifier of the proof type to be updated
     * @param proofTypeDTO the data transfer object containing the updated details of the proof type
     * @return a reactive Mono containing the updated ProofTypeDTO
     */
    Mono<ProofTypeDTO> updateProofType(Long proofTypeId, ProofTypeDTO proofTypeDTO);
    
    /**
     * Deletes a proof type identified by its unique ID.
     *
     * @param proofTypeId the unique identifier of the proof type to be deleted
     * @return a Mono that completes when the proof type is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deleteProofType(Long proofTypeId);
    
    /**
     * Retrieves a proof type by its unique identifier.
     *
     * @param proofTypeId the unique identifier of the proof type to retrieve
     * @return a Mono emitting the {@link ProofTypeDTO} representing the proof type if found,
     *         or an empty Mono if the proof type does not exist
     */
    Mono<ProofTypeDTO> getProofTypeById(Long proofTypeId);
}