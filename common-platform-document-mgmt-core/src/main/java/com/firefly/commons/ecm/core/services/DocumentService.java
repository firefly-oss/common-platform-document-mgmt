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


package com.firefly.commons.ecm.core.services;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.interfaces.dtos.DocumentDTO;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service interface for managing Document entities in the Enterprise Content Management system.
 */
public interface DocumentService {

    /**
     * Get a document by its ID.
     *
     * @param id The document ID
     * @return A Mono emitting the document if found, or empty if not found
     */
    Mono<DocumentDTO> getById(UUID id);

    /**
     * Filter documents based on the provided filter request.
     *
     * @param filterRequest The filter request containing filtering and pagination parameters
     * @return A Mono emitting a pagination response with the filtered documents
     */
    Mono<PaginationResponse<DocumentDTO>> filter(FilterRequest<DocumentDTO> filterRequest);

    /**
     * Update an existing document.
     *
     * @param document The document to update
     * @return A Mono emitting the updated document
     */
    Mono<DocumentDTO> update(DocumentDTO document);

    /**
     * Create a new document.
     *
     * @param document The document to create
     * @return A Mono emitting the created document
     */
    Mono<DocumentDTO> create(DocumentDTO document);

    /**
     * Delete a document by its ID.
     *
     * @param id The ID of the document to delete
     * @return A Mono completing when the document is deleted
     */
    Mono<Void> delete(UUID id);

    // ECM Port Operations

    /**
     * Upload a document file and store it using ECM ports.
     *
     * @param documentId The ID of the document to upload content for
     * @param filePart The file part containing the document content
     * @return A Mono emitting the updated document with storage information
     */
    Mono<DocumentDTO> uploadContent(UUID documentId, FilePart filePart);

    /**
     * Download document content using ECM ports.
     *
     * @param documentId The ID of the document to download
     * @return A Flux of DataBuffer containing the document content
     */
    Flux<DataBuffer> downloadContent(UUID documentId);

    /**
     * Create a new version of a document using ECM ports.
     *
     * @param documentId The ID of the original document
     * @param filePart The file part containing the new version content
     * @param versionComment Optional comment for the new version
     * @return A Mono emitting the updated document with new version information
     */
    Mono<DocumentDTO> createVersion(UUID documentId, FilePart filePart, String versionComment);

    /**
     * Get document content metadata using ECM ports.
     *
     * @param documentId The ID of the document
     * @return A Mono emitting document content metadata
     */
    Mono<DocumentDTO> getContentMetadata(UUID documentId);
}
