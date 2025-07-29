package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.DocumentTypeMapper;
import com.catalis.commons.ecm.interfaces.dtos.DocumentTypeDTO;
import com.catalis.commons.ecm.models.entities.DocumentType;
import com.catalis.commons.ecm.models.repositories.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class DocumentTypeServiceImpl implements DocumentTypeService {

    @Autowired
    private DocumentTypeRepository repository;

    @Autowired
    private DocumentTypeMapper mapper;

    @Override
    public Mono<PaginationResponse<DocumentTypeDTO>> filterDocumentTypes(FilterRequest<DocumentTypeDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        DocumentType.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<DocumentTypeDTO> createDocumentType(DocumentTypeDTO documentTypeDTO) {
        return Mono.just(documentTypeDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<DocumentTypeDTO> updateDocumentType(Long documentTypeId, DocumentTypeDTO documentTypeDTO) {
        return repository.findById(documentTypeId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document Type not found with ID: " + documentTypeId)))
                .flatMap(existingDocumentType -> {
                    DocumentType updatedDocumentType = mapper.toEntity(documentTypeDTO);
                    updatedDocumentType.setId(documentTypeId);
                    return repository.save(updatedDocumentType);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteDocumentType(Long documentTypeId) {
        return repository.findById(documentTypeId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document Type not found with ID: " + documentTypeId)))
                .flatMap(documentType -> repository.deleteById(documentTypeId));
    }

    @Override
    public Mono<DocumentTypeDTO> getDocumentTypeById(Long documentTypeId) {
        return repository.findById(documentTypeId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document Type not found with ID: " + documentTypeId)))
                .map(mapper::toDTO);
    }
}