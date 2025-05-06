package com.catalis.commons.ecm.core.services.impl;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.SignatureProviderService;
import com.catalis.commons.ecm.interfaces.dtos.SignatureProviderDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the SignatureProviderService interface.
 */
@Service
@Transactional
public class SignatureProviderServiceImpl implements SignatureProviderService {

    @Override
    public Mono<SignatureProviderDTO> getById(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<PaginationResponse<SignatureProviderDTO>> filter(FilterRequest<SignatureProviderDTO> filterRequest) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<SignatureProviderDTO> update(SignatureProviderDTO signatureProvider) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<SignatureProviderDTO> create(SignatureProviderDTO signatureProvider) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<Void> delete(Long id) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<SignatureProviderDTO> getDefaultProvider(String tenantId) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }

    @Override
    public Mono<SignatureProviderDTO> setAsDefault(Long id, String tenantId) {
        return Mono.error(new UnsupportedOperationException("Not implemented yet"));
    }
}