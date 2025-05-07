package com.catalis.commons.ecm.core.services.impl;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.DocumentTagMapper;
import com.catalis.commons.ecm.core.services.DocumentTagService;
import com.catalis.commons.ecm.interfaces.dtos.DocumentTagDTO;
import com.catalis.commons.ecm.models.entities.DocumentTag;
import com.catalis.commons.ecm.models.repositories.DocumentTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the DocumentTagService interface.
 */
@Service
@Transactional
public class DocumentTagServiceImpl implements DocumentTagService {

    @Autowired
    private DocumentTagRepository repository;

    @Autowired
    private DocumentTagMapper mapper;

    @Override
    public Mono<DocumentTagDTO> getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<DocumentTagDTO>> filter(FilterRequest<DocumentTagDTO> filterRequest) {
        return FilterUtils.createFilter(
                DocumentTag.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<DocumentTagDTO> update(DocumentTagDTO documentTag) {
        if (documentTag.getId() == null) {
            return Mono.error(new IllegalArgumentException("ID cannot be null for update operation"));
        }

        return repository.findById(documentTag.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Document tag not found with ID: " + documentTag.getId())))
                .flatMap(existingEntity -> {
                    DocumentTag entityToUpdate = mapper.toEntity(documentTag);
                    // Preserve created info
                    entityToUpdate.setCreatedAt(existingEntity.getCreatedAt());
                    entityToUpdate.setCreatedBy(existingEntity.getCreatedBy());
                    return repository.save(entityToUpdate);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<DocumentTagDTO> create(DocumentTagDTO documentTag) {
        // Ensure ID is null for create operation
        documentTag.setId(null);

        DocumentTag entity = mapper.toEntity(documentTag);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Document tag not found with ID: " + id)))
                .flatMap(entity -> repository.delete(entity));
    }
}
