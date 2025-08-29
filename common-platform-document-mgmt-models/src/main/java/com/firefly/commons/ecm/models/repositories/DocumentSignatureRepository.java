package com.firefly.commons.ecm.models.repositories;

import com.firefly.commons.ecm.interfaces.enums.SignatureStatus;
import com.firefly.commons.ecm.models.entities.DocumentSignature;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repository for managing DocumentSignature entities in the Enterprise Content Management system.
 */
@Repository
public interface DocumentSignatureRepository extends BaseRepository<DocumentSignature, Long> {
    
    /**
     * Find all signatures for a document.
     *
     * @param documentId The document ID
     * @return A Flux emitting all signatures for the document
     */
    Flux<DocumentSignature> findByDocumentId(Long documentId);
    
    /**
     * Find all signatures for a document version.
     *
     * @param documentVersionId The document version ID
     * @return A Flux emitting all signatures for the document version
     */
    Flux<DocumentSignature> findByDocumentVersionId(Long documentVersionId);
    
    /**
     * Find all signatures by a specific signer.
     *
     * @param signerPartyId The signer party ID
     * @return A Flux emitting all signatures by the signer
     */
    Flux<DocumentSignature> findBySignerPartyId(Long signerPartyId);
    
    /**
     * Find all signatures with a specific status.
     *
     * @param signatureStatus The signature status
     * @return A Flux emitting all signatures with the specified status
     */
    Flux<DocumentSignature> findBySignatureStatus(SignatureStatus signatureStatus);
    
    /**
     * Find all signatures for a document with a specific status.
     *
     * @param documentId The document ID
     * @param signatureStatus The signature status
     * @return A Flux emitting all signatures for the document with the specified status
     */
    Flux<DocumentSignature> findByDocumentIdAndSignatureStatus(Long documentId, SignatureStatus signatureStatus);
    
    /**
     * Count the number of signatures for a document.
     *
     * @param documentId The document ID
     * @return A Mono emitting the count of signatures for the document
     */
    Mono<Long> countByDocumentId(Long documentId);
}
