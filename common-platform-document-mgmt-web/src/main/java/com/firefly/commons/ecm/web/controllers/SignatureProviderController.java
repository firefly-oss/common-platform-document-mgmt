package com.firefly.commons.ecm.web.controllers;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.core.services.SignatureProviderService;
import com.firefly.commons.ecm.interfaces.dtos.SignatureProviderDTO;
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
 * REST controller for managing Signature Provider resources.
 */
@RestController
@RequestMapping("/api/v1/signature-providers")
@RequiredArgsConstructor
@Tag(name = "Signature Provider Controller", description = "API for managing signature providers")
public class SignatureProviderController {

    private final SignatureProviderService signatureProviderService;

    @GetMapping
    @Operation(summary = "List all signature providers", description = "Returns a paginated list of signature providers with optional filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved signature providers",
                    content = @Content(schema = @Schema(implementation = PaginationResponse.class)))
    })
    public Mono<PaginationResponse<SignatureProviderDTO>> listSignatureProviders(
            @Parameter(description = "Filter request for signature providers") @ParameterObject @ModelAttribute FilterRequest<SignatureProviderDTO> filterRequest) {
        return signatureProviderService.filter(filterRequest != null ? filterRequest : new FilterRequest<>());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get signature provider by ID", description = "Returns a signature provider by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved signature provider",
                    content = @Content(schema = @Schema(implementation = SignatureProviderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Signature provider not found")
    })
    public Mono<SignatureProviderDTO> getSignatureProviderById(
            @Parameter(description = "ID of the signature provider to retrieve") @PathVariable Long id) {
        return signatureProviderService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new signature provider", description = "Creates a new signature provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Signature provider created successfully",
                    content = @Content(schema = @Schema(implementation = SignatureProviderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid signature provider data")
    })
    public Mono<SignatureProviderDTO> createSignatureProvider(
            @Parameter(description = "Signature provider data to create") @RequestBody SignatureProviderDTO providerDTO) {
        return signatureProviderService.create(providerDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing signature provider", description = "Updates an existing signature provider by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature provider updated successfully",
                    content = @Content(schema = @Schema(implementation = SignatureProviderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid signature provider data"),
            @ApiResponse(responseCode = "404", description = "Signature provider not found")
    })
    public Mono<SignatureProviderDTO> updateSignatureProvider(
            @Parameter(description = "ID of the signature provider to update") @PathVariable Long id,
            @Parameter(description = "Updated signature provider data") @RequestBody SignatureProviderDTO providerDTO) {
        providerDTO.setId(id);
        return signatureProviderService.update(providerDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a signature provider", description = "Deletes a signature provider by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Signature provider deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Signature provider not found")
    })
    public Mono<Void> deleteSignatureProvider(
            @Parameter(description = "ID of the signature provider to delete") @PathVariable Long id) {
        return signatureProviderService.delete(id);
    }

    @PostMapping("/{id}/default")
    @Operation(summary = "Set as default signature provider", description = "Sets a signature provider as the default for the tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature provider set as default successfully",
                    content = @Content(schema = @Schema(implementation = SignatureProviderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Signature provider not found")
    })
    public Mono<SignatureProviderDTO> setAsDefaultProvider(
            @Parameter(description = "ID of the signature provider to set as default") @PathVariable Long id,
            @Parameter(description = "Tenant ID") @RequestParam(required = false) String tenantId) {
        return signatureProviderService.setAsDefault(id, tenantId);
    }
}
