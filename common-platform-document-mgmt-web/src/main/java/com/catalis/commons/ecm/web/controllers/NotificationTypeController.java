package com.catalis.commons.ecm.web.controllers;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.NotificationTypeService;
import com.catalis.commons.ecm.interfaces.dtos.NotificationTypeDTO;
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
@RequestMapping("/api/v1/notification-types")
@Tag(name = "Notification Types", description = "API for managing notification types")
@RequiredArgsConstructor
public class NotificationTypeController {

    private final NotificationTypeService notificationTypeService;

    @Operation(summary = "Get notification type by ID", description = "Retrieves a notification type by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification type found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationTypeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Notification type not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<NotificationTypeDTO>> getNotificationTypeById(
            @Parameter(description = "Notification type ID", required = true)
            @PathVariable Long id) {
        return notificationTypeService.getNotificationTypeById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filter notification types", description = "Filters notification types based on provided criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification types filtered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/filter")
    public Mono<ResponseEntity<PaginationResponse<NotificationTypeDTO>>> filterNotificationTypes(
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<NotificationTypeDTO> filterRequest) {
        return notificationTypeService.filterNotificationTypes(filterRequest)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Create notification type", description = "Creates a new notification type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notification type created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationTypeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid notification type data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<NotificationTypeDTO>> createNotificationType(
            @Parameter(description = "Notification type data", required = true)
            @Valid @RequestBody NotificationTypeDTO notificationTypeDTO) {
        return notificationTypeService.createNotificationType(notificationTypeDTO)
                .map(createdType -> ResponseEntity.status(HttpStatus.CREATED).body(createdType));
    }

    @Operation(summary = "Update notification type", description = "Updates an existing notification type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification type updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationTypeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid notification type data"),
            @ApiResponse(responseCode = "404", description = "Notification type not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<NotificationTypeDTO>> updateNotificationType(
            @Parameter(description = "Notification type ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated notification type data", required = true)
            @Valid @RequestBody NotificationTypeDTO notificationTypeDTO) {
        return notificationTypeService.updateNotificationType(id, notificationTypeDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete notification type", description = "Deletes a notification type by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notification type deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Notification type not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteNotificationType(
            @Parameter(description = "Notification type ID", required = true)
            @PathVariable Long id) {
        return notificationTypeService.deleteNotificationType(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}