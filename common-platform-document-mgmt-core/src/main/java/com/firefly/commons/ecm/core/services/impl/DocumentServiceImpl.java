package com.firefly.commons.ecm.core.services.impl;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.filters.FilterUtils;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.core.mappers.DocumentMapper;
import com.firefly.commons.ecm.core.services.DocumentService;
import com.firefly.commons.ecm.interfaces.dtos.DocumentDTO;
import com.firefly.commons.ecm.models.entities.Document;
import com.firefly.commons.ecm.models.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the DocumentService interface.
 */
@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository repository;

    @Autowired
    private DocumentMapper mapper;

    @Override
    public Mono<DocumentDTO> getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<DocumentDTO>> filter(FilterRequest<DocumentDTO> filterRequest) {
        return FilterUtils.createFilter(
                Document.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<DocumentDTO> update(DocumentDTO document) {
        if (document.getId() == null) {
            return Mono.error(new IllegalArgumentException("ID cannot be null for update operation"));
        }

        return repository.findById(document.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Document not found with ID: " + document.getId())))
                .flatMap(existingEntity -> {
                    Document entityToUpdate = mapper.toEntity(document);
                    // Preserve created info
                    entityToUpdate.setCreatedAt(existingEntity.getCreatedAt());
                    entityToUpdate.setCreatedBy(existingEntity.getCreatedBy());
                    return repository.save(entityToUpdate);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<DocumentDTO> create(DocumentDTO document) {
        // Ensure ID is null for create operation
        document.setId(null);

        Document entity = mapper.toEntity(document);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Document not found with ID: " + id)))
                .flatMap(entity -> repository.delete(entity));
    }
}
