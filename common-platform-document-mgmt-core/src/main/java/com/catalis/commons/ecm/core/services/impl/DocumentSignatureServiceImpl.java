package com.catalis.commons.ecm.core.services.impl;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.DocumentSignatureService;
import com.catalis.commons.ecm.interfaces.dtos.DocumentSignatureDTO;
import com.catalis.commons.ecm.interfaces.enums.SignatureStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of the DocumentSignatureService interface.
 */
@Service
@Transactional
public class DocumentSignatureServiceImpl implements DocumentSignatureService {

    @Override
    public Mono<DocumentSignatureDTO> getById(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<PaginationResponse<DocumentSignatureDTO>> filter(FilterRequest<DocumentSignatureDTO> filterRequest) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<DocumentSignatureDTO> update(DocumentSignatureDTO documentSignature) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<DocumentSignatureDTO> create(DocumentSignatureDTO documentSignature) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<Void> delete(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Flux<DocumentSignatureDTO> getByDocumentId(Long documentId) {
        return Flux.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Flux<DocumentSignatureDTO> getByDocumentVersionId(Long documentVersionId) {
        return Flux.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Flux<DocumentSignatureDTO> getBySignerPartyId(Long signerPartyId) {
        return Flux.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Flux<DocumentSignatureDTO> getBySignatureStatus(SignatureStatus signatureStatus) {
        return Flux.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<DocumentSignatureDTO> initiateSigningProcess(DocumentSignatureDTO documentSignature) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<DocumentSignatureDTO> cancelSignature(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<Boolean> isDocumentFullySigned(Long documentId) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }
}