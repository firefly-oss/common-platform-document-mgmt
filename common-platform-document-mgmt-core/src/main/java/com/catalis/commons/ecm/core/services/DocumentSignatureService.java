package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.interfaces.dtos.DocumentSignatureDTO;
import com.catalis.commons.ecm.interfaces.enums.SignatureStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing DocumentSignature entities in the Enterprise Content Management system.
 */
public interface DocumentSignatureService {

    /**
     * Get a document signature by its ID.
     *
     * @param id The document signature ID
     * @return A Mono emitting the document signature if found, or empty if not found
     */
    Mono<DocumentSignatureDTO> getById(Long id);

    /**
     * Filter document signatures based on the provided filter request.
     *
     * @param filterRequest The filter request containing filtering and pagination parameters
     * @return A Mono emitting a pagination response with the filtered document signatures
     */
    Mono<PaginationResponse<DocumentSignatureDTO>> filter(FilterRequest<DocumentSignatureDTO> filterRequest);

    /**
     * Update an existing document signature.
     *
     * @param documentSignature The document signature to update
     * @return A Mono emitting the updated document signature
     */
    Mono<DocumentSignatureDTO> update(DocumentSignatureDTO documentSignature);

    /**
     * Create a new document signature.
     *
     * @param documentSignature The document signature to create
     * @return A Mono emitting the created document signature
     */
    Mono<DocumentSignatureDTO> create(DocumentSignatureDTO documentSignature);

    /**
     * Delete a document signature by its ID.
     *
     * @param id The ID of the document signature to delete
     * @return A Mono completing when the document signature is deleted
     */
    Mono<Void> delete(Long id);

    /**
     * Get all signatures for a document.
     *
     * @param documentId The document ID
     * @return A Flux emitting all signatures for the document
     */
    Flux<DocumentSignatureDTO> getByDocumentId(Long documentId);

    /**
     * Get all signatures for a document version.
     *
     * @param documentVersionId The document version ID
     * @return A Flux emitting all signatures for the document version
     */
    Flux<DocumentSignatureDTO> getByDocumentVersionId(Long documentVersionId);

    /**
     * Get all signatures by a specific signer.
     *
     * @param signerPartyId The signer party ID
     * @return A Flux emitting all signatures by the signer
     */
    Flux<DocumentSignatureDTO> getBySignerPartyId(Long signerPartyId);

    /**
     * Get all signatures with a specific status.
     *
     * @param signatureStatus The signature status
     * @return A Flux emitting all signatures with the specified status
     */
    Flux<DocumentSignatureDTO> getBySignatureStatus(SignatureStatus signatureStatus);

    /**
     * Initiate the signing process for a document.
     *
     * @param documentSignature The document signature to initiate
     * @return A Mono emitting the initiated document signature
     */
    Mono<DocumentSignatureDTO> initiateSigningProcess(DocumentSignatureDTO documentSignature);

    /**
     * Cancel a signature request.
     *
     * @param id The ID of the document signature to cancel
     * @return A Mono emitting the canceled document signature
     */
    Mono<DocumentSignatureDTO> cancelSignature(Long id);

    /**
     * Check if a document is fully signed.
     *
     * @param documentId The document ID
     * @return A Mono emitting true if the document is fully signed, false otherwise
     */
    Mono<Boolean> isDocumentFullySigned(Long documentId);
}
