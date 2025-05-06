package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.interfaces.enums.SignatureStatus;
import com.catalis.commons.ecm.models.entities.SignatureRequest;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Repository for managing SignatureRequest entities in the Enterprise Content Management system.
 */
@Repository
public interface SignatureRequestRepository extends BaseRepository<SignatureRequest, Long> {
    
    /**
     * Find all signature requests for a document signature.
     *
     * @param documentSignatureId The document signature ID
     * @return A Flux emitting all signature requests for the document signature
     */
    Flux<SignatureRequest> findByDocumentSignatureId(Long documentSignatureId);
    
    /**
     * Find a signature request by its reference.
     *
     * @param requestReference The request reference
     * @return A Mono emitting the signature request if found, or empty if not found
     */
    Mono<SignatureRequest> findByRequestReference(String requestReference);
    
    /**
     * Find all signature requests with a specific status.
     *
     * @param requestStatus The request status
     * @return A Flux emitting all signature requests with the specified status
     */
    Flux<SignatureRequest> findByRequestStatus(SignatureStatus requestStatus);
    
    /**
     * Find all expired signature requests.
     *
     * @param currentDateTime The current date and time
     * @return A Flux emitting all expired signature requests
     */
    Flux<SignatureRequest> findByExpirationDateBeforeAndRequestStatus(
            LocalDateTime currentDateTime, SignatureStatus requestStatus);
    
    /**
     * Find all signature requests that need a reminder.
     *
     * @param reminderSent Whether a reminder has been sent
     * @param requestStatus The request status
     * @return A Flux emitting all signature requests that need a reminder
     */
    Flux<SignatureRequest> findByReminderSentAndRequestStatus(
            Boolean reminderSent, SignatureStatus requestStatus);
}
