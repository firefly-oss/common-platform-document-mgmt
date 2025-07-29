package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.DocumentMapper;
import com.catalis.commons.ecm.interfaces.dtos.DocumentDTO;
import com.catalis.commons.ecm.models.entities.Document;
import com.catalis.commons.ecm.models.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository repository;

    @Autowired
    private DocumentMapper mapper;

    @Override
    public Mono<PaginationResponse<DocumentDTO>> filterDocuments(FilterRequest<DocumentDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        Document.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<DocumentDTO> createDocument(DocumentDTO documentDTO) {
        return Mono.just(documentDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<DocumentDTO> updateDocument(Long documentId, DocumentDTO documentDTO) {
        return repository.findById(documentId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document not found with ID: " + documentId)))
                .flatMap(existingDocument -> {
                    Document updatedDocument = mapper.toEntity(documentDTO);
                    updatedDocument.setId(documentId);
                    return repository.save(updatedDocument);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteDocument(Long documentId) {
        return repository.findById(documentId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document not found with ID: " + documentId)))
                .flatMap(document -> repository.deleteById(documentId));
    }

    @Override
    public Mono<DocumentDTO> getDocumentById(Long documentId) {
        return repository.findById(documentId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document not found with ID: " + documentId)))
                .map(mapper::toDTO);
    }
}