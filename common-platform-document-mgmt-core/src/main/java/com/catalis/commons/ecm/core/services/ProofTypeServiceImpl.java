package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.ProofTypeMapper;
import com.catalis.commons.ecm.interfaces.dtos.ProofTypeDTO;
import com.catalis.commons.ecm.models.entities.ProofType;
import com.catalis.commons.ecm.models.repositories.ProofTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class ProofTypeServiceImpl implements ProofTypeService {

    @Autowired
    private ProofTypeRepository repository;

    @Autowired
    private ProofTypeMapper mapper;

    @Override
    public Mono<PaginationResponse<ProofTypeDTO>> filterProofTypes(FilterRequest<ProofTypeDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        ProofType.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<ProofTypeDTO> createProofType(ProofTypeDTO proofTypeDTO) {
        return Mono.just(proofTypeDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<ProofTypeDTO> updateProofType(Long proofTypeId, ProofTypeDTO proofTypeDTO) {
        return repository.findById(proofTypeId)
                .switchIfEmpty(Mono.error(new RuntimeException("Proof Type not found with ID: " + proofTypeId)))
                .flatMap(existingProofType -> {
                    ProofType updatedProofType = mapper.toEntity(proofTypeDTO);
                    updatedProofType.setId(proofTypeId);
                    return repository.save(updatedProofType);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteProofType(Long proofTypeId) {
        return repository.findById(proofTypeId)
                .switchIfEmpty(Mono.error(new RuntimeException("Proof Type not found with ID: " + proofTypeId)))
                .flatMap(proofType -> repository.deleteById(proofTypeId));
    }

    @Override
    public Mono<ProofTypeDTO> getProofTypeById(Long proofTypeId) {
        return repository.findById(proofTypeId)
                .switchIfEmpty(Mono.error(new RuntimeException("Proof Type not found with ID: " + proofTypeId)))
                .map(mapper::toDTO);
    }
}