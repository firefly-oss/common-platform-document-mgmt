package com.catalis.commons.ecm.web.controllers;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.SignatureProofService;
import com.catalis.commons.ecm.interfaces.dtos.SignatureProofDTO;
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
@RequestMapping("/api/v1/signature-proofs")
@Tag(name = "Signature Proofs", description = "API for managing signature proofs")
@RequiredArgsConstructor
public class SignatureProofController {

    private final SignatureProofService signatureProofService;

    @Operation(summary = "Get signature proof by ID", description = "Retrieves a signature proof by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature proof found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignatureProofDTO.class))),
            @ApiResponse(responseCode = "404", description = "Signature proof not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SignatureProofDTO>> getSignatureProofById(
            @Parameter(description = "Signature proof ID", required = true)
            @PathVariable Long id) {
        return signatureProofService.getSignatureProofById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filter signature proofs", description = "Filters signature proofs based on provided criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature proofs filtered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/filter")
    public Mono<ResponseEntity<PaginationResponse<SignatureProofDTO>>> filterSignatureProofs(
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<SignatureProofDTO> filterRequest) {
        return signatureProofService.filterSignatureProofs(filterRequest)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Create signature proof", description = "Creates a new signature proof")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Signature proof created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignatureProofDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid signature proof data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<SignatureProofDTO>> createSignatureProof(
            @Parameter(description = "Signature proof data", required = true)
            @Valid @RequestBody SignatureProofDTO signatureProofDTO) {
        return signatureProofService.createSignatureProof(signatureProofDTO)
                .map(createdProof -> ResponseEntity.status(HttpStatus.CREATED).body(createdProof));
    }

    @Operation(summary = "Update signature proof", description = "Updates an existing signature proof")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature proof updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignatureProofDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid signature proof data"),
            @ApiResponse(responseCode = "404", description = "Signature proof not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<SignatureProofDTO>> updateSignatureProof(
            @Parameter(description = "Signature proof ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated signature proof data", required = true)
            @Valid @RequestBody SignatureProofDTO signatureProofDTO) {
        return signatureProofService.updateSignatureProof(id, signatureProofDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete signature proof", description = "Deletes a signature proof by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Signature proof deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Signature proof not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteSignatureProof(
            @Parameter(description = "Signature proof ID", required = true)
            @PathVariable Long id) {
        return signatureProofService.deleteSignatureProof(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}