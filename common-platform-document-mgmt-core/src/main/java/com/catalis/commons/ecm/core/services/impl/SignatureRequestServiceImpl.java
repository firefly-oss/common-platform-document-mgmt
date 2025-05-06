package com.catalis.commons.ecm.core.services.impl;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.SignatureRequestService;
import com.catalis.commons.ecm.interfaces.dtos.SignatureRequestDTO;
import com.catalis.commons.ecm.interfaces.enums.SignatureStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of the SignatureRequestService interface.
 */
@Service
@Transactional
public class SignatureRequestServiceImpl implements SignatureRequestService {

    @Override
    public Mono<SignatureRequestDTO> getById(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<PaginationResponse<SignatureRequestDTO>> filter(FilterRequest<SignatureRequestDTO> filterRequest) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<SignatureRequestDTO> update(SignatureRequestDTO signatureRequest) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<SignatureRequestDTO> create(SignatureRequestDTO signatureRequest) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<Void> delete(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Flux<SignatureRequestDTO> getByDocumentSignatureId(Long documentSignatureId) {
        return Flux.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<SignatureRequestDTO> getByRequestReference(String requestReference) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Flux<SignatureRequestDTO> getByRequestStatus(SignatureStatus requestStatus) {
        return Flux.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<SignatureRequestDTO> sendNotification(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<SignatureRequestDTO> sendReminder(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Flux<SignatureRequestDTO> processExpiredRequests() {
        return Flux.error(new UnsupportedOperationException("Not implemented yet"));
    }
}