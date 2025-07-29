package com.catalis.commons.ecm.web.controllers;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.DocumentStatusService;
import com.catalis.commons.ecm.interfaces.dtos.DocumentStatusDTO;
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
@RequestMapping("/api/v1/document-statuses")
@Tag(name = "Document Statuses", description = "API for managing document statuses")
@RequiredArgsConstructor
public class DocumentStatusController {

    private final DocumentStatusService documentStatusService;

    @Operation(summary = "Get document status by ID", description = "Retrieves a document status by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document status found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentStatusDTO.class))),
            @ApiResponse(responseCode = "404", description = "Document status not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<DocumentStatusDTO>> getDocumentStatusById(
            @Parameter(description = "Document status ID", required = true)
            @PathVariable Long id) {
        return documentStatusService.getDocumentStatusById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filter document statuses", description = "Filters document statuses based on provided criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document statuses filtered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/filter")
    public Mono<ResponseEntity<PaginationResponse<DocumentStatusDTO>>> filterDocumentStatuses(
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<DocumentStatusDTO> filterRequest) {
        return documentStatusService.filterDocumentStatuses(filterRequest)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Create document status", description = "Creates a new document status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document status created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentStatusDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid document status data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<DocumentStatusDTO>> createDocumentStatus(
            @Parameter(description = "Document status data", required = true)
            @Valid @RequestBody DocumentStatusDTO documentStatusDTO) {
        return documentStatusService.createDocumentStatus(documentStatusDTO)
                .map(createdStatus -> ResponseEntity.status(HttpStatus.CREATED).body(createdStatus));
    }

    @Operation(summary = "Update document status", description = "Updates an existing document status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document status updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentStatusDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid document status data"),
            @ApiResponse(responseCode = "404", description = "Document status not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<DocumentStatusDTO>> updateDocumentStatus(
            @Parameter(description = "Document status ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated document status data", required = true)
            @Valid @RequestBody DocumentStatusDTO documentStatusDTO) {
        return documentStatusService.updateDocumentStatus(id, documentStatusDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete document status", description = "Deletes a document status by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document status deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Document status not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteDocumentStatus(
            @Parameter(description = "Document status ID", required = true)
            @PathVariable Long id) {
        return documentStatusService.deleteDocumentStatus(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}