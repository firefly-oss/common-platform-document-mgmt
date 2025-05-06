package com.catalis.commons.ecm.core.services.impl;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.DocumentVersionService;
import com.catalis.commons.ecm.interfaces.dtos.DocumentVersionDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the DocumentVersionService interface.
 */
@Service
@Transactional
public class DocumentVersionServiceImpl implements DocumentVersionService {

    @Override
    public Mono<DocumentVersionDTO> getById(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<PaginationResponse<DocumentVersionDTO>> filter(FilterRequest<DocumentVersionDTO> filterRequest) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<DocumentVersionDTO> update(DocumentVersionDTO documentVersion) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<DocumentVersionDTO> create(DocumentVersionDTO documentVersion) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<Void> delete(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }
}