package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.interfaces.dtos.SignatureProofDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing signature proofs.
 */
public interface SignatureProofService {
    /**
     * Filters the signature proofs based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for SignatureProofDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of signature proofs
     */
    Mono<PaginationResponse<SignatureProofDTO>> filterSignatureProofs(FilterRequest<SignatureProofDTO> filterRequest);
    
    /**
     * Creates a new signature proof based on the provided information.
     *
     * @param signatureProofDTO the DTO object containing details of the signature proof to be created
     * @return a Mono that emits the created SignatureProofDTO object
     */
    Mono<SignatureProofDTO> createSignatureProof(SignatureProofDTO signatureProofDTO);
    
    /**
     * Updates an existing signature proof with updated information.
     *
     * @param signatureProofId the unique identifier of the signature proof to be updated
     * @param signatureProofDTO the data transfer object containing the updated details of the signature proof
     * @return a reactive Mono containing the updated SignatureProofDTO
     */
    Mono<SignatureProofDTO> updateSignatureProof(Long signatureProofId, SignatureProofDTO signatureProofDTO);
    
    /**
     * Deletes a signature proof identified by its unique ID.
     *
     * @param signatureProofId the unique identifier of the signature proof to be deleted
     * @return a Mono that completes when the signature proof is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deleteSignatureProof(Long signatureProofId);
    
    /**
     * Retrieves a signature proof by its unique identifier.
     *
     * @param signatureProofId the unique identifier of the signature proof to retrieve
     * @return a Mono emitting the {@link SignatureProofDTO} representing the signature proof if found,
     *         or an empty Mono if the signature proof does not exist
     */
    Mono<SignatureProofDTO> getSignatureProofById(Long signatureProofId);
}