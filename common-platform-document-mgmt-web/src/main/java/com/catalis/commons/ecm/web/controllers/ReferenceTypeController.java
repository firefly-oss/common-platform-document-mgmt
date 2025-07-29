package com.catalis.commons.ecm.web.controllers;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.ReferenceTypeService;
import com.catalis.commons.ecm.interfaces.dtos.ReferenceTypeDTO;
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
@RequestMapping("/api/v1/reference-types")
@Tag(name = "Reference Types", description = "API for managing reference types")
@RequiredArgsConstructor
public class ReferenceTypeController {

    private final ReferenceTypeService referenceTypeService;

    @Operation(summary = "Get reference type by ID", description = "Retrieves a reference type by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reference type found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReferenceTypeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Reference type not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ReferenceTypeDTO>> getReferenceTypeById(
            @Parameter(description = "Reference type ID", required = true)
            @PathVariable Long id) {
        return referenceTypeService.getReferenceTypeById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filter reference types", description = "Filters reference types based on provided criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reference types filtered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/filter")
    public Mono<ResponseEntity<PaginationResponse<ReferenceTypeDTO>>> filterReferenceTypes(
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<ReferenceTypeDTO> filterRequest) {
        return referenceTypeService.filterReferenceTypes(filterRequest)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Create reference type", description = "Creates a new reference type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reference type created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReferenceTypeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid reference type data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<ReferenceTypeDTO>> createReferenceType(
            @Parameter(description = "Reference type data", required = true)
            @Valid @RequestBody ReferenceTypeDTO referenceTypeDTO) {
        return referenceTypeService.createReferenceType(referenceTypeDTO)
                .map(createdType -> ResponseEntity.status(HttpStatus.CREATED).body(createdType));
    }

    @Operation(summary = "Update reference type", description = "Updates an existing reference type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reference type updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReferenceTypeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid reference type data"),
            @ApiResponse(responseCode = "404", description = "Reference type not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ReferenceTypeDTO>> updateReferenceType(
            @Parameter(description = "Reference type ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated reference type data", required = true)
            @Valid @RequestBody ReferenceTypeDTO referenceTypeDTO) {
        return referenceTypeService.updateReferenceType(id, referenceTypeDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete reference type", description = "Deletes a reference type by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reference type deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Reference type not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteReferenceType(
            @Parameter(description = "Reference type ID", required = true)
            @PathVariable Long id) {
        return referenceTypeService.deleteReferenceType(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}