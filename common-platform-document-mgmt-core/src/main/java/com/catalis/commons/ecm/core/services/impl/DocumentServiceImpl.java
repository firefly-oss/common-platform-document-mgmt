package com.catalis.commons.ecm.core.services.impl;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.DocumentService;
import com.catalis.commons.ecm.interfaces.dtos.DocumentDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the DocumentService interface.
 */
@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    @Override
    public Mono<DocumentDTO> getById(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<PaginationResponse<DocumentDTO>> filter(FilterRequest<DocumentDTO> filterRequest) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<DocumentDTO> update(DocumentDTO document) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<DocumentDTO> create(DocumentDTO document) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<Void> delete(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }
}