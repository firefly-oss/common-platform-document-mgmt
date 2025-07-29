package com.catalis.commons.ecm.web.controllers;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.SignatureRequestService;
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

@RestController
@RequestMapping("/api/v1/signature-requests")
@Tag(name = "Signature Requests", description = "API for managing signature requests")
@RequiredArgsConstructor
public class SignatureRequestController {

    private final SignatureRequestService signatureRequestService;

    @Operation(summary = "Get signature request by ID", description = "Retrieves a signature request by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature request found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignatureRequestDTO.class))),
            @ApiResponse(responseCode = "404", description = "Signature request not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SignatureRequestDTO>> getSignatureRequestById(
            @Parameter(description = "Signature request ID", required = true)
            @PathVariable Long id) {
        return signatureRequestService.getSignatureRequestById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filter signature requests", description = "Filters signature requests based on provided criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature requests filtered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/filter")
    public Mono<ResponseEntity<PaginationResponse<SignatureRequestDTO>>> filterSignatureRequests(
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<SignatureRequestDTO> filterRequest) {
        return signatureRequestService.filterSignatureRequests(filterRequest)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Create signature request", description = "Creates a new signature request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Signature request created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignatureRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid signature request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<SignatureRequestDTO>> createSignatureRequest(
            @Parameter(description = "Signature request data", required = true)
            @Valid @RequestBody SignatureRequestDTO signatureRequestDTO) {
        return signatureRequestService.createSignatureRequest(signatureRequestDTO)
                .map(createdRequest -> ResponseEntity.status(HttpStatus.CREATED).body(createdRequest));
    }

    @Operation(summary = "Update signature request", description = "Updates an existing signature request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature request updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignatureRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid signature request data"),
            @ApiResponse(responseCode = "404", description = "Signature request not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<SignatureRequestDTO>> updateSignatureRequest(
            @Parameter(description = "Signature request ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated signature request data", required = true)
            @Valid @RequestBody SignatureRequestDTO signatureRequestDTO) {
        return signatureRequestService.updateSignatureRequest(id, signatureRequestDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete signature request", description = "Deletes a signature request by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Signature request deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Signature request not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteSignatureRequest(
            @Parameter(description = "Signature request ID", required = true)
            @PathVariable Long id) {
        return signatureRequestService.deleteSignatureRequest(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}