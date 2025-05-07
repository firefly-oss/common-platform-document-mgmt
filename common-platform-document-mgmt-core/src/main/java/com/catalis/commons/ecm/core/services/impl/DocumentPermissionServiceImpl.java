package com.catalis.commons.ecm.core.services.impl;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.DocumentPermissionMapper;
import com.catalis.commons.ecm.core.services.DocumentPermissionService;
import com.catalis.commons.ecm.interfaces.dtos.DocumentPermissionDTO;
import com.catalis.commons.ecm.models.entities.DocumentPermission;
import com.catalis.commons.ecm.models.repositories.DocumentPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the DocumentPermissionService interface.
 */
@Service
@Transactional
public class DocumentPermissionServiceImpl implements DocumentPermissionService {

    @Autowired
    private DocumentPermissionRepository repository;

    @Autowired
    private DocumentPermissionMapper mapper;

    @Override
    public Mono<DocumentPermissionDTO> getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<DocumentPermissionDTO>> filter(FilterRequest<DocumentPermissionDTO> filterRequest) {
        return FilterUtils.createFilter(
                DocumentPermission.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<DocumentPermissionDTO> update(DocumentPermissionDTO documentPermission) {
        if (documentPermission.getId() == null) {
            return Mono.error(new IllegalArgumentException("ID cannot be null for update operation"));
        }

        return repository.findById(documentPermission.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Document permission not found with ID: " + documentPermission.getId())))
                .flatMap(existingEntity -> {
                    DocumentPermission entityToUpdate = mapper.toEntity(documentPermission);
                    // Preserve created info
                    entityToUpdate.setCreatedAt(existingEntity.getCreatedAt());
                    entityToUpdate.setCreatedBy(existingEntity.getCreatedBy());
                    return repository.save(entityToUpdate);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<DocumentPermissionDTO> create(DocumentPermissionDTO documentPermission) {
        // Ensure ID is null for create operation
        documentPermission.setId(null);

        DocumentPermission entity = mapper.toEntity(documentPermission);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Document permission not found with ID: " + id)))
                .flatMap(entity -> repository.delete(entity));
    }
}
