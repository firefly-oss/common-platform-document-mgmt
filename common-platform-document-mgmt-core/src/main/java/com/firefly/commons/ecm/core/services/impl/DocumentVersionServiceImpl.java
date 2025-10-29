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
        log.debug("Deleting document version with ID: {}", id);
        
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Document version not found with ID: " + id)))
                .flatMap(entity -> {
                    log.info("Deleting version: {} (ID: {})", entity.getFileName(), entity.getId());
                    
                    // Delete version content from ECM storage if available
                    java.util.UUID versionUuid = java.util.UUID.fromString(entity.getId().toString());
                    
                    return ecmPortProvider.getDocumentVersionPort()
                            .map(port -> {
                                log.debug("Using ECM DocumentVersionPort to delete version content");
                                return port.deleteVersion(versionUuid)
                                        .doOnSuccess(result -> log.debug("Version content deleted from ECM storage"))
                                        .doOnError(error -> log.warn("Failed to delete version content from ECM storage: {}", error.getMessage()))
                                        .onErrorComplete() // Continue even if ECM deletion fails
                                        .then(repository.delete(entity));
                            })
                            .orElse(
                                    // Fallback: delete from database only
                                    repository.delete(entity)
                                            .doOnSuccess(result -> log.debug("Version deleted from database (ECM port not available)"))
                            )
                            .doOnSuccess(result -> log.info("Document version deleted successfully: {}", entity.getId()))
                            .doOnError(error -> log.error("Failed to delete document version {}: {}", entity.getId(), error.getMessage(), error));
                });
    }

    // ECM Port Operations Implementation

    @Override
    public Mono<DocumentVersionDTO> uploadVersionContent(UUID versionId, FilePart filePart) {
        log.debug("Uploading content for version ID: {} with filename: {}", versionId, filePart.filename());
        
        return repository.findById(versionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document version not found with ID: " + versionId)))
                .flatMap(version -> {
                    log.info("Uploading content for version: {} (ID: {}), filename: {}",
                            version.getVersionNumber(), version.getId(), filePart.filename());
                    
                    // Upload version content using ECM port if available
                    java.util.UUID versionUuid = java.util.UUID.fromString(version.getId().toString());
                    
                    return ecmPortProvider.getDocumentContentPort()
                            .map(port -> {
                                log.debug("Using ECM DocumentContentPort to store version content");
                                
                                // Convert FilePart content to byte array for ECM storage
                                return filePart.content()
                                        .collectList()
                                        .map(dataBuffers -> {
                                            // Convert DataBuffer list to byte array
                                            int totalSize = dataBuffers.stream().mapToInt(DataBuffer::readableByteCount).sum();
                                            byte[] bytes = new byte[totalSize];
                                            int offset = 0;
                                            for (DataBuffer buffer : dataBuffers) {
                                                int length = buffer.readableByteCount();
                                                buffer.read(bytes, offset, length);
                                                offset += length;
                                            }
                                            return bytes;
                                        })
                                        .flatMap(contentBytes -> {
                                            String mimeType = filePart.headers().getContentType() != null
                                                    ? filePart.headers().getContentType().toString()
                                                    : "application/octet-stream";
                                            return port.storeContent(versionUuid, contentBytes, mimeType);
                                        })
                                        .flatMap(storagePath -> {
                                            log.debug("Version content stored successfully at path: {}", storagePath);
                                            
                                            // Update version metadata with ECM storage info
                                            version.setFileName(filePart.filename());
                                            version.setStoragePath(storagePath);
                                            version.setMimeType(filePart.headers().getContentType() != null ?
                                                    filePart.headers().getContentType().toString() : null);
                                            
                                            // Save updated version metadata
                                            return repository.save(version)
                                                    .doOnSuccess(savedVersion -> log.info("Version content uploaded successfully for ID: {}", savedVersion.getId()))
                                                    .doOnError(error -> log.error("Failed to save version metadata after content upload: {}", error.getMessage(), error));
                                        })
                                        .doOnError(error -> {
                                            log.error("Failed to upload version content for version ID {}: {}", versionId, error.getMessage(), error);
                                        });
                            })
                            .orElseThrow(() -> {
                                log.warn("Version content upload not available - ECM DocumentContentPort not configured");
                                return new RuntimeException("Version content upload requires ECM DocumentContentPort to be configured");
                            });
                })
                .map(mapper::toDTO);
    }

    @Override
    public Flux<DataBuffer> downloadVersionContent(UUID versionId) {
        log.debug("Downloading content for version ID: {}", versionId);
        
        return repository.findById(versionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document version not found with ID: " + versionId)))
                .flatMapMany(version -> {
                    log.info("Downloading content for version: {} (ID: {})", version.getVersionNumber(), version.getId());
                    
                    // Download version content using ECM port if available
                    java.util.UUID versionUuid = java.util.UUID.fromString(version.getId().toString());
                    
                    return ecmPortProvider.getDocumentContentPort()
                            .map(port -> {
                                log.debug("Using ECM DocumentContentPort to download version content");
                                return port.getContentStream(versionUuid)
                                        .doOnNext(buffer -> log.trace("Downloaded version content buffer of size: {}", buffer.readableByteCount()))
                                        .doOnComplete(() -> log.debug("Version content download completed for version ID: {}", versionId))
                                        .doOnError(error -> {
                                            log.error("Failed to download version content for version ID {}: {}", versionId, error.getMessage(), error);
                                        });
                            })
                            .orElseThrow(() -> {
                                log.warn("Version content not available - ECM DocumentContentPort not configured");
                                return new RuntimeException("Version content download requires ECM DocumentContentPort to be configured");
                            });
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
