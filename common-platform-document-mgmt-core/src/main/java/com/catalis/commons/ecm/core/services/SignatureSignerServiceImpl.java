package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.SignatureSignerMapper;
import com.catalis.commons.ecm.interfaces.dtos.SignatureSignerDTO;
import com.catalis.commons.ecm.models.entities.SignatureSigner;
import com.catalis.commons.ecm.models.repositories.SignatureSignerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class SignatureSignerServiceImpl implements SignatureSignerService {

    @Autowired
    private SignatureSignerRepository repository;

    @Autowired
    private SignatureSignerMapper mapper;

    @Override
    public Mono<PaginationResponse<SignatureSignerDTO>> filterSignatureSigners(FilterRequest<SignatureSignerDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        SignatureSigner.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<SignatureSignerDTO> createSignatureSigner(SignatureSignerDTO signatureSignerDTO) {
        return Mono.just(signatureSignerDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<SignatureSignerDTO> updateSignatureSigner(Long signatureSignerId, SignatureSignerDTO signatureSignerDTO) {
        return repository.findById(signatureSignerId)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature Signer not found with ID: " + signatureSignerId)))
                .flatMap(existingSignatureSigner -> {
                    SignatureSigner updatedSignatureSigner = mapper.toEntity(signatureSignerDTO);
                    updatedSignatureSigner.setId(signatureSignerId);
                    return repository.save(updatedSignatureSigner);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteSignatureSigner(Long signatureSignerId) {
        return repository.findById(signatureSignerId)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature Signer not found with ID: " + signatureSignerId)))
                .flatMap(signatureSigner -> repository.deleteById(signatureSignerId));
    }

    @Override
    public Mono<SignatureSignerDTO> getSignatureSignerById(Long signatureSignerId) {
        return repository.findById(signatureSignerId)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature Signer not found with ID: " + signatureSignerId)))
                .map(mapper::toDTO);
    }
}