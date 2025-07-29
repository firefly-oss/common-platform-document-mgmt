package com.catalis.commons.ecm.web.controllers;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.SignatureStatusService;
import com.catalis.commons.ecm.interfaces.dtos.SignatureStatusDTO;
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
@RequestMapping("/api/v1/signature-statuses")
@Tag(name = "Signature Statuses", description = "API for managing signature statuses")
@RequiredArgsConstructor
public class SignatureStatusController {

    private final SignatureStatusService signatureStatusService;

    @Operation(summary = "Get signature status by ID", description = "Retrieves a signature status by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature status found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignatureStatusDTO.class))),
            @ApiResponse(responseCode = "404", description = "Signature status not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SignatureStatusDTO>> getSignatureStatusById(
            @Parameter(description = "Signature status ID", required = true)
            @PathVariable Long id) {
        return signatureStatusService.getSignatureStatusById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filter signature statuses", description = "Filters signature statuses based on provided criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature statuses filtered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/filter")
    public Mono<ResponseEntity<PaginationResponse<SignatureStatusDTO>>> filterSignatureStatuses(
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<SignatureStatusDTO> filterRequest) {
        return signatureStatusService.filterSignatureStatuses(filterRequest)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Create signature status", description = "Creates a new signature status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Signature status created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignatureStatusDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid signature status data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<SignatureStatusDTO>> createSignatureStatus(
            @Parameter(description = "Signature status data", required = true)
            @Valid @RequestBody SignatureStatusDTO signatureStatusDTO) {
        return signatureStatusService.createSignatureStatus(signatureStatusDTO)
                .map(createdStatus -> ResponseEntity.status(HttpStatus.CREATED).body(createdStatus));
    }

    @Operation(summary = "Update signature status", description = "Updates an existing signature status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signature status updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignatureStatusDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid signature status data"),
            @ApiResponse(responseCode = "404", description = "Signature status not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<SignatureStatusDTO>> updateSignatureStatus(
            @Parameter(description = "Signature status ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated signature status data", required = true)
            @Valid @RequestBody SignatureStatusDTO signatureStatusDTO) {
        return signatureStatusService.updateSignatureStatus(id, signatureStatusDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete signature status", description = "Deletes a signature status by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Signature status deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Signature status not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteSignatureStatus(
            @Parameter(description = "Signature status ID", required = true)
            @PathVariable Long id) {
        return signatureStatusService.deleteSignatureStatus(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}