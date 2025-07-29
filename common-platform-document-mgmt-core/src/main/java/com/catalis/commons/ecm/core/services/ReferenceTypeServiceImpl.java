package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.ReferenceTypeMapper;
import com.catalis.commons.ecm.interfaces.dtos.ReferenceTypeDTO;
import com.catalis.commons.ecm.models.entities.ReferenceType;
import com.catalis.commons.ecm.models.repositories.ReferenceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class ReferenceTypeServiceImpl implements ReferenceTypeService {

    @Autowired
    private ReferenceTypeRepository repository;

    @Autowired
    private ReferenceTypeMapper mapper;

    @Override
    public Mono<PaginationResponse<ReferenceTypeDTO>> filterReferenceTypes(FilterRequest<ReferenceTypeDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        ReferenceType.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<ReferenceTypeDTO> createReferenceType(ReferenceTypeDTO referenceTypeDTO) {
        return Mono.just(referenceTypeDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<ReferenceTypeDTO> updateReferenceType(Long referenceTypeId, ReferenceTypeDTO referenceTypeDTO) {
        return repository.findById(referenceTypeId)
                .switchIfEmpty(Mono.error(new RuntimeException("Reference Type not found with ID: " + referenceTypeId)))
                .flatMap(existingReferenceType -> {
                    ReferenceType updatedReferenceType = mapper.toEntity(referenceTypeDTO);
                    updatedReferenceType.setId(referenceTypeId);
                    return repository.save(updatedReferenceType);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteReferenceType(Long referenceTypeId) {
        return repository.findById(referenceTypeId)
                .switchIfEmpty(Mono.error(new RuntimeException("Reference Type not found with ID: " + referenceTypeId)))
                .flatMap(referenceType -> repository.deleteById(referenceTypeId));
    }

    @Override
    public Mono<ReferenceTypeDTO> getReferenceTypeById(Long referenceTypeId) {
        return repository.findById(referenceTypeId)
                .switchIfEmpty(Mono.error(new RuntimeException("Reference Type not found with ID: " + referenceTypeId)))
                .map(mapper::toDTO);
    }
}