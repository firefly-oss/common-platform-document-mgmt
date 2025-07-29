package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.DocumentVersionMapper;
import com.catalis.commons.ecm.interfaces.dtos.DocumentVersionDTO;
import com.catalis.commons.ecm.models.entities.DocumentVersion;
import com.catalis.commons.ecm.models.repositories.DocumentVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class DocumentVersionServiceImpl implements DocumentVersionService {

    @Autowired
    private DocumentVersionRepository repository;

    @Autowired
    private DocumentVersionMapper mapper;

    @Override
    public Mono<PaginationResponse<DocumentVersionDTO>> filterDocumentVersions(FilterRequest<DocumentVersionDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        DocumentVersion.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<DocumentVersionDTO> createDocumentVersion(DocumentVersionDTO documentVersionDTO) {
        return Mono.just(documentVersionDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<DocumentVersionDTO> updateDocumentVersion(Long documentVersionId, DocumentVersionDTO documentVersionDTO) {
        return repository.findById(documentVersionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document Version not found with ID: " + documentVersionId)))
                .flatMap(existingDocumentVersion -> {
                    DocumentVersion updatedDocumentVersion = mapper.toEntity(documentVersionDTO);
                    updatedDocumentVersion.setId(documentVersionId);
                    return repository.save(updatedDocumentVersion);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteDocumentVersion(Long documentVersionId) {
        return repository.findById(documentVersionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document Version not found with ID: " + documentVersionId)))
                .flatMap(documentVersion -> repository.deleteById(documentVersionId));
    }

    @Override
    public Mono<DocumentVersionDTO> getDocumentVersionById(Long documentVersionId) {
        return repository.findById(documentVersionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document Version not found with ID: " + documentVersionId)))
                .map(mapper::toDTO);
    }
}