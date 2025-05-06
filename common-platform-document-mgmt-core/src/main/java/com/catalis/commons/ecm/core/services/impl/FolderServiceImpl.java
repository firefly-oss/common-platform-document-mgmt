package com.catalis.commons.ecm.core.services.impl;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.FolderService;
import com.catalis.commons.ecm.interfaces.dtos.FolderDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the FolderService interface.
 */
@Service
@Transactional
public class FolderServiceImpl implements FolderService {

    @Override
    public Mono<FolderDTO> getById(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<PaginationResponse<FolderDTO>> filter(FilterRequest<FolderDTO> filterRequest) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<FolderDTO> update(FolderDTO folder) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<FolderDTO> create(FolderDTO folder) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<Void> delete(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }
}