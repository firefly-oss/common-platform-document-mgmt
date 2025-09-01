package com.firefly.commons.ecm.web.controllers;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.core.services.DocumentTagService;
import com.firefly.commons.ecm.interfaces.dtos.DocumentTagDTO;
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
 * REST controller for managing Document Tag resources.
 */
@RestController
@RequestMapping("/api/v1/documents/{documentId}/tags")
@RequiredArgsConstructor
@Tag(name = "Document Tag Controller", description = "API for managing document tags")
public class DocumentTagController {

    private final DocumentTagService documentTagService;

    @GetMapping
    @Operation(summary = "List all tags for a document", description = "Returns all tags associated with a specific document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved document tags",
                    content = @Content(schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    public Mono<PaginationResponse<DocumentTagDTO>> listDocumentTags(
            @Parameter(description = "ID of the document") @PathVariable UUID documentId,
            @Parameter(description = "Filter request for document tags") @ParameterObject @ModelAttribute FilterRequest<DocumentTagDTO> filterRequest) {
        return documentTagService.filter(filterRequest != null ? filterRequest : new FilterRequest<>());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a tag to a document", description = "Associates a tag with a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tag added to document successfully",
                    content = @Content(schema = @Schema(implementation = DocumentTagDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid tag data"),
            @ApiResponse(responseCode = "404", description = "Document or tag not found")
    })
    public Mono<DocumentTagDTO> addTagToDocument(
            @Parameter(description = "ID of the document") @PathVariable UUID documentId,
            @Parameter(description = "Document tag data to create") @RequestBody DocumentTagDTO documentTagDTO) {
        documentTagDTO.setDocumentId(documentId);
        return documentTagService.create(documentTagDTO);
    }

    @DeleteMapping("/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove a tag from a document", description = "Removes a tag association from a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tag removed from document successfully"),
            @ApiResponse(responseCode = "404", description = "Document tag association not found")
    })
    public Mono<Void> removeTagFromDocument(
            @Parameter(description = "ID of the document") @PathVariable UUID documentId,
            @Parameter(description = "ID of the tag to remove") @PathVariable UUID tagId) {
        // This is a simplified implementation. In a real-world scenario, you would need to
        // query the service to find the document-tag association by document ID and tag ID, then delete it.
        return documentTagService.filter(new FilterRequest<>())
                .flatMapMany(response -> Mono.justOrEmpty(response.getContent().stream()
                        .filter(tag -> tag.getDocumentId().equals(documentId) && tag.getTagId().equals(tagId))
                        .findFirst()))
                .flatMap(tag -> documentTagService.delete(tag.getId()))
                .then();
    }
}
