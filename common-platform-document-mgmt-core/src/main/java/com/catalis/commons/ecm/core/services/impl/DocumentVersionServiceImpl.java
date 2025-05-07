package com.catalis.commons.ecm.core.services.impl;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.DocumentVersionMapper;
import com.catalis.commons.ecm.core.services.DocumentVersionService;
import com.catalis.commons.ecm.interfaces.dtos.DocumentVersionDTO;
import com.catalis.commons.ecm.models.entities.DocumentVersion;
import com.catalis.commons.ecm.models.repositories.DocumentVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the DocumentVersionService interface.
 */
@Service
@Transactional
public class DocumentVersionServiceImpl implements DocumentVersionService {

    @Autowired
    private DocumentVersionRepository repository;

    @Autowired
    private DocumentVersionMapper mapper;

    @Override
    public Mono<DocumentVersionDTO> getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<DocumentVersionDTO>> filter(FilterRequest<DocumentVersionDTO> filterRequest) {
        return FilterUtils.createFilter(
                DocumentVersion.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<DocumentVersionDTO> update(DocumentVersionDTO documentVersion) {
        if (documentVersion.getId() == null) {
            return Mono.error(new IllegalArgumentException("ID cannot be null for update operation"));
        }

        return repository.findById(documentVersion.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Document version not found with ID: " + documentVersion.getId())))
                .flatMap(existingEntity -> {
                    DocumentVersion entityToUpdate = mapper.toEntity(documentVersion);
                    // Preserve created info
                    entityToUpdate.setCreatedAt(existingEntity.getCreatedAt());
                    entityToUpdate.setCreatedBy(existingEntity.getCreatedBy());
                    return repository.save(entityToUpdate);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<DocumentVersionDTO> create(DocumentVersionDTO documentVersion) {
        // Ensure ID is null for create operation
        documentVersion.setId(null);

        DocumentVersion entity = mapper.toEntity(documentVersion);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Document version not found with ID: " + id)))
                .flatMap(entity -> repository.delete(entity));
    }
}
