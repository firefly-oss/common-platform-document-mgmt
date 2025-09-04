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
import com.firefly.commons.ecm.interfaces.dtos.DocumentVersionDTO;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;
/**
 * Service interface for managing DocumentVersion entities in the Enterprise Content Management system.
 */
public interface DocumentVersionService {

    /**
     * Get a document version by its ID.
     *
     * @param id The document version ID
     * @return A Mono emitting the document version if found, or empty if not found
     */
    Mono<DocumentVersionDTO> getById(UUID id);

    /**
     * Filter document versions based on the provided filter request.
     *
     * @param filterRequest The filter request containing filtering and pagination parameters
     * @return A Mono emitting a pagination response with the filtered document versions
     */
    Mono<PaginationResponse<DocumentVersionDTO>> filter(FilterRequest<DocumentVersionDTO> filterRequest);

    /**
     * Update an existing document version.
     *
     * @param documentVersion The document version to update
     * @return A Mono emitting the updated document version
     */
    Mono<DocumentVersionDTO> update(DocumentVersionDTO documentVersion);

    /**
     * Create a new document version.
     *
     * @param documentVersion The document version to create
     * @return A Mono emitting the created document version
     */
    Mono<DocumentVersionDTO> create(DocumentVersionDTO documentVersion);

    /**
     * Delete a document version by its ID.
     *
     * @param id The ID of the document version to delete
     * @return A Mono completing when the document version is deleted
     */
    Mono<Void> delete(UUID id);

    // ECM Port Operations for Version Content

    /**
     * Upload content for a document version using ECM ports.
     *
     * @param versionId The ID of the document version to upload content for
     * @param filePart The file part containing the version content
     * @return A Mono emitting the updated document version with storage information
     */
    Mono<DocumentVersionDTO> uploadVersionContent(UUID versionId, FilePart filePart);

    /**
     * Download content of a document version using ECM ports.
     *
     * @param versionId The ID of the document version to download
     * @return A Flux of DataBuffer containing the version content
     */
    Flux<DataBuffer> downloadVersionContent(UUID versionId);

    /**
     * Get document version content metadata using ECM ports.
     *
     * @param versionId The ID of the document version
     * @return A Mono emitting document version content metadata
     */
    Mono<DocumentVersionDTO> getVersionContentMetadata(UUID versionId);

    /**
     * Get all versions for a specific document.
     *
     * @param documentId The document ID
     * @return A Flux emitting all versions for the document
     */
    Flux<DocumentVersionDTO> getVersionsByDocumentId(UUID documentId);
}
