package com.firefly.commons.ecm.core.services.impl;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.filters.FilterUtils;
import com.firefly.common.core.queries.PaginationResponse;

import com.firefly.commons.ecm.core.mappers.DocumentMetadataMapper;
import com.firefly.commons.ecm.core.services.DocumentMetadataService;
import com.firefly.commons.ecm.interfaces.dtos.DocumentMetadataDTO;
import com.firefly.commons.ecm.models.entities.DocumentMetadata;
import com.firefly.commons.ecm.models.repositories.DocumentMetadataRepository;
import com.firefly.core.ecm.service.EcmPortProvider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import java.util.UUID;
/**
 * Implementation of the DocumentMetadataService interface.
 * Provides comprehensive document metadata management with ECM port integration.
 */
@Service
@Transactional
@Slf4j
public class DocumentMetadataServiceImpl implements DocumentMetadataService {

    @Autowired
    private DocumentMetadataRepository repository;

    @Autowired
    private DocumentMetadataMapper mapper;

    @Autowired
    private EcmPortProvider ecmPortProvider;

    @Override
    public Mono<DocumentMetadataDTO> getById(UUID id) {
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
        log.debug("Creating document metadata for document ID: {}", documentMetadata.getDocumentId());

        // Ensure ID is null for create operation
        documentMetadata.setId(null);

        DocumentMetadata entity = mapper.toEntity(documentMetadata);
        return repository.save(entity)
                .doOnSuccess(savedEntity -> log.info("Document metadata created successfully with ID: {}", savedEntity.getId()))
                .doOnError(error -> log.error("Failed to create document metadata: {}", error.getMessage(), error))
                // Note: ECM metadata integration would be implemented here if needed
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID id) {
        log.debug("Deleting document metadata with ID: {}", id);

        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Document metadata not found with ID: " + id)))
                .flatMap(entity -> {
                    log.info("Deleting document metadata: {} for document ID: {}", entity.getKey(), entity.getDocumentId());

                    // Note: ECM metadata removal would be implemented here if needed
                    return repository.delete(entity)
                            .doOnSuccess(result -> log.info("Document metadata deleted successfully: {}", id))
                            .doOnError(error -> log.error("Failed to delete document metadata {}: {}", id, error.getMessage(), error));
                });
    }
}
