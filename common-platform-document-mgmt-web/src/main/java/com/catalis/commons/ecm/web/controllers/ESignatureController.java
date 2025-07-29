package com.catalis.commons.ecm.web.controllers;

import com.catalis.commons.ecm.core.services.ESignatureService;
import com.catalis.commons.ecm.interfaces.dtos.SignatureProofDTO;
import com.catalis.commons.ecm.interfaces.dtos.SignatureRequestDTO;
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

/**
 * REST controller for e-signature operations.
 * This controller provides endpoints for initiating, checking, and canceling signature requests,
 * as well as retrieving and validating signature proofs.
 */
@RestController
@RequestMapping("/api/v1/e-signatures")
@Tag(name = "E-Signatures", description = "API for e-signature operations")
@RequiredArgsConstructor
public class ESignatureController {

    private final ESignatureService eSignatureService;

    @Operation(summary = "Initiate signature request", description = "Initiates a new signature request with the specified provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Signature request initiated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignatureRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid signature request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/initiate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<SignatureRequestDTO>> initiateSignature(
            @Parameter(description = "Signature request data", required = true)
            @Valid @RequestBody SignatureRequestDTO signatureRequestDTO,
            @Parameter(description = "E-signature provider name (optional)")
            @RequestParam(required = false) String provider) {
        
        Mono<SignatureRequestDTO> result = provider != null ?
                eSignatureService.initiateSignature(signatureRequestDTO, provider) :
                eSignatureService.initiateSignature(signatureRequestDTO);
        
        return result.map(createdRequest -> ResponseEntity.status(HttpStatus.CREATED).body(createdRequest));
    }

    @Operation(summary = "Get signature status", description = "Gets the status of a signature request from the specified provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature status retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignatureRequestDTO.class))),
            @ApiResponse(responseCode = "404", description = "Signature request not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<SignatureRequestDTO>> getSignatureStatus(
            @Parameter(description = "Signature request ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "E-signature provider name (optional)")
            @RequestParam(required = false) String provider) {
        
        Mono<SignatureRequestDTO> result = provider != null ?
                eSignatureService.getSignatureStatus(id, provider) :
                eSignatureService.getSignatureStatus(id);
        
        return result.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cancel signature request", description = "Cancels a signature request with the specified provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Signature request canceled successfully"),
            @ApiResponse(responseCode = "404", description = "Signature request not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> cancelSignature(
            @Parameter(description = "Signature request ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "E-signature provider name (optional)")
            @RequestParam(required = false) String provider) {
        
        Mono<Void> result = provider != null ?
                eSignatureService.cancelSignature(id, provider) :
                eSignatureService.cancelSignature(id);
        
        return result.then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get signature proof", description = "Gets the proof of signature from the specified provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature proof retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignatureProofDTO.class))),
            @ApiResponse(responseCode = "404", description = "Signature request not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/{id}/proof", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<SignatureProofDTO>> getSignatureProof(
            @Parameter(description = "Signature request ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "E-signature provider name (optional)")
            @RequestParam(required = false) String provider) {
        
        Mono<SignatureProofDTO> result = provider != null ?
                eSignatureService.getSignatureProof(id, provider) :
                eSignatureService.getSignatureProof(id);
        
        return result.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Validate signature proof", description = "Validates a signature proof with the specified provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature proof validation result",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "Invalid signature proof data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/validate-proof", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Boolean>> validateSignatureProof(
            @Parameter(description = "Signature proof data", required = true)
            @Valid @RequestBody SignatureProofDTO signatureProofDTO,
            @Parameter(description = "E-signature provider name (optional)")
            @RequestParam(required = false) String provider) {
        
        Mono<Boolean> result = provider != null ?
                eSignatureService.validateSignatureProof(signatureProofDTO, provider) :
                eSignatureService.validateSignatureProof(signatureProofDTO);
        
        return result.map(ResponseEntity::ok);
    }
}