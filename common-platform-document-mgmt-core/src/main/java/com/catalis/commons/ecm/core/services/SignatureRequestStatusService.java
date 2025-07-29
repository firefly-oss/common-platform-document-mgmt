package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.interfaces.dtos.SignatureRequestStatusDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing signature request statuses.
 */
public interface SignatureRequestStatusService {
    /**
     * Filters the signature request statuses based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for SignatureRequestStatusDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of signature request statuses
     */
    Mono<PaginationResponse<SignatureRequestStatusDTO>> filterSignatureRequestStatuses(FilterRequest<SignatureRequestStatusDTO> filterRequest);
    
    /**
     * Creates a new signature request status based on the provided information.
     *
     * @param signatureRequestStatusDTO the DTO object containing details of the signature request status to be created
     * @return a Mono that emits the created SignatureRequestStatusDTO object
     */
    Mono<SignatureRequestStatusDTO> createSignatureRequestStatus(SignatureRequestStatusDTO signatureRequestStatusDTO);
    
    /**
     * Updates an existing signature request status with updated information.
     *
     * @param signatureRequestStatusId the unique identifier of the signature request status to be updated
     * @param signatureRequestStatusDTO the data transfer object containing the updated details of the signature request status
     * @return a reactive Mono containing the updated SignatureRequestStatusDTO
     */
    Mono<SignatureRequestStatusDTO> updateSignatureRequestStatus(Long signatureRequestStatusId, SignatureRequestStatusDTO signatureRequestStatusDTO);
    
    /**
     * Deletes a signature request status identified by its unique ID.
     *
     * @param signatureRequestStatusId the unique identifier of the signature request status to be deleted
     * @return a Mono that completes when the signature request status is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deleteSignatureRequestStatus(Long signatureRequestStatusId);
    
    /**
     * Retrieves a signature request status by its unique identifier.
     *
     * @param signatureRequestStatusId the unique identifier of the signature request status to retrieve
     * @return a Mono emitting the {@link SignatureRequestStatusDTO} representing the signature request status if found,
     *         or an empty Mono if the signature request status does not exist
     */
    Mono<SignatureRequestStatusDTO> getSignatureRequestStatusById(Long signatureRequestStatusId);
}