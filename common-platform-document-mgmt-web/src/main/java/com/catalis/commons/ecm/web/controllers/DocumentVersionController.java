package com.catalis.commons.ecm.web.controllers;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.DocumentVersionService;
import com.catalis.commons.ecm.interfaces.dtos.DocumentVersionDTO;
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

/**
 * REST controller for managing Document Version resources.
 */
@RestController
@RequestMapping("/api/v1/documents/{documentId}/versions")
@RequiredArgsConstructor
@Tag(name = "Document Version Controller", description = "API for managing document versions")
public class DocumentVersionController {

    private final DocumentVersionService documentVersionService;

    @GetMapping
    @Operation(summary = "List all versions of a document", description = "Returns a paginated list of versions for a specific document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved document versions",
                    content = @Content(schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    public Mono<PaginationResponse<DocumentVersionDTO>> listDocumentVersions(
            @Parameter(description = "ID of the document") @PathVariable Long documentId,
            @Parameter(description = "Filter request for document versions") @ParameterObject @ModelAttribute FilterRequest<DocumentVersionDTO> filterRequest) {
        // We'll use the service's filter method directly
        // The service implementation should handle filtering by document ID
        return documentVersionService.filter(filterRequest != null ? filterRequest : new FilterRequest<>());
    }

    @GetMapping("/{versionId}")
    @Operation(summary = "Get specific version of a document", description = "Returns a specific version of a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved document version",
                    content = @Content(schema = @Schema(implementation = DocumentVersionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Document version not found")
    })
    public Mono<DocumentVersionDTO> getDocumentVersion(
            @Parameter(description = "ID of the document") @PathVariable Long documentId,
            @Parameter(description = "ID of the version to retrieve") @PathVariable Long versionId) {
        return documentVersionService.getById(versionId)
                .filter(version -> version.getDocumentId().equals(documentId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new document version", description = "Creates a new version for a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document version created successfully",
                    content = @Content(schema = @Schema(implementation = DocumentVersionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid document version data"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    public Mono<DocumentVersionDTO> createDocumentVersion(
            @Parameter(description = "ID of the document") @PathVariable Long documentId,
            @Parameter(description = "Document version data to create") @RequestBody DocumentVersionDTO versionDTO) {
        versionDTO.setDocumentId(documentId);
        return documentVersionService.create(versionDTO);
    }

    @DeleteMapping("/{versionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a document version", description = "Deletes a specific version of a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document version deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Document version not found")
    })
    public Mono<Void> deleteDocumentVersion(
            @Parameter(description = "ID of the document") @PathVariable Long documentId,
            @Parameter(description = "ID of the version to delete") @PathVariable Long versionId) {
        return documentVersionService.getById(versionId)
                .filter(version -> version.getDocumentId().equals(documentId))
                .flatMap(version -> documentVersionService.delete(versionId));
    }
}
