package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.DocumentStatusMapper;
import com.catalis.commons.ecm.interfaces.dtos.DocumentStatusDTO;
import com.catalis.commons.ecm.models.entities.DocumentStatus;
import com.catalis.commons.ecm.models.repositories.DocumentStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class DocumentStatusServiceImpl implements DocumentStatusService {

    @Autowired
    private DocumentStatusRepository repository;

    @Autowired
    private DocumentStatusMapper mapper;

    @Override
    public Mono<PaginationResponse<DocumentStatusDTO>> filterDocumentStatuses(FilterRequest<DocumentStatusDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        DocumentStatus.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<DocumentStatusDTO> createDocumentStatus(DocumentStatusDTO documentStatusDTO) {
        return Mono.just(documentStatusDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<DocumentStatusDTO> updateDocumentStatus(Long documentStatusId, DocumentStatusDTO documentStatusDTO) {
        return repository.findById(documentStatusId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document Status not found with ID: " + documentStatusId)))
                .flatMap(existingDocumentStatus -> {
                    DocumentStatus updatedDocumentStatus = mapper.toEntity(documentStatusDTO);
                    updatedDocumentStatus.setId(documentStatusId);
                    return repository.save(updatedDocumentStatus);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteDocumentStatus(Long documentStatusId) {
        return repository.findById(documentStatusId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document Status not found with ID: " + documentStatusId)))
                .flatMap(documentStatus -> repository.deleteById(documentStatusId));
    }

    @Override
    public Mono<DocumentStatusDTO> getDocumentStatusById(Long documentStatusId) {
        return repository.findById(documentStatusId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document Status not found with ID: " + documentStatusId)))
                .map(mapper::toDTO);
    }
}