package com.catalis.commons.ecm.web.controllers;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.SignatureRequestStatusService;
import com.catalis.commons.ecm.interfaces.dtos.SignatureRequestStatusDTO;
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
@RequestMapping("/api/v1/signature-request-statuses")
@Tag(name = "Signature Request Statuses", description = "API for managing signature request statuses")
@RequiredArgsConstructor
public class SignatureRequestStatusController {

    private final SignatureRequestStatusService signatureRequestStatusService;

    @Operation(summary = "Get signature request status by ID", description = "Retrieves a signature request status by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature request status found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignatureRequestStatusDTO.class))),
            @ApiResponse(responseCode = "404", description = "Signature request status not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SignatureRequestStatusDTO>> getSignatureRequestStatusById(
            @Parameter(description = "Signature request status ID", required = true)
            @PathVariable Long id) {
        return signatureRequestStatusService.getSignatureRequestStatusById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filter signature request statuses", description = "Filters signature request statuses based on provided criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature request statuses filtered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/filter")
    public Mono<ResponseEntity<PaginationResponse<SignatureRequestStatusDTO>>> filterSignatureRequestStatuses(
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<SignatureRequestStatusDTO> filterRequest) {
        return signatureRequestStatusService.filterSignatureRequestStatuses(filterRequest)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Create signature request status", description = "Creates a new signature request status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Signature request status created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignatureRequestStatusDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid signature request status data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<SignatureRequestStatusDTO>> createSignatureRequestStatus(
            @Parameter(description = "Signature request status data", required = true)
            @Valid @RequestBody SignatureRequestStatusDTO signatureRequestStatusDTO) {
        return signatureRequestStatusService.createSignatureRequestStatus(signatureRequestStatusDTO)
                .map(createdStatus -> ResponseEntity.status(HttpStatus.CREATED).body(createdStatus));
    }

    @Operation(summary = "Update signature request status", description = "Updates an existing signature request status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature request status updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignatureRequestStatusDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid signature request status data"),
            @ApiResponse(responseCode = "404", description = "Signature request status not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<SignatureRequestStatusDTO>> updateSignatureRequestStatus(
            @Parameter(description = "Signature request status ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated signature request status data", required = true)
            @Valid @RequestBody SignatureRequestStatusDTO signatureRequestStatusDTO) {
        return signatureRequestStatusService.updateSignatureRequestStatus(id, signatureRequestStatusDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete signature request status", description = "Deletes a signature request status by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Signature request status deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Signature request status not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteSignatureRequestStatus(
            @Parameter(description = "Signature request status ID", required = true)
            @PathVariable Long id) {
        return signatureRequestStatusService.deleteSignatureRequestStatus(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}