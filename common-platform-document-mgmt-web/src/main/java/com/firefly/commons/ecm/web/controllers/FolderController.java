package com.firefly.commons.ecm.web.controllers;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.core.services.DocumentService;
import com.firefly.commons.ecm.core.services.FolderService;
import com.firefly.commons.ecm.interfaces.dtos.DocumentDTO;
import com.firefly.commons.ecm.interfaces.dtos.FolderDTO;
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
 * REST controller for managing Folder resources.
 */
@RestController
@RequestMapping("/api/v1/folders")
@RequiredArgsConstructor
@Tag(name = "Folder Controller", description = "API for managing folders")
public class FolderController {

    private final FolderService folderService;
    private final DocumentService documentService;

    @GetMapping
    @Operation(summary = "List all folders", description = "Returns a paginated list of folders with optional filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved folders",
                    content = @Content(schema = @Schema(implementation = PaginationResponse.class)))
    })
    public Mono<PaginationResponse<FolderDTO>> listFolders(
            @Parameter(description = "Filter request for folders") @ParameterObject @ModelAttribute FilterRequest<FolderDTO> filterRequest) {
        return folderService.filter(filterRequest != null ? filterRequest : new FilterRequest<>());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get folder by ID", description = "Returns a folder by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved folder",
                    content = @Content(schema = @Schema(implementation = FolderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Folder not found")
    })
    public Mono<FolderDTO> getFolderById(
            @Parameter(description = "ID of the folder to retrieve") @PathVariable UUID id) {
        return folderService.getById(id);
    }

    @GetMapping("/{id}/documents")
    @Operation(summary = "List all documents in a folder", description = "Returns all documents in a specific folder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved documents",
                    content = @Content(schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Folder not found")
    })
    public Mono<PaginationResponse<DocumentDTO>> listDocumentsInFolder(
            @Parameter(description = "ID of the folder") @PathVariable UUID id,
            @Parameter(description = "Filter request for documents") @ParameterObject @ModelAttribute FilterRequest<DocumentDTO> filterRequest) {

        FilterRequest<DocumentDTO> request = filterRequest != null ? filterRequest : new FilterRequest<>();

        // In a real implementation, you would add a filter for the folder ID
        // This is a simplified implementation
        return documentService.filter(request);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new folder", description = "Creates a new folder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Folder created successfully",
                    content = @Content(schema = @Schema(implementation = FolderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid folder data")
    })
    public Mono<FolderDTO> createFolder(
            @Parameter(description = "Folder data to create") @RequestBody FolderDTO folderDTO) {
        return folderService.create(folderDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing folder", description = "Updates an existing folder by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Folder updated successfully",
                    content = @Content(schema = @Schema(implementation = FolderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid folder data"),
            @ApiResponse(responseCode = "404", description = "Folder not found")
    })
    public Mono<FolderDTO> updateFolder(
            @Parameter(description = "ID of the folder to update") @PathVariable UUID id,
            @Parameter(description = "Updated folder data") @RequestBody FolderDTO folderDTO) {
        folderDTO.setId(id);
        return folderService.update(folderDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a folder", description = "Deletes a folder by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Folder deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Folder not found")
    })
    public Mono<Void> deleteFolder(
            @Parameter(description = "ID of the folder to delete") @PathVariable UUID id) {
        return folderService.delete(id);
    }
}
