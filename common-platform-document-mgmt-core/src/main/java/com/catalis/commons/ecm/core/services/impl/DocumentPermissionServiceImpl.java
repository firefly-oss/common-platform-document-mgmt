package com.catalis.commons.ecm.core.services.impl;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.DocumentPermissionService;
import com.catalis.commons.ecm.interfaces.dtos.DocumentPermissionDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the DocumentPermissionService interface.
 */
@Service
@Transactional
public class DocumentPermissionServiceImpl implements DocumentPermissionService {

    @Override
    public Mono<DocumentPermissionDTO> getById(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<PaginationResponse<DocumentPermissionDTO>> filter(FilterRequest<DocumentPermissionDTO> filterRequest) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<DocumentPermissionDTO> update(DocumentPermissionDTO documentPermission) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<DocumentPermissionDTO> create(DocumentPermissionDTO documentPermission) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<Void> delete(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }
}