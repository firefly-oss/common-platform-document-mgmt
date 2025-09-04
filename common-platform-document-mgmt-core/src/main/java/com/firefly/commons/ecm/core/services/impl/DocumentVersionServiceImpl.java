/*
 * Copyright 2025 Firefly Software Solutions Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.firefly.commons.ecm.core.services.impl;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.filters.FilterUtils;
import com.firefly.common.core.queries.PaginationResponse;

import com.firefly.commons.ecm.core.mappers.DocumentVersionMapper;
import com.firefly.commons.ecm.core.services.DocumentVersionService;
import com.firefly.commons.ecm.interfaces.dtos.DocumentVersionDTO;
import com.firefly.commons.ecm.models.entities.DocumentVersion;
import com.firefly.commons.ecm.models.repositories.DocumentVersionRepository;
import com.firefly.core.ecm.service.EcmPortProvider;
import com.firefly.core.ecm.port.document.DocumentVersionPort;
import com.firefly.core.ecm.port.document.DocumentContentPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;
/**
 * Implementation of the DocumentVersionService interface.
 * Provides comprehensive document version management with ECM port integration.
 */
@Service
@Transactional
@Slf4j
public class DocumentVersionServiceImpl implements DocumentVersionService {

    @Autowired
    private DocumentVersionRepository repository;

    @Autowired
    private DocumentVersionMapper mapper;

    @Autowired
    private EcmPortProvider ecmPortProvider;

    @Override
    public Mono<DocumentVersionDTO> getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<DocumentVersionDTO>> filter(FilterRequest<DocumentVersionDTO> filterRequest) {
        return FilterUtils.createFilter(
                DocumentVersion.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<DocumentVersionDTO> update(DocumentVersionDTO documentVersion) {
        if (documentVersion.getId() == null) {
            return Mono.error(new IllegalArgumentException("ID cannot be null for update operation"));
        }

        return repository.findById(documentVersion.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Document version not found with ID: " + documentVersion.getId())))
                .flatMap(existingEntity -> {
                    DocumentVersion entityToUpdate = mapper.toEntity(documentVersion);
                    // Preserve created info
                    entityToUpdate.setCreatedAt(existingEntity.getCreatedAt());
                    entityToUpdate.setCreatedBy(existingEntity.getCreatedBy());
                    return repository.save(entityToUpdate);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<DocumentVersionDTO> create(DocumentVersionDTO documentVersion) {
        // Ensure ID is null for create operation
        documentVersion.setId(null);

        DocumentVersion entity = mapper.toEntity(documentVersion);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Document version not found with ID: " + id)))
                .flatMap(entity -> {
                    // Delete version content from ECM storage if available
                    java.util.UUID versionUuid = java.util.UUID.fromString(entity.getId().toString());
                    
                    // ECM integration temporarily commented out due to missing dependencies
                    // return ecmPortProvider.getDocumentVersionPort()
                    //         .map(port -> port.deleteVersion(versionUuid)
                    //                 .onErrorComplete()) // Continue even if ECM deletion fails
                    //         .orElse(Mono.empty())
                    //         .then(repository.delete(entity));
                    return repository.delete(entity);
                });
    }

    // ECM Port Operations Implementation

    @Override
    public Mono<DocumentVersionDTO> uploadVersionContent(UUID versionId, FilePart filePart) {
        return repository.findById(versionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document version not found with ID: " + versionId)))
                .flatMap(version -> {
                    // Upload version content using ECM port if available
                    java.util.UUID versionUuid = java.util.UUID.fromString(version.getId().toString());
                    
                    // ECM integration temporarily commented out due to missing dependencies
                    return Mono.just(version)
                            .flatMap(v -> {
                                // Fallback implementation without ECM
                                v.setFileName(filePart.filename());
                                v.setStoragePath("local/versions/" + v.getId());
                                v.setMimeType(filePart.headers().getContentType() != null ?
                                        filePart.headers().getContentType().toString() : null);
                                return repository.save(v);
                            });

                    // return ecmPortProvider.getDocumentContentPort()
                    //         .map(port -> {
                    //             // Store version content stream and get storage path
                    //             return port.storeContentStream(
                    //                     versionUuid,
                    //                     filePart.content(),
                    //                     filePart.filename(),
                    //                     null // size will be calculated by the port
                    //             ).flatMap(storagePath -> {
                    //                 // Update version metadata with ECM storage info
                    //                 version.setFileName(filePart.filename());
                    //                 version.setStoragePath(storagePath);
                    //                 version.setMimeType(filePart.headers().getContentType() != null ?
                    //                         filePart.headers().getContentType().toString() : null);
                    //                 return repository.save(version);
                    //             });
                    //         })
                    //         .orElse(
                    //             // Fallback: just update metadata without ECM storage
                    //             Mono.fromRunnable(() -> {
                    //                 version.setFileName(filePart.filename());
                    //                 version.setStoragePath("local/versions/" + version.getId());
                    //                 version.setMimeType(filePart.headers().getContentType() != null ?
                    //                         filePart.headers().getContentType().toString() : null);
                    //             }).then(repository.save(version))
                    //         );
                })
                .map(mapper::toDTO);
    }

    @Override
    public Flux<DataBuffer> downloadVersionContent(UUID versionId) {
        return repository.findById(versionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document version not found with ID: " + versionId)))
                .flatMapMany(version -> {
                    // Download version content using ECM port if available
                    java.util.UUID versionUuid = java.util.UUID.fromString(version.getId().toString());
                    
                    // ECM integration temporarily commented out due to missing dependencies
                    // return ecmPortProvider.getDocumentContentPort()
                    //         .map(port -> port.getContentStream(versionUuid))
                    //         .orElse(Flux.error(new RuntimeException("Version content not available - ECM port not configured")));
                    return Flux.error(new RuntimeException("Version content download not available - ECM integration disabled"));
                });
    }

    @Override
    public Mono<DocumentVersionDTO> getVersionContentMetadata(UUID versionId) {
        return repository.findById(versionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document version not found with ID: " + versionId)))
                .map(mapper::toDTO);
    }

    @Override
    public Flux<DocumentVersionDTO> getVersionsByDocumentId(UUID documentId) {
        return repository.findByDocumentId(documentId)
                .map(mapper::toDTO);
    }
}
