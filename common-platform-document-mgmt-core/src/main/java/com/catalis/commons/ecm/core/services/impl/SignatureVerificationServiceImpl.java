package com.catalis.commons.ecm.core.services.impl;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.SignatureVerificationService;
import com.catalis.commons.ecm.interfaces.dtos.SignatureVerificationDTO;
import com.catalis.commons.ecm.interfaces.enums.VerificationStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of the SignatureVerificationService interface.
 */
@Service
@Transactional
public class SignatureVerificationServiceImpl implements SignatureVerificationService {

    @Override
    public Mono<SignatureVerificationDTO> getById(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<PaginationResponse<SignatureVerificationDTO>> filter(FilterRequest<SignatureVerificationDTO> filterRequest) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<SignatureVerificationDTO> update(SignatureVerificationDTO signatureVerification) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<SignatureVerificationDTO> create(SignatureVerificationDTO signatureVerification) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<Void> delete(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Flux<SignatureVerificationDTO> getByDocumentSignatureId(Long documentSignatureId) {
        return Flux.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<SignatureVerificationDTO> getLatestVerification(Long documentSignatureId) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Flux<SignatureVerificationDTO> getByVerificationStatus(VerificationStatus verificationStatus) {
        return Flux.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<SignatureVerificationDTO> verifySignature(Long documentSignatureId) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Flux<SignatureVerificationDTO> verifyAllSignaturesForDocument(Long documentId) {
        return Flux.error(new UnsupportedOperationException("Not implemented yet"));
    }
}