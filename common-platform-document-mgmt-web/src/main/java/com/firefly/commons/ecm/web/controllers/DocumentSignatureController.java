package com.firefly.commons.ecm.web.controllers;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.core.services.DocumentSignatureService;
import com.firefly.commons.ecm.interfaces.dtos.DocumentSignatureDTO;
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
 * REST controller for managing Document Signature resources.
 */
@RestController
@RequestMapping("/api/v1/documents/{documentId}/signatures")
@RequiredArgsConstructor
@Tag(name = "Document Signature Controller", description = "API for managing document signatures")
public class DocumentSignatureController {

    private final DocumentSignatureService documentSignatureService;

    @GetMapping
    @Operation(summary = "List all signatures for a document", description = "Returns all signatures for a specific document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved document signatures",
                    content = @Content(schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    public Mono<PaginationResponse<DocumentSignatureDTO>> listDocumentSignatures(
            @Parameter(description = "ID of the document") @PathVariable Long documentId,
            @Parameter(description = "Filter request for document signatures") @ParameterObject @ModelAttribute FilterRequest<DocumentSignatureDTO> filterRequest) {
        return documentSignatureService.filter(filterRequest != null ? filterRequest : new FilterRequest<>());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get specific signature", description = "Returns a specific signature for a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved document signature",
                    content = @Content(schema = @Schema(implementation = DocumentSignatureDTO.class))),
            @ApiResponse(responseCode = "404", description = "Document signature not found")
    })
    public Mono<DocumentSignatureDTO> getDocumentSignature(
            @Parameter(description = "ID of the document") @PathVariable Long documentId,
            @Parameter(description = "ID of the signature to retrieve") @PathVariable Long id) {
        return documentSignatureService.getById(id)
                .filter(signature -> signature.getDocumentId().equals(documentId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a signature to a document", description = "Creates a new signature for a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document signature created successfully",
                    content = @Content(schema = @Schema(implementation = DocumentSignatureDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid document signature data"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    public Mono<DocumentSignatureDTO> addDocumentSignature(
            @Parameter(description = "ID of the document") @PathVariable Long documentId,
            @Parameter(description = "Document signature data to create") @RequestBody DocumentSignatureDTO signatureDTO) {
        signatureDTO.setDocumentId(documentId);
        return documentSignatureService.create(signatureDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a document signature", description = "Updates an existing signature for a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document signature updated successfully",
                    content = @Content(schema = @Schema(implementation = DocumentSignatureDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid document signature data"),
            @ApiResponse(responseCode = "404", description = "Document signature not found")
    })
    public Mono<DocumentSignatureDTO> updateDocumentSignature(
            @Parameter(description = "ID of the document") @PathVariable Long documentId,
            @Parameter(description = "ID of the signature to update") @PathVariable Long id,
            @Parameter(description = "Updated document signature data") @RequestBody DocumentSignatureDTO signatureDTO) {
        signatureDTO.setId(id);
        signatureDTO.setDocumentId(documentId);
        return documentSignatureService.update(signatureDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove a signature from a document", description = "Deletes a signature from a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document signature deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Document signature not found")
    })
    public Mono<Void> deleteDocumentSignature(
            @Parameter(description = "ID of the document") @PathVariable Long documentId,
            @Parameter(description = "ID of the signature to delete") @PathVariable Long id) {
        return documentSignatureService.getById(id)
                .filter(signature -> signature.getDocumentId().equals(documentId))
                .flatMap(signature -> documentSignatureService.delete(id));
    }
}
