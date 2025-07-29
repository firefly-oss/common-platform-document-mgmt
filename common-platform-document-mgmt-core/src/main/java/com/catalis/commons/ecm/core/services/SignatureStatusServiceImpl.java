package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.SignatureStatusMapper;
import com.catalis.commons.ecm.interfaces.dtos.SignatureStatusDTO;
import com.catalis.commons.ecm.models.entities.SignatureStatus;
import com.catalis.commons.ecm.models.repositories.SignatureStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class SignatureStatusServiceImpl implements SignatureStatusService {

    @Autowired
    private SignatureStatusRepository repository;

    @Autowired
    private SignatureStatusMapper mapper;

    @Override
    public Mono<PaginationResponse<SignatureStatusDTO>> filterSignatureStatuses(FilterRequest<SignatureStatusDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        SignatureStatus.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<SignatureStatusDTO> createSignatureStatus(SignatureStatusDTO signatureStatusDTO) {
        return Mono.just(signatureStatusDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<SignatureStatusDTO> updateSignatureStatus(Long signatureStatusId, SignatureStatusDTO signatureStatusDTO) {
        return repository.findById(signatureStatusId)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature Status not found with ID: " + signatureStatusId)))
                .flatMap(existingSignatureStatus -> {
                    SignatureStatus updatedSignatureStatus = mapper.toEntity(signatureStatusDTO);
                    updatedSignatureStatus.setId(signatureStatusId);
                    return repository.save(updatedSignatureStatus);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteSignatureStatus(Long signatureStatusId) {
        return repository.findById(signatureStatusId)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature Status not found with ID: " + signatureStatusId)))
                .flatMap(signatureStatus -> repository.deleteById(signatureStatusId));
    }

    @Override
    public Mono<SignatureStatusDTO> getSignatureStatusById(Long signatureStatusId) {
        return repository.findById(signatureStatusId)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature Status not found with ID: " + signatureStatusId)))
                .map(mapper::toDTO);
    }
}