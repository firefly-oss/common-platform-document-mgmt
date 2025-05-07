package com.catalis.commons.ecm.core.services.impl;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.DocumentMetadataMapper;
import com.catalis.commons.ecm.core.services.DocumentMetadataService;
import com.catalis.commons.ecm.interfaces.dtos.DocumentMetadataDTO;
import com.catalis.commons.ecm.models.entities.DocumentMetadata;
import com.catalis.commons.ecm.models.repositories.DocumentMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the DocumentMetadataService interface.
 */
@Service
@Transactional
public class DocumentMetadataServiceImpl implements DocumentMetadataService {

    @Autowired
    private DocumentMetadataRepository repository;

    @Autowired
    private DocumentMetadataMapper mapper;

    @Override
    public Mono<DocumentMetadataDTO> getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<DocumentMetadataDTO>> filter(FilterRequest<DocumentMetadataDTO> filterRequest) {
        return FilterUtils.createFilter(
                DocumentMetadata.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<DocumentMetadataDTO> update(DocumentMetadataDTO documentMetadata) {
        if (documentMetadata.getId() == null) {
            return Mono.error(new IllegalArgumentException("ID cannot be null for update operation"));
        }

        return repository.findById(documentMetadata.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Document metadata not found with ID: " + documentMetadata.getId())))
                .flatMap(existingEntity -> {
                    DocumentMetadata entityToUpdate = mapper.toEntity(documentMetadata);
                    // Preserve created info
                    entityToUpdate.setCreatedAt(existingEntity.getCreatedAt());
                    entityToUpdate.setCreatedBy(existingEntity.getCreatedBy());
                    return repository.save(entityToUpdate);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<DocumentMetadataDTO> create(DocumentMetadataDTO documentMetadata) {
        // Ensure ID is null for create operation
        documentMetadata.setId(null);

        DocumentMetadata entity = mapper.toEntity(documentMetadata);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Document metadata not found with ID: " + id)))
                .flatMap(entity -> repository.delete(entity));
    }
}
