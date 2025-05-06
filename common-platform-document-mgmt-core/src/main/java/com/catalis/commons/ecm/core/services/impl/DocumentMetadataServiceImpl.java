package com.catalis.commons.ecm.core.services.impl;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.DocumentMetadataService;
import com.catalis.commons.ecm.interfaces.dtos.DocumentMetadataDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the DocumentMetadataService interface.
 */
@Service
@Transactional
public class DocumentMetadataServiceImpl implements DocumentMetadataService {

    @Override
    public Mono<DocumentMetadataDTO> getById(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<PaginationResponse<DocumentMetadataDTO>> filter(FilterRequest<DocumentMetadataDTO> filterRequest) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<DocumentMetadataDTO> update(DocumentMetadataDTO documentMetadata) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<DocumentMetadataDTO> create(DocumentMetadataDTO documentMetadata) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<Void> delete(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }
}