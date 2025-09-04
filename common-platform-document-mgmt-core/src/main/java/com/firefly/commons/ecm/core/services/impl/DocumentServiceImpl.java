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

import com.firefly.commons.ecm.core.mappers.DocumentMapper;
import com.firefly.commons.ecm.core.services.DocumentService;
import com.firefly.commons.ecm.interfaces.dtos.DocumentDTO;
import com.firefly.commons.ecm.models.entities.Document;
import com.firefly.commons.ecm.models.repositories.DocumentRepository;
import com.firefly.core.ecm.service.EcmPortProvider;
import com.firefly.core.ecm.port.document.DocumentContentPort;
import com.firefly.core.ecm.port.document.DocumentVersionPort;

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
 * Implementation of the DocumentService interface.
 * Provides comprehensive document management with ECM port integration.
 */
@Service
@Transactional
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository repository;

    @Autowired
    private DocumentMapper mapper;

    @Autowired
    private EcmPortProvider ecmPortProvider;

    @Override
    public Mono<DocumentDTO> getById(UUID id) {
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
        log.debug("Creating new document: {}", document.getName());

        // Ensure ID is null for create operation
        document.setId(null);

        Document entity = mapper.toEntity(document);
        return repository.save(entity)
                .doOnSuccess(savedEntity -> log.info("Document created successfully with ID: {}", savedEntity.getId()))
                .doOnError(error -> log.error("Failed to create document: {}", error.getMessage(), error))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID id) {
        log.debug("Deleting document with ID: {}", id);

        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Document not found with ID: " + id)))
                .flatMap(entity -> {
                    log.info("Deleting document: {} (ID: {})", entity.getName(), entity.getId());

                    // Delete content from ECM storage if available
                    return ecmPortProvider.getDocumentContentPort()
                            .map(port -> {
                                log.debug("Deleting document content from ECM storage for document ID: {}", entity.getId());
                                return port.deleteContent(java.util.UUID.fromString(entity.getId().toString()))
                                        .doOnSuccess(result -> log.debug("Document content deleted from ECM storage"))
                                        .doOnError(error -> log.warn("Failed to delete document content from ECM storage: {}", error.getMessage()))
                                        .onErrorComplete() // Continue even if content deletion fails
                                        .then(repository.delete(entity));
                            })
                            .orElse(repository.delete(entity))
                            .doOnSuccess(result -> log.info("Document deleted successfully: {}", entity.getId()))
                            .doOnError(error -> log.error("Failed to delete document {}: {}", entity.getId(), error.getMessage(), error));
                });
    }

    // ECM Port Operations Implementation

    @Override
    public Mono<DocumentDTO> uploadContent(UUID documentId, FilePart filePart) {
        log.debug("Uploading content for document ID: {} with filename: {}", documentId, filePart.filename());

        return repository.findById(documentId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document not found with ID: " + documentId)))
                .flatMap(document -> {
                    log.info("Uploading content for document: {} (ID: {}), filename: {}",
                            document.getName(), document.getId(), filePart.filename());

                    // Upload content using ECM port if available
                    java.util.UUID documentUuid = java.util.UUID.fromString(document.getId().toString());

                    return ecmPortProvider.getDocumentContentPort()
                            .map(port -> {
                                log.debug("Using ECM DocumentContentPort to store content");
                                // Store content stream and get storage path
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
                                            // Store content using ECM port (returns Mono<String>)
                                            return port.storeContent(documentUuid, contentBytes, filePart.filename());
                                        })
                                        .flatMap(storagePath -> {
                                    log.debug("Content stored successfully at path: {}", storagePath);

                                    // Update document metadata with ECM storage info
                                    document.setFileName(filePart.filename());
                                    document.setStoragePath(storagePath);
                                    document.setMimeType(filePart.headers().getContentType() != null ?
                                            filePart.headers().getContentType().toString() : null);
                                    // Save updated document metadata
                                    return repository.save(document)
                                            .doOnSuccess(savedDoc -> log.info("Document content uploaded successfully for ID: {}", savedDoc.getId()))
                                            .doOnError(error -> log.error("Failed to save document metadata after content upload: {}", error.getMessage(), error));
                                })
                                .doOnError(error -> {
                                    log.error("Failed to upload content for document ID {}: {}", documentId, error.getMessage(), error);
                                });
                            })
                            .orElseThrow(() -> {
                                log.warn("Document content upload not available - ECM DocumentContentPort not configured");
                                return new RuntimeException("Document content upload requires ECM DocumentContentPort to be configured");
                            });
                })
                // Note: Search indexing would be handled by ECM search port if available
                .map(savedDocument -> savedDocument)
                .map(mapper::toDTO);
    }

    @Override
    public Flux<DataBuffer> downloadContent(UUID documentId) {
        log.debug("Downloading content for document ID: {}", documentId);

        return repository.findById(documentId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document not found with ID: " + documentId)))
                .flatMapMany(document -> {
                    log.info("Downloading content for document: {} (ID: {})", document.getName(), document.getId());

                    // Download content using ECM port if available
                    java.util.UUID documentUuid = java.util.UUID.fromString(document.getId().toString());

                    return ecmPortProvider.getDocumentContentPort()
                            .map(port -> {
                                log.debug("Using ECM DocumentContentPort to download content");
                                return port.getContentStream(documentUuid)
                                        .doOnNext(buffer -> log.trace("Downloaded content buffer of size: {}", buffer.readableByteCount()))
                                        .doOnComplete(() -> log.debug("Content download completed for document ID: {}", documentId))
                                        .doOnError(error -> {
                                            log.error("Failed to download content for document ID {}: {}", documentId, error.getMessage(), error);
                                        });
                            })
                            .orElseThrow(() -> {
                                log.warn("Document content not available - ECM DocumentContentPort not configured");
                                return new RuntimeException("Document content download requires ECM DocumentContentPort to be configured");
                            });
                });
    }

    @Override
    public Mono<DocumentDTO> createVersion(UUID documentId, FilePart filePart, String versionComment) {
        return repository.findById(documentId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document not found with ID: " + documentId)))
                .flatMap(document -> {
                    java.util.UUID documentUuid = java.util.UUID.fromString(document.getId().toString());
                    
                    // Use ECM port for version management if available
                    return ecmPortProvider.getDocumentVersionPort()
                            .map(port -> {
                                // Convert FilePart content to byte array and create version through ECM port
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
                                            // Create DocumentVersion object for ECM
                                            // Note: This would need to be created based on the actual DocumentVersion class structure
                                            // For now, we'll skip ECM version creation and just update the document
                                            document.setVersion(document.getVersion() + 1);
                                            document.setFileName(filePart.filename());
                                            document.setMimeType(filePart.headers().getContentType() != null ?
                                                    filePart.headers().getContentType().toString() : null);

                                            return repository.save(document);
                                        });
                            })
                            .orElse(
                                // Fallback: simple version increment without ECM versioning
                                Mono.fromRunnable(() -> {
                                    document.setVersion(document.getVersion() + 1);
                                    document.setFileName(filePart.filename());
                                    document.setMimeType(filePart.headers().getContentType() != null ? 
                                            filePart.headers().getContentType().toString() : null);
                                }).then(repository.save(document))
                            );
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<DocumentDTO> getContentMetadata(UUID documentId) {
        return repository.findById(documentId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document not found with ID: " + documentId)))
                .map(mapper::toDTO);
    }
}
