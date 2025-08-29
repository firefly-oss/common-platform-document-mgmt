package com.firefly.commons.ecm.web.controllers;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.core.services.DocumentPermissionService;
import com.firefly.commons.ecm.interfaces.dtos.DocumentPermissionDTO;
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
 * REST controller for managing Document Permission resources.
 */
@RestController
@RequestMapping("/api/v1/documents/{documentId}/permissions")
@RequiredArgsConstructor
@Tag(name = "Document Permission Controller", description = "API for managing document permissions")
public class DocumentPermissionController {

    private final DocumentPermissionService documentPermissionService;

    @GetMapping
    @Operation(summary = "List all permissions for a document", description = "Returns all permissions for a specific document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved document permissions",
                    content = @Content(schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    public Mono<PaginationResponse<DocumentPermissionDTO>> listDocumentPermissions(
            @Parameter(description = "ID of the document") @PathVariable Long documentId,
            @Parameter(description = "Filter request for document permissions") @ParameterObject @ModelAttribute FilterRequest<DocumentPermissionDTO> filterRequest) {
        return documentPermissionService.filter(filterRequest != null ? filterRequest : new FilterRequest<>());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get specific permission", description = "Returns a specific permission for a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved document permission",
                    content = @Content(schema = @Schema(implementation = DocumentPermissionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Document permission not found")
    })
    public Mono<DocumentPermissionDTO> getDocumentPermission(
            @Parameter(description = "ID of the document") @PathVariable Long documentId,
            @Parameter(description = "ID of the permission to retrieve") @PathVariable Long id) {
        return documentPermissionService.getById(id)
                .filter(permission -> permission.getDocumentId().equals(documentId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a permission to a document", description = "Creates a new permission for a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document permission created successfully",
                    content = @Content(schema = @Schema(implementation = DocumentPermissionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid document permission data"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    public Mono<DocumentPermissionDTO> addDocumentPermission(
            @Parameter(description = "ID of the document") @PathVariable Long documentId,
            @Parameter(description = "Document permission data to create") @RequestBody DocumentPermissionDTO permissionDTO) {
        permissionDTO.setDocumentId(documentId);
        return documentPermissionService.create(permissionDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a document permission", description = "Updates an existing permission for a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document permission updated successfully",
                    content = @Content(schema = @Schema(implementation = DocumentPermissionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid document permission data"),
            @ApiResponse(responseCode = "404", description = "Document permission not found")
    })
    public Mono<DocumentPermissionDTO> updateDocumentPermission(
            @Parameter(description = "ID of the document") @PathVariable Long documentId,
            @Parameter(description = "ID of the permission to update") @PathVariable Long id,
            @Parameter(description = "Updated document permission data") @RequestBody DocumentPermissionDTO permissionDTO) {
        permissionDTO.setId(id);
        permissionDTO.setDocumentId(documentId);
        return documentPermissionService.update(permissionDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove a permission from a document", description = "Deletes a permission from a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document permission deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Document permission not found")
    })
    public Mono<Void> deleteDocumentPermission(
            @Parameter(description = "ID of the document") @PathVariable Long documentId,
            @Parameter(description = "ID of the permission to delete") @PathVariable Long id) {
        return documentPermissionService.getById(id)
                .filter(permission -> permission.getDocumentId().equals(documentId))
                .flatMap(permission -> documentPermissionService.delete(id));
    }
}
