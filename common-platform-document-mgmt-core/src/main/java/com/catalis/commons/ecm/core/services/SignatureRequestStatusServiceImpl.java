package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.SignatureRequestStatusMapper;
import com.catalis.commons.ecm.interfaces.dtos.SignatureRequestStatusDTO;
import com.catalis.commons.ecm.models.entities.SignatureRequestStatus;
import com.catalis.commons.ecm.models.repositories.SignatureRequestStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class SignatureRequestStatusServiceImpl implements SignatureRequestStatusService {

    @Autowired
    private SignatureRequestStatusRepository repository;

    @Autowired
    private SignatureRequestStatusMapper mapper;

    @Override
    public Mono<PaginationResponse<SignatureRequestStatusDTO>> filterSignatureRequestStatuses(FilterRequest<SignatureRequestStatusDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        SignatureRequestStatus.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<SignatureRequestStatusDTO> createSignatureRequestStatus(SignatureRequestStatusDTO signatureRequestStatusDTO) {
        return Mono.just(signatureRequestStatusDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<SignatureRequestStatusDTO> updateSignatureRequestStatus(Long signatureRequestStatusId, SignatureRequestStatusDTO signatureRequestStatusDTO) {
        return repository.findById(signatureRequestStatusId)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature Request Status not found with ID: " + signatureRequestStatusId)))
                .flatMap(existingSignatureRequestStatus -> {
                    SignatureRequestStatus updatedSignatureRequestStatus = mapper.toEntity(signatureRequestStatusDTO);
                    updatedSignatureRequestStatus.setId(signatureRequestStatusId);
                    return repository.save(updatedSignatureRequestStatus);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteSignatureRequestStatus(Long signatureRequestStatusId) {
        return repository.findById(signatureRequestStatusId)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature Request Status not found with ID: " + signatureRequestStatusId)))
                .flatMap(signatureRequestStatus -> repository.deleteById(signatureRequestStatusId));
    }

    @Override
    public Mono<SignatureRequestStatusDTO> getSignatureRequestStatusById(Long signatureRequestStatusId) {
        return repository.findById(signatureRequestStatusId)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature Request Status not found with ID: " + signatureRequestStatusId)))
                .map(mapper::toDTO);
    }
}