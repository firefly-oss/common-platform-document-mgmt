package com.catalis.commons.ecm.web.controllers;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.DocumentTypeService;
import com.catalis.commons.ecm.interfaces.dtos.DocumentTypeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/document-types")
@Tag(name = "Document Types", description = "API for managing document types")
@RequiredArgsConstructor
public class DocumentTypeController {

    private final DocumentTypeService documentTypeService;

    @Operation(summary = "Get document type by ID", description = "Retrieves a document type by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document type found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentTypeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Document type not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<DocumentTypeDTO>> getDocumentTypeById(
            @Parameter(description = "Document type ID", required = true)
            @PathVariable Long id) {
        return documentTypeService.getDocumentTypeById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filter document types", description = "Filters document types based on provided criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document types filtered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/filter")
    public Mono<ResponseEntity<PaginationResponse<DocumentTypeDTO>>> filterDocumentTypes(
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<DocumentTypeDTO> filterRequest) {
        return documentTypeService.filterDocumentTypes(filterRequest)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Create document type", description = "Creates a new document type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document type created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentTypeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid document type data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<DocumentTypeDTO>> createDocumentType(
            @Parameter(description = "Document type data", required = true)
            @Valid @RequestBody DocumentTypeDTO documentTypeDTO) {
        return documentTypeService.createDocumentType(documentTypeDTO)
                .map(createdType -> ResponseEntity.status(HttpStatus.CREATED).body(createdType));
    }

    @Operation(summary = "Update document type", description = "Updates an existing document type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document type updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentTypeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid document type data"),
            @ApiResponse(responseCode = "404", description = "Document type not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<DocumentTypeDTO>> updateDocumentType(
            @Parameter(description = "Document type ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated document type data", required = true)
            @Valid @RequestBody DocumentTypeDTO documentTypeDTO) {
        return documentTypeService.updateDocumentType(id, documentTypeDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete document type", description = "Deletes a document type by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document type deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Document type not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteDocumentType(
            @Parameter(description = "Document type ID", required = true)
            @PathVariable Long id) {
        return documentTypeService.deleteDocumentType(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}