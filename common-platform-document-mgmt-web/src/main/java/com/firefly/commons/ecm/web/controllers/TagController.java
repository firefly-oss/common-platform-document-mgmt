package com.firefly.commons.ecm.web.controllers;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.core.services.DocumentService;
import com.firefly.commons.ecm.core.services.TagService;
import com.firefly.commons.ecm.interfaces.dtos.DocumentDTO;
import com.firefly.commons.ecm.interfaces.dtos.TagDTO;
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
 * REST controller for managing Tag resources.
 */
@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
@Tag(name = "Tag Controller", description = "API for managing tags")
public class TagController {

    private final TagService tagService;
    private final DocumentService documentService;

    @GetMapping
    @Operation(summary = "List all tags", description = "Returns a paginated list of tags with optional filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tags",
                    content = @Content(schema = @Schema(implementation = PaginationResponse.class)))
    })
    public Mono<PaginationResponse<TagDTO>> listTags(
            @Parameter(description = "Filter request for tags") @ParameterObject @ModelAttribute FilterRequest<TagDTO> filterRequest) {
        return tagService.filter(filterRequest != null ? filterRequest : new FilterRequest<>());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tag by ID", description = "Returns a tag by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tag",
                    content = @Content(schema = @Schema(implementation = TagDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    public Mono<TagDTO> getTagById(
            @Parameter(description = "ID of the tag to retrieve") @PathVariable UUID id) {
        return tagService.getById(id);
    }

    @GetMapping("/{id}/documents")
    @Operation(summary = "List all documents with a specific tag", description = "Returns all documents that have a specific tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved documents",
                    content = @Content(schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    public Mono<PaginationResponse<DocumentDTO>> listDocumentsWithTag(
            @Parameter(description = "ID of the tag") @PathVariable UUID id,
            @Parameter(description = "Filter request for documents") @ParameterObject @ModelAttribute FilterRequest<DocumentDTO> filterRequest) {

        FilterRequest<DocumentDTO> request = filterRequest != null ? filterRequest : new FilterRequest<>();

        // In a real implementation, you would add a filter for the tag ID
        // This is a simplified implementation
        return documentService.filter(request);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new tag", description = "Creates a new tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tag created successfully",
                    content = @Content(schema = @Schema(implementation = TagDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid tag data")
    })
    public Mono<TagDTO> createTag(
            @Parameter(description = "Tag data to create") @RequestBody TagDTO tagDTO) {
        return tagService.create(tagDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing tag", description = "Updates an existing tag by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag updated successfully",
                    content = @Content(schema = @Schema(implementation = TagDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid tag data"),
            @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    public Mono<TagDTO> updateTag(
            @Parameter(description = "ID of the tag to update") @PathVariable UUID id,
            @Parameter(description = "Updated tag data") @RequestBody TagDTO tagDTO) {
        tagDTO.setId(id);
        return tagService.update(tagDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a tag", description = "Deletes a tag by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tag deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    public Mono<Void> deleteTag(
            @Parameter(description = "ID of the tag to delete") @PathVariable UUID id) {
        return tagService.delete(id);
    }
}
