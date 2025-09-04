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


package com.firefly.commons.ecm.web.controllers;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.core.services.DocumentMetadataService;
import com.firefly.commons.ecm.interfaces.dtos.DocumentMetadataDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.UUID;
/**
 * REST controller for managing Document Metadata resources.
 */
@RestController
@RequestMapping("/api/v1/documents/{documentId}/metadata")
@RequiredArgsConstructor
@Tag(name = "Document Metadata Controller", description = "API for managing document metadata")
public class DocumentMetadataController {

    private final DocumentMetadataService documentMetadataService;

    @GetMapping
    @Operation(summary = "Get all metadata for a document", description = "Returns all metadata for a specific document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved document metadata",
                    content = @Content(schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    public Mono<PaginationResponse<DocumentMetadataDTO>> getAllMetadata(
            @Parameter(description = "ID of the document") @PathVariable UUID documentId,
            @Parameter(description = "Filter request for document metadata") @ParameterObject @ModelAttribute FilterRequest<DocumentMetadataDTO> filterRequest) {
        return documentMetadataService.filter(filterRequest != null ? filterRequest : new FilterRequest<>());
    }

    @GetMapping("/{key}")
    @Operation(summary = "Get specific metadata by key", description = "Returns specific metadata for a document by key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved document metadata",
                    content = @Content(schema = @Schema(implementation = DocumentMetadataDTO.class))),
            @ApiResponse(responseCode = "404", description = "Document metadata not found")
    })
    public Mono<DocumentMetadataDTO> getMetadataByKey(
            @Parameter(description = "ID of the document") @PathVariable UUID documentId,
            @Parameter(description = "Key of the metadata to retrieve") @PathVariable String key) {
        // This is a simplified implementation. In a real-world scenario, you would need to
        // query the service to find metadata by document ID and key.
        return documentMetadataService.filter(new FilterRequest<>())
                .flatMapMany(response -> Mono.justOrEmpty(response.getContent().stream()
                        .filter(metadata -> metadata.getDocumentId().equals(documentId) && metadata.getKey().equals(key))
                        .findFirst()))
                .next();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add metadata to a document", description = "Adds new metadata to a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document metadata created successfully",
                    content = @Content(schema = @Schema(implementation = DocumentMetadataDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid document metadata data"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    public Mono<DocumentMetadataDTO> addMetadata(
            @Parameter(description = "ID of the document") @PathVariable UUID documentId,
            @Parameter(description = "Document metadata to add") @RequestBody DocumentMetadataDTO metadataDTO) {
        metadataDTO.setDocumentId(documentId);
        return documentMetadataService.create(metadataDTO);
    }

    @PutMapping("/{key}")
    @Operation(summary = "Update document metadata", description = "Updates existing metadata for a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document metadata updated successfully",
                    content = @Content(schema = @Schema(implementation = DocumentMetadataDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid document metadata data"),
            @ApiResponse(responseCode = "404", description = "Document metadata not found")
    })
    public Mono<DocumentMetadataDTO> updateMetadata(
            @Parameter(description = "ID of the document") @PathVariable UUID documentId,
            @Parameter(description = "Key of the metadata to update") @PathVariable String key,
            @Parameter(description = "Updated document metadata") @RequestBody DocumentMetadataDTO metadataDTO) {
        metadataDTO.setDocumentId(documentId);
        metadataDTO.setKey(key);
        return documentMetadataService.update(metadataDTO);
    }

    @DeleteMapping("/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete document metadata", description = "Deletes metadata from a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document metadata deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Document metadata not found")
    })
    public Mono<Void> deleteMetadata(
            @Parameter(description = "ID of the document") @PathVariable UUID documentId,
            @Parameter(description = "Key of the metadata to delete") @PathVariable String key) {
        // This is a simplified implementation. In a real-world scenario, you would need to
        // query the service to find metadata by document ID and key, then delete it.
        return documentMetadataService.filter(new FilterRequest<>())
                .flatMapMany(response -> Mono.justOrEmpty(response.getContent().stream()
                        .filter(metadata -> metadata.getDocumentId().equals(documentId) && metadata.getKey().equals(key))
                        .findFirst()))
                .flatMap(metadata -> documentMetadataService.delete(metadata.getId()))
                .then();
    }
}
