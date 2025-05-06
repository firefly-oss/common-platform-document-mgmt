package com.catalis.commons.ecm.web.controllers;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.SignatureVerificationService;
import com.catalis.commons.ecm.interfaces.dtos.SignatureVerificationDTO;
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

/**
 * REST controller for managing Signature Verification resources.
 */
@RestController
@RequestMapping("/api/v1/signature-verifications")
@RequiredArgsConstructor
@Tag(name = "Signature Verification Controller", description = "API for managing signature verifications")
public class SignatureVerificationController {

    private final SignatureVerificationService signatureVerificationService;

    @GetMapping
    @Operation(summary = "List all signature verifications", description = "Returns a paginated list of signature verifications with optional filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved signature verifications",
                    content = @Content(schema = @Schema(implementation = PaginationResponse.class)))
    })
    public Mono<PaginationResponse<SignatureVerificationDTO>> listSignatureVerifications(
            @Parameter(description = "Filter request for signature verifications") @ParameterObject @ModelAttribute FilterRequest<SignatureVerificationDTO> filterRequest) {
        return signatureVerificationService.filter(filterRequest != null ? filterRequest : new FilterRequest<>());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get signature verification by ID", description = "Returns a signature verification by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved signature verification",
                    content = @Content(schema = @Schema(implementation = SignatureVerificationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Signature verification not found")
    })
    public Mono<SignatureVerificationDTO> getSignatureVerificationById(
            @Parameter(description = "ID of the signature verification to retrieve") @PathVariable Long id) {
        return signatureVerificationService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new signature verification", description = "Creates a new signature verification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Signature verification created successfully",
                    content = @Content(schema = @Schema(implementation = SignatureVerificationDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid signature verification data")
    })
    public Mono<SignatureVerificationDTO> createSignatureVerification(
            @Parameter(description = "Signature verification data to create") @RequestBody SignatureVerificationDTO verificationDTO) {
        return signatureVerificationService.create(verificationDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing signature verification", description = "Updates an existing signature verification by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature verification updated successfully",
                    content = @Content(schema = @Schema(implementation = SignatureVerificationDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid signature verification data"),
            @ApiResponse(responseCode = "404", description = "Signature verification not found")
    })
    public Mono<SignatureVerificationDTO> updateSignatureVerification(
            @Parameter(description = "ID of the signature verification to update") @PathVariable Long id,
            @Parameter(description = "Updated signature verification data") @RequestBody SignatureVerificationDTO verificationDTO) {
        verificationDTO.setId(id);
        return signatureVerificationService.update(verificationDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a signature verification", description = "Deletes a signature verification by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Signature verification deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Signature verification not found")
    })
    public Mono<Void> deleteSignatureVerification(
            @Parameter(description = "ID of the signature verification to delete") @PathVariable Long id) {
        return signatureVerificationService.delete(id);
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify a document signature", description = "Verifies a document signature and returns the verification result")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature verification completed successfully",
                    content = @Content(schema = @Schema(implementation = SignatureVerificationDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid signature verification request"),
            @ApiResponse(responseCode = "404", description = "Document signature not found")
    })
    public Mono<SignatureVerificationDTO> verifyDocumentSignature(
            @Parameter(description = "ID of the document signature to verify") @RequestParam Long documentSignatureId) {
        return signatureVerificationService.verifySignature(documentSignatureId);
    }
}
