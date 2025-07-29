package com.catalis.commons.ecm.web.controllers;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.SignatureSignerService;
import com.catalis.commons.ecm.interfaces.dtos.SignatureSignerDTO;
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
@RequestMapping("/api/v1/signature-signers")
@Tag(name = "Signature Signers", description = "API for managing signature signers")
@RequiredArgsConstructor
public class SignatureSignerController {

    private final SignatureSignerService signatureSignerService;

    @Operation(summary = "Get signature signer by ID", description = "Retrieves a signature signer by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature signer found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignatureSignerDTO.class))),
            @ApiResponse(responseCode = "404", description = "Signature signer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SignatureSignerDTO>> getSignatureSignerById(
            @Parameter(description = "Signature signer ID", required = true)
            @PathVariable Long id) {
        return signatureSignerService.getSignatureSignerById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filter signature signers", description = "Filters signature signers based on provided criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature signers filtered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/filter")
    public Mono<ResponseEntity<PaginationResponse<SignatureSignerDTO>>> filterSignatureSigners(
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<SignatureSignerDTO> filterRequest) {
        return signatureSignerService.filterSignatureSigners(filterRequest)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Create signature signer", description = "Creates a new signature signer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Signature signer created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignatureSignerDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid signature signer data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<SignatureSignerDTO>> createSignatureSigner(
            @Parameter(description = "Signature signer data", required = true)
            @Valid @RequestBody SignatureSignerDTO signatureSignerDTO) {
        return signatureSignerService.createSignatureSigner(signatureSignerDTO)
                .map(createdSigner -> ResponseEntity.status(HttpStatus.CREATED).body(createdSigner));
    }

    @Operation(summary = "Update signature signer", description = "Updates an existing signature signer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature signer updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignatureSignerDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid signature signer data"),
            @ApiResponse(responseCode = "404", description = "Signature signer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<SignatureSignerDTO>> updateSignatureSigner(
            @Parameter(description = "Signature signer ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated signature signer data", required = true)
            @Valid @RequestBody SignatureSignerDTO signatureSignerDTO) {
        return signatureSignerService.updateSignatureSigner(id, signatureSignerDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete signature signer", description = "Deletes a signature signer by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Signature signer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Signature signer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteSignatureSigner(
            @Parameter(description = "Signature signer ID", required = true)
            @PathVariable Long id) {
        return signatureSignerService.deleteSignatureSigner(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}