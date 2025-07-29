package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.SignatureProofMapper;
import com.catalis.commons.ecm.interfaces.dtos.SignatureProofDTO;
import com.catalis.commons.ecm.models.entities.SignatureProof;
import com.catalis.commons.ecm.models.repositories.SignatureProofRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class SignatureProofServiceImpl implements SignatureProofService {

    @Autowired
    private SignatureProofRepository repository;

    @Autowired
    private SignatureProofMapper mapper;

    @Override
    public Mono<PaginationResponse<SignatureProofDTO>> filterSignatureProofs(FilterRequest<SignatureProofDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        SignatureProof.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<SignatureProofDTO> createSignatureProof(SignatureProofDTO signatureProofDTO) {
        return Mono.just(signatureProofDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<SignatureProofDTO> updateSignatureProof(Long signatureProofId, SignatureProofDTO signatureProofDTO) {
        return repository.findById(signatureProofId)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature Proof not found with ID: " + signatureProofId)))
                .flatMap(existingSignatureProof -> {
                    SignatureProof updatedSignatureProof = mapper.toEntity(signatureProofDTO);
                    updatedSignatureProof.setId(signatureProofId);
                    return repository.save(updatedSignatureProof);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteSignatureProof(Long signatureProofId) {
        return repository.findById(signatureProofId)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature Proof not found with ID: " + signatureProofId)))
                .flatMap(signatureProof -> repository.deleteById(signatureProofId));
    }

    @Override
    public Mono<SignatureProofDTO> getSignatureProofById(Long signatureProofId) {
        return repository.findById(signatureProofId)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature Proof not found with ID: " + signatureProofId)))
                .map(mapper::toDTO);
    }
}