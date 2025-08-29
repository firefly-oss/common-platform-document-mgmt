package com.firefly.commons.ecm.web.controllers;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.core.services.DocumentService;
import com.firefly.commons.ecm.interfaces.dtos.DocumentDTO;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import reactor.core.publisher.Mono;

/**
 * REST controller for managing Document resources.
 */
@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@Tag(name = "Document Controller", description = "API for managing documents")
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping
    @Operation(summary = "List all documents", description = "Returns a paginated list of documents with optional filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved documents",
                    content = @Content(schema = @Schema(implementation = PaginationResponse.class)))
    })
    public Mono<PaginationResponse<DocumentDTO>> listDocuments(
            @Parameter(description = "Filter request for documents") @ParameterObject @ModelAttribute FilterRequest<DocumentDTO> filterRequest) {
        return documentService.filter(filterRequest != null ? filterRequest : new FilterRequest<>());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get document by ID", description = "Returns a document by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved document",
                    content = @Content(schema = @Schema(implementation = DocumentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    public Mono<DocumentDTO> getDocumentById(
            @Parameter(description = "ID of the document to retrieve") @PathVariable Long id) {
        return documentService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new document", description = "Creates a new document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document created successfully",
                    content = @Content(schema = @Schema(implementation = DocumentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid document data")
    })
    public Mono<DocumentDTO> createDocument(
            @Parameter(description = "Document data to create") @RequestBody DocumentDTO documentDTO) {
        return documentService.create(documentDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing document", description = "Updates an existing document by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document updated successfully",
                    content = @Content(schema = @Schema(implementation = DocumentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid document data"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    public Mono<DocumentDTO> updateDocument(
            @Parameter(description = "ID of the document to update") @PathVariable Long id,
            @Parameter(description = "Updated document data") @RequestBody DocumentDTO documentDTO) {
        documentDTO.setId(id);
        return documentService.update(documentDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a document", description = "Deletes a document by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    public Mono<Void> deleteDocument(
            @Parameter(description = "ID of the document to delete") @PathVariable Long id) {
        return documentService.delete(id);
    }
}
