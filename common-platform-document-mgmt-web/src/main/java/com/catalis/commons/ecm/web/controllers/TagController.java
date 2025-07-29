package com.catalis.commons.ecm.web.controllers;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.TagService;
import com.catalis.commons.ecm.interfaces.dtos.TagDTO;
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
@RequestMapping("/api/v1/tags")
@Tag(name = "Tags", description = "API for managing tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @Operation(summary = "Get tag by ID", description = "Retrieves a tag by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tag not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TagDTO>> getTagById(
            @Parameter(description = "Tag ID", required = true)
            @PathVariable Long id) {
        return tagService.getTagById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filter tags", description = "Filters tags based on provided criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tags filtered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/filter")
    public Mono<ResponseEntity<PaginationResponse<TagDTO>>> filterTags(
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<TagDTO> filterRequest) {
        return tagService.filterTags(filterRequest)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Create tag", description = "Creates a new tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tag created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid tag data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<TagDTO>> createTag(
            @Parameter(description = "Tag data", required = true)
            @Valid @RequestBody TagDTO tagDTO) {
        return tagService.createTag(tagDTO)
                .map(createdTag -> ResponseEntity.status(HttpStatus.CREATED).body(createdTag));
    }

    @Operation(summary = "Update tag", description = "Updates an existing tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid tag data"),
            @ApiResponse(responseCode = "404", description = "Tag not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TagDTO>> updateTag(
            @Parameter(description = "Tag ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated tag data", required = true)
            @Valid @RequestBody TagDTO tagDTO) {
        return tagService.updateTag(id, tagDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete tag", description = "Deletes a tag by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tag deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Tag not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteTag(
            @Parameter(description = "Tag ID", required = true)
            @PathVariable Long id) {
        return tagService.deleteTag(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}