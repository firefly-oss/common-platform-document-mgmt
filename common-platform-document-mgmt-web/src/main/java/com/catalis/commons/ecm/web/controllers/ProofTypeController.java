package com.catalis.commons.ecm.web.controllers;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.ProofTypeService;
import com.catalis.commons.ecm.interfaces.dtos.ProofTypeDTO;
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
@RequestMapping("/api/v1/proof-types")
@Tag(name = "Proof Types", description = "API for managing proof types")
@RequiredArgsConstructor
public class ProofTypeController {

    private final ProofTypeService proofTypeService;

    @Operation(summary = "Get proof type by ID", description = "Retrieves a proof type by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proof type found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProofTypeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Proof type not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProofTypeDTO>> getProofTypeById(
            @Parameter(description = "Proof type ID", required = true)
            @PathVariable Long id) {
        return proofTypeService.getProofTypeById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filter proof types", description = "Filters proof types based on provided criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proof types filtered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/filter")
    public Mono<ResponseEntity<PaginationResponse<ProofTypeDTO>>> filterProofTypes(
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<ProofTypeDTO> filterRequest) {
        return proofTypeService.filterProofTypes(filterRequest)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Create proof type", description = "Creates a new proof type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proof type created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProofTypeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid proof type data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<ProofTypeDTO>> createProofType(
            @Parameter(description = "Proof type data", required = true)
            @Valid @RequestBody ProofTypeDTO proofTypeDTO) {
        return proofTypeService.createProofType(proofTypeDTO)
                .map(createdType -> ResponseEntity.status(HttpStatus.CREATED).body(createdType));
    }

    @Operation(summary = "Update proof type", description = "Updates an existing proof type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proof type updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProofTypeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid proof type data"),
            @ApiResponse(responseCode = "404", description = "Proof type not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ProofTypeDTO>> updateProofType(
            @Parameter(description = "Proof type ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated proof type data", required = true)
            @Valid @RequestBody ProofTypeDTO proofTypeDTO) {
        return proofTypeService.updateProofType(id, proofTypeDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete proof type", description = "Deletes a proof type by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Proof type deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Proof type not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteProofType(
            @Parameter(description = "Proof type ID", required = true)
            @PathVariable Long id) {
        return proofTypeService.deleteProofType(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}