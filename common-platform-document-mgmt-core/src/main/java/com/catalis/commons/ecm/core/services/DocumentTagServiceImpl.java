package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.DocumentTagMapper;
import com.catalis.commons.ecm.interfaces.dtos.DocumentTagDTO;
import com.catalis.commons.ecm.models.entities.DocumentTag;
import com.catalis.commons.ecm.models.repositories.DocumentTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class DocumentTagServiceImpl implements DocumentTagService {

    @Autowired
    private DocumentTagRepository repository;

    @Autowired
    private DocumentTagMapper mapper;

    @Override
    public Mono<PaginationResponse<DocumentTagDTO>> filterDocumentTags(FilterRequest<DocumentTagDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        DocumentTag.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<DocumentTagDTO> createDocumentTag(DocumentTagDTO documentTagDTO) {
        return Mono.just(documentTagDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<DocumentTagDTO> updateDocumentTag(Long documentTagId, DocumentTagDTO documentTagDTO) {
        return repository.findById(documentTagId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document Tag not found with ID: " + documentTagId)))
                .flatMap(existingDocumentTag -> {
                    DocumentTag updatedDocumentTag = mapper.toEntity(documentTagDTO);
                    updatedDocumentTag.setId(documentTagId);
                    return repository.save(updatedDocumentTag);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteDocumentTag(Long documentTagId) {
        return repository.findById(documentTagId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document Tag not found with ID: " + documentTagId)))
                .flatMap(documentTag -> repository.deleteById(documentTagId));
    }

    @Override
    public Mono<DocumentTagDTO> getDocumentTagById(Long documentTagId) {
        return repository.findById(documentTagId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document Tag not found with ID: " + documentTagId)))
                .map(mapper::toDTO);
    }
}