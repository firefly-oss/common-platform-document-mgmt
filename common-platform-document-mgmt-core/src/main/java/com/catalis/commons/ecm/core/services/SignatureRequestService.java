package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.interfaces.dtos.SignatureRequestDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing signature requests.
 */
public interface SignatureRequestService {
    /**
     * Filters the signature requests based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for SignatureRequestDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of signature requests
     */
    Mono<PaginationResponse<SignatureRequestDTO>> filterSignatureRequests(FilterRequest<SignatureRequestDTO> filterRequest);
    
    /**
     * Creates a new signature request based on the provided information.
     *
     * @param signatureRequestDTO the DTO object containing details of the signature request to be created
     * @return a Mono that emits the created SignatureRequestDTO object
     */
    Mono<SignatureRequestDTO> createSignatureRequest(SignatureRequestDTO signatureRequestDTO);
    
    /**
     * Updates an existing signature request with updated information.
     *
     * @param signatureRequestId the unique identifier of the signature request to be updated
     * @param signatureRequestDTO the data transfer object containing the updated details of the signature request
     * @return a reactive Mono containing the updated SignatureRequestDTO
     */
    Mono<SignatureRequestDTO> updateSignatureRequest(Long signatureRequestId, SignatureRequestDTO signatureRequestDTO);
    
    /**
     * Deletes a signature request identified by its unique ID.
     *
     * @param signatureRequestId the unique identifier of the signature request to be deleted
     * @return a Mono that completes when the signature request is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deleteSignatureRequest(Long signatureRequestId);
    
    /**
     * Retrieves a signature request by its unique identifier.
     *
     * @param signatureRequestId the unique identifier of the signature request to retrieve
     * @return a Mono emitting the {@link SignatureRequestDTO} representing the signature request if found,
     *         or an empty Mono if the signature request does not exist
     */
    Mono<SignatureRequestDTO> getSignatureRequestById(Long signatureRequestId);
}