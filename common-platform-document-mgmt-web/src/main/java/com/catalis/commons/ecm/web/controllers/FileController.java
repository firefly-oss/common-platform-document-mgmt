package com.catalis.commons.ecm.web.controllers;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.FileService;
import com.catalis.commons.ecm.interfaces.dtos.FileDTO;
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
@RequestMapping("/api/v1/files")
@Tag(name = "Files", description = "API for managing file metadata")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(summary = "Get file by ID", description = "Retrieves file metadata by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FileDTO.class))),
            @ApiResponse(responseCode = "404", description = "File not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FileDTO>> getFileById(
            @Parameter(description = "File ID", required = true)
            @PathVariable Long id) {
        return fileService.getFileById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filter files", description = "Filters files based on provided criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Files filtered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/filter")
    public Mono<ResponseEntity<PaginationResponse<FileDTO>>> filterFiles(
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<FileDTO> filterRequest) {
        return fileService.filterFiles(filterRequest)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Create file", description = "Creates a new file metadata entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "File created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FileDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid file data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<FileDTO>> createFile(
            @Parameter(description = "File metadata", required = true)
            @Valid @RequestBody FileDTO fileDTO) {
        return fileService.createFile(fileDTO)
                .map(createdFile -> ResponseEntity.status(HttpStatus.CREATED).body(createdFile));
    }

    @Operation(summary = "Update file", description = "Updates an existing file metadata entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FileDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid file data"),
            @ApiResponse(responseCode = "404", description = "File not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<FileDTO>> updateFile(
            @Parameter(description = "File ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated file metadata", required = true)
            @Valid @RequestBody FileDTO fileDTO) {
        return fileService.updateFile(id, fileDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete file", description = "Deletes a file by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "File deleted successfully"),
            @ApiResponse(responseCode = "404", description = "File not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteFile(
            @Parameter(description = "File ID", required = true)
            @PathVariable Long id) {
        return fileService.deleteFile(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}