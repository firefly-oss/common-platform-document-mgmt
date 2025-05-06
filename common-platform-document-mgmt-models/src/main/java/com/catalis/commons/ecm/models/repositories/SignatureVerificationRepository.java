package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.interfaces.enums.VerificationStatus;
import com.catalis.commons.ecm.models.entities.SignatureVerification;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repository for managing SignatureVerification entities in the Enterprise Content Management system.
 */
@Repository
public interface SignatureVerificationRepository extends BaseRepository<SignatureVerification, Long> {
    
    /**
     * Find all verifications for a document signature.
     *
     * @param documentSignatureId The document signature ID
     * @return A Flux emitting all verifications for the document signature
     */
    Flux<SignatureVerification> findByDocumentSignatureId(Long documentSignatureId);
    
    /**
     * Find the latest verification for a document signature.
     *
     * @param documentSignatureId The document signature ID
     * @return A Mono emitting the latest verification for the document signature
     */
    Mono<SignatureVerification> findFirstByDocumentSignatureIdOrderByVerificationTimestampDesc(Long documentSignatureId);
    
    /**
     * Find all verifications with a specific status.
     *
     * @param verificationStatus The verification status
     * @return A Flux emitting all verifications with the specified status
     */
    Flux<SignatureVerification> findByVerificationStatus(VerificationStatus verificationStatus);
    
    /**
     * Count the number of verifications for a document signature.
     *
     * @param documentSignatureId The document signature ID
     * @return A Mono emitting the count of verifications for the document signature
     */
    Mono<Long> countByDocumentSignatureId(Long documentSignatureId);
}
