package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.DocumentReferenceMapper;
import com.catalis.commons.ecm.interfaces.dtos.DocumentReferenceDTO;
import com.catalis.commons.ecm.models.entities.DocumentReference;
import com.catalis.commons.ecm.models.repositories.DocumentReferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class DocumentReferenceServiceImpl implements DocumentReferenceService {

    @Autowired
    private DocumentReferenceRepository repository;

    @Autowired
    private DocumentReferenceMapper mapper;

    @Override
    public Mono<PaginationResponse<DocumentReferenceDTO>> filterDocumentReferences(FilterRequest<DocumentReferenceDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        DocumentReference.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<DocumentReferenceDTO> createDocumentReference(DocumentReferenceDTO documentReferenceDTO) {
        return Mono.just(documentReferenceDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<DocumentReferenceDTO> updateDocumentReference(Long documentReferenceId, DocumentReferenceDTO documentReferenceDTO) {
        return repository.findById(documentReferenceId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document Reference not found with ID: " + documentReferenceId)))
                .flatMap(existingDocumentReference -> {
                    DocumentReference updatedDocumentReference = mapper.toEntity(documentReferenceDTO);
                    updatedDocumentReference.setId(documentReferenceId);
                    return repository.save(updatedDocumentReference);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteDocumentReference(Long documentReferenceId) {
        return repository.findById(documentReferenceId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document Reference not found with ID: " + documentReferenceId)))
                .flatMap(documentReference -> repository.deleteById(documentReferenceId));
    }

    @Override
    public Mono<DocumentReferenceDTO> getDocumentReferenceById(Long documentReferenceId) {
        return repository.findById(documentReferenceId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document Reference not found with ID: " + documentReferenceId)))
                .map(mapper::toDTO);
    }
}