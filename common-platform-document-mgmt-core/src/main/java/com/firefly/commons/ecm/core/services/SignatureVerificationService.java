package com.firefly.commons.ecm.core.services;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.interfaces.dtos.SignatureVerificationDTO;
import com.firefly.commons.ecm.interfaces.enums.VerificationStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing SignatureVerification entities in the Enterprise Content Management system.
 */
public interface SignatureVerificationService {

    /**
     * Get a signature verification by its ID.
     *
     * @param id The signature verification ID
     * @return A Mono emitting the signature verification if found, or empty if not found
     */
    Mono<SignatureVerificationDTO> getById(Long id);

    /**
     * Filter signature verifications based on the provided filter request.
     *
     * @param filterRequest The filter request containing filtering and pagination parameters
     * @return A Mono emitting a pagination response with the filtered signature verifications
     */
    Mono<PaginationResponse<SignatureVerificationDTO>> filter(FilterRequest<SignatureVerificationDTO> filterRequest);

    /**
     * Update an existing signature verification.
     *
     * @param signatureVerification The signature verification to update
     * @return A Mono emitting the updated signature verification
     */
    Mono<SignatureVerificationDTO> update(SignatureVerificationDTO signatureVerification);

    /**
     * Create a new signature verification.
     *
     * @param signatureVerification The signature verification to create
     * @return A Mono emitting the created signature verification
     */
    Mono<SignatureVerificationDTO> create(SignatureVerificationDTO signatureVerification);

    /**
     * Delete a signature verification by its ID.
     *
     * @param id The ID of the signature verification to delete
     * @return A Mono completing when the signature verification is deleted
     */
    Mono<Void> delete(Long id);

    /**
     * Get all verifications for a document signature.
     *
     * @param documentSignatureId The document signature ID
     * @return A Flux emitting all verifications for the document signature
     */
    Flux<SignatureVerificationDTO> getByDocumentSignatureId(Long documentSignatureId);

    /**
     * Get the latest verification for a document signature.
     *
     * @param documentSignatureId The document signature ID
     * @return A Mono emitting the latest verification for the document signature
     */
    Mono<SignatureVerificationDTO> getLatestVerification(Long documentSignatureId);

    /**
     * Get all verifications with a specific status.
     *
     * @param verificationStatus The verification status
     * @return A Flux emitting all verifications with the specified status
     */
    Flux<SignatureVerificationDTO> getByVerificationStatus(VerificationStatus verificationStatus);

    /**
     * Verify a document signature.
     *
     * @param documentSignatureId The document signature ID
     * @return A Mono emitting the verification result
     */
    Mono<SignatureVerificationDTO> verifySignature(Long documentSignatureId);

    /**
     * Verify all signatures for a document.
     *
     * @param documentId The document ID
     * @return A Flux emitting the verification results for all signatures
     */
    Flux<SignatureVerificationDTO> verifyAllSignaturesForDocument(Long documentId);
}
