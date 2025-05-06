package com.catalis.commons.ecm.core.services.impl;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.TagService;
import com.catalis.commons.ecm.interfaces.dtos.TagDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the TagService interface.
 */
@Service
@Transactional
public class TagServiceImpl implements TagService {

    @Override
    public Mono<TagDTO> getById(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<PaginationResponse<TagDTO>> filter(FilterRequest<TagDTO> filterRequest) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<TagDTO> update(TagDTO tag) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<TagDTO> create(TagDTO tag) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<Void> delete(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }
}