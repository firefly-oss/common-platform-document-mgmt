package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.interfaces.dtos.SignatureStatusDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing signature statuses.
 */
public interface SignatureStatusService {
    /**
     * Filters the signature statuses based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for SignatureStatusDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of signature statuses
     */
    Mono<PaginationResponse<SignatureStatusDTO>> filterSignatureStatuses(FilterRequest<SignatureStatusDTO> filterRequest);
    
    /**
     * Creates a new signature status based on the provided information.
     *
     * @param signatureStatusDTO the DTO object containing details of the signature status to be created
     * @return a Mono that emits the created SignatureStatusDTO object
     */
    Mono<SignatureStatusDTO> createSignatureStatus(SignatureStatusDTO signatureStatusDTO);
    
    /**
     * Updates an existing signature status with updated information.
     *
     * @param signatureStatusId the unique identifier of the signature status to be updated
     * @param signatureStatusDTO the data transfer object containing the updated details of the signature status
     * @return a reactive Mono containing the updated SignatureStatusDTO
     */
    Mono<SignatureStatusDTO> updateSignatureStatus(Long signatureStatusId, SignatureStatusDTO signatureStatusDTO);
    
    /**
     * Deletes a signature status identified by its unique ID.
     *
     * @param signatureStatusId the unique identifier of the signature status to be deleted
     * @return a Mono that completes when the signature status is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deleteSignatureStatus(Long signatureStatusId);
    
    /**
     * Retrieves a signature status by its unique identifier.
     *
     * @param signatureStatusId the unique identifier of the signature status to retrieve
     * @return a Mono emitting the {@link SignatureStatusDTO} representing the signature status if found,
     *         or an empty Mono if the signature status does not exist
     */
    Mono<SignatureStatusDTO> getSignatureStatusById(Long signatureStatusId);
}