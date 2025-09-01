package com.firefly.commons.ecm.core.services;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.interfaces.dtos.SignatureRequestDTO;
import com.firefly.commons.ecm.interfaces.enums.SignatureStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;
/**
 * Service interface for managing SignatureRequest entities in the Enterprise Content Management system.
 */
public interface SignatureRequestService {

    /**
     * Get a signature request by its ID.
     *
     * @param id The signature request ID
     * @return A Mono emitting the signature request if found, or empty if not found
     */
    Mono<SignatureRequestDTO> getById(UUID id);

    /**
     * Filter signature requests based on the provided filter request.
     *
     * @param filterRequest The filter request containing filtering and pagination parameters
     * @return A Mono emitting a pagination response with the filtered signature requests
     */
    Mono<PaginationResponse<SignatureRequestDTO>> filter(FilterRequest<SignatureRequestDTO> filterRequest);

    /**
     * Update an existing signature request.
     *
     * @param signatureRequest The signature request to update
     * @return A Mono emitting the updated signature request
     */
    Mono<SignatureRequestDTO> update(SignatureRequestDTO signatureRequest);

    /**
     * Create a new signature request.
     *
     * @param signatureRequest The signature request to create
     * @return A Mono emitting the created signature request
     */
    Mono<SignatureRequestDTO> create(SignatureRequestDTO signatureRequest);

    /**
     * Delete a signature request by its ID.
     *
     * @param id The ID of the signature request to delete
     * @return A Mono completing when the signature request is deleted
     */
    Mono<Void> delete(UUID id);

    /**
     * Get all signature requests for a document signature.
     *
     * @param documentSignatureId The document signature ID
     * @return A Flux emitting all signature requests for the document signature
     */
    Flux<SignatureRequestDTO> getByDocumentSignatureId(UUID documentSignatureId);

    /**
     * Get a signature request by its reference.
     *
     * @param requestReference The request reference
     * @return A Mono emitting the signature request if found, or empty if not found
     */
    Mono<SignatureRequestDTO> getByRequestReference(String requestReference);

    /**
     * Get all signature requests with a specific status.
     *
     * @param requestStatus The request status
     * @return A Flux emitting all signature requests with the specified status
     */
    Flux<SignatureRequestDTO> getByRequestStatus(SignatureStatus requestStatus);

    /**
     * Send a notification for a signature request.
     *
     * @param id The ID of the signature request
     * @return A Mono emitting the updated signature request
     */
    Mono<SignatureRequestDTO> sendNotification(UUID id);

    /**
     * Send a reminder for a signature request.
     *
     * @param id The ID of the signature request
     * @return A Mono emitting the updated signature request
     */
    Mono<SignatureRequestDTO> sendReminder(UUID id);

    /**
     * Check for and process expired signature requests.
     *
     * @return A Flux emitting the processed signature requests
     */
    Flux<SignatureRequestDTO> processExpiredRequests();
}
