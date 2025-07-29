package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.interfaces.dtos.SignatureSignerDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing signature signers.
 */
public interface SignatureSignerService {
    /**
     * Filters the signature signers based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for SignatureSignerDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of signature signers
     */
    Mono<PaginationResponse<SignatureSignerDTO>> filterSignatureSigners(FilterRequest<SignatureSignerDTO> filterRequest);
    
    /**
     * Creates a new signature signer based on the provided information.
     *
     * @param signatureSignerDTO the DTO object containing details of the signature signer to be created
     * @return a Mono that emits the created SignatureSignerDTO object
     */
    Mono<SignatureSignerDTO> createSignatureSigner(SignatureSignerDTO signatureSignerDTO);
    
    /**
     * Updates an existing signature signer with updated information.
     *
     * @param signatureSignerId the unique identifier of the signature signer to be updated
     * @param signatureSignerDTO the data transfer object containing the updated details of the signature signer
     * @return a reactive Mono containing the updated SignatureSignerDTO
     */
    Mono<SignatureSignerDTO> updateSignatureSigner(Long signatureSignerId, SignatureSignerDTO signatureSignerDTO);
    
    /**
     * Deletes a signature signer identified by its unique ID.
     *
     * @param signatureSignerId the unique identifier of the signature signer to be deleted
     * @return a Mono that completes when the signature signer is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deleteSignatureSigner(Long signatureSignerId);
    
    /**
     * Retrieves a signature signer by its unique identifier.
     *
     * @param signatureSignerId the unique identifier of the signature signer to retrieve
     * @return a Mono emitting the {@link SignatureSignerDTO} representing the signature signer if found,
     *         or an empty Mono if the signature signer does not exist
     */
    Mono<SignatureSignerDTO> getSignatureSignerById(Long signatureSignerId);
}