package com.catalis.commons.ecm.web.controllers;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.DocumentService;
import com.catalis.commons.ecm.core.services.DocumentVersionService;
import com.catalis.commons.ecm.core.services.DocumentTagService;
import com.catalis.commons.ecm.core.services.DocumentReferenceService;
import com.catalis.commons.ecm.interfaces.dtos.DocumentDTO;
import com.catalis.commons.ecm.interfaces.dtos.DocumentVersionDTO;
import com.catalis.commons.ecm.interfaces.dtos.DocumentTagDTO;
import com.catalis.commons.ecm.interfaces.dtos.DocumentReferenceDTO;
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
@RequestMapping("/api/v1/documents")
@Tag(name = "Documents", description = "API for managing documents and related resources")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentVersionService documentVersionService;
    private final DocumentTagService documentTagService;
    private final DocumentReferenceService documentReferenceService;

    // Document endpoints

    @Operation(summary = "Get document by ID", description = "Retrieves a document by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Document not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<DocumentDTO>> getDocumentById(
            @Parameter(description = "Document ID", required = true)
            @PathVariable Long id) {
        return documentService.getDocumentById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filter documents", description = "Filters documents based on provided criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documents filtered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/filter")
    public Mono<ResponseEntity<PaginationResponse<DocumentDTO>>> filterDocuments(
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<DocumentDTO> filterRequest) {
        return documentService.filterDocuments(filterRequest)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Create document", description = "Creates a new document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid document data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<DocumentDTO>> createDocument(
            @Parameter(description = "Document data", required = true)
            @Valid @RequestBody DocumentDTO documentDTO) {
        return documentService.createDocument(documentDTO)
                .map(createdDoc -> ResponseEntity.status(HttpStatus.CREATED).body(createdDoc));
    }

    @Operation(summary = "Update document", description = "Updates an existing document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid document data"),
            @ApiResponse(responseCode = "404", description = "Document not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<DocumentDTO>> updateDocument(
            @Parameter(description = "Document ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated document data", required = true)
            @Valid @RequestBody DocumentDTO documentDTO) {
        return documentService.updateDocument(id, documentDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete document", description = "Deletes a document by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Document not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteDocument(
            @Parameter(description = "Document ID", required = true)
            @PathVariable Long id) {
        return documentService.deleteDocument(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Document Version endpoints (nested resource)

    @Operation(summary = "Get document version by ID", description = "Retrieves a document version by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document version found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentVersionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Document version not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{documentId}/versions/{versionId}")
    public Mono<ResponseEntity<DocumentVersionDTO>> getDocumentVersionById(
            @Parameter(description = "Document ID", required = true) @PathVariable Long documentId,
            @Parameter(description = "Version ID", required = true) @PathVariable Long versionId) {
        return documentVersionService.getDocumentVersionById(versionId)
                .filter(version -> version.getDocumentId().equals(documentId))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all document versions", description = "Retrieves all versions of a specific document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document versions retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Document not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{documentId}/versions")
    public Mono<ResponseEntity<PaginationResponse<DocumentVersionDTO>>> getDocumentVersions(
            @Parameter(description = "Document ID", required = true) @PathVariable Long documentId) {
        // First check if the document exists
        return documentService.getDocumentById(documentId)
                .flatMap(document -> {
                    // Create a filter request for all versions
                    FilterRequest<DocumentVersionDTO> filterRequest = new FilterRequest<>();
                    return documentVersionService.filterDocumentVersions(filterRequest)
                            .map(ResponseEntity::ok);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filter document versions", description = "Advanced filtering of document versions. Include documentId in filter criteria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document versions filtered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{documentId}/versions/filter")
    public Mono<ResponseEntity<PaginationResponse<DocumentVersionDTO>>> filterDocumentVersions(
            @Parameter(description = "Document ID", required = true) @PathVariable Long documentId,
            @Parameter(description = "Filter criteria. Include documentId in filter criteria.", required = true)
            @Valid @RequestBody FilterRequest<DocumentVersionDTO> filterRequest) {
        // Note: The client should include documentId in the filter criteria
        return documentVersionService.filterDocumentVersions(filterRequest)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Create document version", description = "Creates a new version for a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document version created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentVersionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid document version data"),
            @ApiResponse(responseCode = "404", description = "Document not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/{documentId}/versions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<DocumentVersionDTO>> createDocumentVersion(
            @Parameter(description = "Document ID", required = true) @PathVariable Long documentId,
            @Parameter(description = "Document version data", required = true)
            @Valid @RequestBody DocumentVersionDTO versionDTO) {
        // Ensure the version is associated with the correct document
        versionDTO.setDocumentId(documentId);
        return documentService.getDocumentById(documentId)
                .flatMap(document -> documentVersionService.createDocumentVersion(versionDTO))
                .map(createdVersion -> ResponseEntity.status(HttpStatus.CREATED).body(createdVersion))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update document version", description = "Updates an existing document version")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document version updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentVersionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid document version data"),
            @ApiResponse(responseCode = "404", description = "Document version not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(value = "/{documentId}/versions/{versionId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<DocumentVersionDTO>> updateDocumentVersion(
            @Parameter(description = "Document ID", required = true) @PathVariable Long documentId,
            @Parameter(description = "Version ID", required = true) @PathVariable Long versionId,
            @Parameter(description = "Updated document version data", required = true)
            @Valid @RequestBody DocumentVersionDTO versionDTO) {
        // Ensure the version is associated with the correct document
        versionDTO.setDocumentId(documentId);
        return documentVersionService.updateDocumentVersion(versionId, versionDTO)
                .filter(version -> version.getDocumentId().equals(documentId))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete document version", description = "Deletes a document version by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document version deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Document version not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{documentId}/versions/{versionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteDocumentVersion(
            @Parameter(description = "Document ID", required = true) @PathVariable Long documentId,
            @Parameter(description = "Version ID", required = true) @PathVariable Long versionId) {
        return documentVersionService.getDocumentVersionById(versionId)
                .filter(version -> version.getDocumentId().equals(documentId))
                .flatMap(version -> documentVersionService.deleteDocumentVersion(versionId))
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Document Tag endpoints (nested resource)

    @Operation(summary = "Get document tag by ID", description = "Retrieves a document tag by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document tag found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentTagDTO.class))),
            @ApiResponse(responseCode = "404", description = "Document tag not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{documentId}/tags/{tagId}")
    public Mono<ResponseEntity<DocumentTagDTO>> getDocumentTagById(
            @Parameter(description = "Document ID", required = true) @PathVariable Long documentId,
            @Parameter(description = "Tag ID", required = true) @PathVariable Long tagId) {
        return documentTagService.getDocumentTagById(tagId)
                .filter(tag -> tag.getDocumentId().equals(documentId))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all document tags", description = "Retrieves all tags of a specific document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document tags retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Document not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{documentId}/tags")
    public Mono<ResponseEntity<PaginationResponse<DocumentTagDTO>>> getDocumentTags(
            @Parameter(description = "Document ID", required = true) @PathVariable Long documentId) {
        // First check if the document exists
        return documentService.getDocumentById(documentId)
                .flatMap(document -> {
                    // Create a filter request for all tags
                    FilterRequest<DocumentTagDTO> filterRequest = new FilterRequest<>();
                    return documentTagService.filterDocumentTags(filterRequest)
                            .map(ResponseEntity::ok);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filter document tags", description = "Advanced filtering of document tags. Include documentId in filter criteria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document tags filtered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{documentId}/tags/filter")
    public Mono<ResponseEntity<PaginationResponse<DocumentTagDTO>>> filterDocumentTags(
            @Parameter(description = "Document ID", required = true) @PathVariable Long documentId,
            @Parameter(description = "Filter criteria. Include documentId in filter criteria.", required = true)
            @Valid @RequestBody FilterRequest<DocumentTagDTO> filterRequest) {
        // Note: The client should include documentId in the filter criteria
        return documentTagService.filterDocumentTags(filterRequest)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Create document tag", description = "Associates a tag with a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document tag created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentTagDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid document tag data"),
            @ApiResponse(responseCode = "404", description = "Document not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/{documentId}/tags", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<DocumentTagDTO>> createDocumentTag(
            @Parameter(description = "Document ID", required = true) @PathVariable Long documentId,
            @Parameter(description = "Document tag data", required = true)
            @Valid @RequestBody DocumentTagDTO tagDTO) {
        // Ensure the tag is associated with the correct document
        tagDTO.setDocumentId(documentId);
        return documentService.getDocumentById(documentId)
                .flatMap(document -> documentTagService.createDocumentTag(tagDTO))
                .map(createdTag -> ResponseEntity.status(HttpStatus.CREATED).body(createdTag))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete document tag", description = "Removes a tag association from a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document tag deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Document tag not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{documentId}/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteDocumentTag(
            @Parameter(description = "Document ID", required = true) @PathVariable Long documentId,
            @Parameter(description = "Tag ID", required = true) @PathVariable Long tagId) {
        return documentTagService.getDocumentTagById(tagId)
                .filter(tag -> tag.getDocumentId().equals(documentId))
                .flatMap(tag -> documentTagService.deleteDocumentTag(tagId))
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Document Reference endpoints (nested resource)

    @Operation(summary = "Get document reference by ID", description = "Retrieves a document reference by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document reference found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentReferenceDTO.class))),
            @ApiResponse(responseCode = "404", description = "Document reference not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{documentId}/references/{referenceId}")
    public Mono<ResponseEntity<DocumentReferenceDTO>> getDocumentReferenceById(
            @Parameter(description = "Document ID", required = true) @PathVariable Long documentId,
            @Parameter(description = "Reference ID", required = true) @PathVariable Long referenceId) {
        return documentReferenceService.getDocumentReferenceById(referenceId)
                .filter(reference -> reference.getDocumentId().equals(documentId))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all document references", description = "Retrieves all references of a specific document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document references retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Document not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{documentId}/references")
    public Mono<ResponseEntity<PaginationResponse<DocumentReferenceDTO>>> getDocumentReferences(
            @Parameter(description = "Document ID", required = true) @PathVariable Long documentId) {
        // First check if the document exists
        return documentService.getDocumentById(documentId)
                .flatMap(document -> {
                    // Create a filter request for all references
                    FilterRequest<DocumentReferenceDTO> filterRequest = new FilterRequest<>();
                    return documentReferenceService.filterDocumentReferences(filterRequest)
                            .map(ResponseEntity::ok);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filter document references", description = "Advanced filtering of document references. Include documentId in filter criteria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document references filtered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{documentId}/references/filter")
    public Mono<ResponseEntity<PaginationResponse<DocumentReferenceDTO>>> filterDocumentReferences(
            @Parameter(description = "Document ID", required = true) @PathVariable Long documentId,
            @Parameter(description = "Filter criteria. Include documentId in filter criteria.", required = true)
            @Valid @RequestBody FilterRequest<DocumentReferenceDTO> filterRequest) {
        // Note: The client should include documentId in the filter criteria
        return documentReferenceService.filterDocumentReferences(filterRequest)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Create document reference", description = "Creates a reference between documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document reference created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentReferenceDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid document reference data"),
            @ApiResponse(responseCode = "404", description = "Document not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/{documentId}/references", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<DocumentReferenceDTO>> createDocumentReference(
            @Parameter(description = "Document ID", required = true) @PathVariable Long documentId,
            @Parameter(description = "Document reference data", required = true)
            @Valid @RequestBody DocumentReferenceDTO referenceDTO) {
        // Ensure the reference is associated with the correct document
        referenceDTO.setDocumentId(documentId);
        return documentService.getDocumentById(documentId)
                .flatMap(document -> documentReferenceService.createDocumentReference(referenceDTO))
                .map(createdReference -> ResponseEntity.status(HttpStatus.CREATED).body(createdReference))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update document reference", description = "Updates an existing document reference")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document reference updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentReferenceDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid document reference data"),
            @ApiResponse(responseCode = "404", description = "Document reference not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(value = "/{documentId}/references/{referenceId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<DocumentReferenceDTO>> updateDocumentReference(
            @Parameter(description = "Document ID", required = true) @PathVariable Long documentId,
            @Parameter(description = "Reference ID", required = true) @PathVariable Long referenceId,
            @Parameter(description = "Updated document reference data", required = true)
            @Valid @RequestBody DocumentReferenceDTO referenceDTO) {
        // Ensure the reference is associated with the correct document
        referenceDTO.setDocumentId(documentId);
        return documentReferenceService.updateDocumentReference(referenceId, referenceDTO)
                .filter(reference -> reference.getDocumentId().equals(documentId))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete document reference", description = "Deletes a document reference by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document reference deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Document reference not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{documentId}/references/{referenceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteDocumentReference(
            @Parameter(description = "Document ID", required = true) @PathVariable Long documentId,
            @Parameter(description = "Reference ID", required = true) @PathVariable Long referenceId) {
        return documentReferenceService.getDocumentReferenceById(referenceId)
                .filter(reference -> reference.getDocumentId().equals(documentId))
                .flatMap(reference -> documentReferenceService.deleteDocumentReference(referenceId))
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}