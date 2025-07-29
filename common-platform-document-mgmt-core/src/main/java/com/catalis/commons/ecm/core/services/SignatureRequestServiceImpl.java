package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.SignatureRequestMapper;
import com.catalis.commons.ecm.interfaces.dtos.SignatureRequestDTO;
import com.catalis.commons.ecm.models.entities.SignatureRequest;
import com.catalis.commons.ecm.models.repositories.SignatureRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class SignatureRequestServiceImpl implements SignatureRequestService {

    @Autowired
    private SignatureRequestRepository repository;

    @Autowired
    private SignatureRequestMapper mapper;

    @Override
    public Mono<PaginationResponse<SignatureRequestDTO>> filterSignatureRequests(FilterRequest<SignatureRequestDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        SignatureRequest.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<SignatureRequestDTO> createSignatureRequest(SignatureRequestDTO signatureRequestDTO) {
        return Mono.just(signatureRequestDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<SignatureRequestDTO> updateSignatureRequest(Long signatureRequestId, SignatureRequestDTO signatureRequestDTO) {
        return repository.findById(signatureRequestId)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature Request not found with ID: " + signatureRequestId)))
                .flatMap(existingSignatureRequest -> {
                    SignatureRequest updatedSignatureRequest = mapper.toEntity(signatureRequestDTO);
                    updatedSignatureRequest.setId(signatureRequestId);
                    return repository.save(updatedSignatureRequest);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteSignatureRequest(Long signatureRequestId) {
        return repository.findById(signatureRequestId)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature Request not found with ID: " + signatureRequestId)))
                .flatMap(signatureRequest -> repository.deleteById(signatureRequestId));
    }

    @Override
    public Mono<SignatureRequestDTO> getSignatureRequestById(Long signatureRequestId) {
        return repository.findById(signatureRequestId)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature Request not found with ID: " + signatureRequestId)))
                .map(mapper::toDTO);
    }
}