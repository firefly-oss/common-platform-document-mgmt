package com.catalis.commons.ecm.web.controllers;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.services.NotificationLogService;
import com.catalis.commons.ecm.interfaces.dtos.NotificationLogDTO;
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
@RequestMapping("/api/v1/notification-logs")
@Tag(name = "Notification Logs", description = "API for managing notification logs")
@RequiredArgsConstructor
public class NotificationLogController {

    private final NotificationLogService notificationLogService;

    @Operation(summary = "Get notification log by ID", description = "Retrieves a notification log by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification log found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationLogDTO.class))),
            @ApiResponse(responseCode = "404", description = "Notification log not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<NotificationLogDTO>> getNotificationLogById(
            @Parameter(description = "Notification log ID", required = true)
            @PathVariable Long id) {
        return notificationLogService.getNotificationLogById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filter notification logs", description = "Filters notification logs based on provided criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification logs filtered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/filter")
    public Mono<ResponseEntity<PaginationResponse<NotificationLogDTO>>> filterNotificationLogs(
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<NotificationLogDTO> filterRequest) {
        return notificationLogService.filterNotificationLogs(filterRequest)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Create notification log", description = "Creates a new notification log")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notification log created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationLogDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid notification log data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<NotificationLogDTO>> createNotificationLog(
            @Parameter(description = "Notification log data", required = true)
            @Valid @RequestBody NotificationLogDTO notificationLogDTO) {
        return notificationLogService.createNotificationLog(notificationLogDTO)
                .map(createdLog -> ResponseEntity.status(HttpStatus.CREATED).body(createdLog));
    }

    @Operation(summary = "Update notification log", description = "Updates an existing notification log")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification log updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationLogDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid notification log data"),
            @ApiResponse(responseCode = "404", description = "Notification log not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<NotificationLogDTO>> updateNotificationLog(
            @Parameter(description = "Notification log ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated notification log data", required = true)
            @Valid @RequestBody NotificationLogDTO notificationLogDTO) {
        return notificationLogService.updateNotificationLog(id, notificationLogDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete notification log", description = "Deletes a notification log by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notification log deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Notification log not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteNotificationLog(
            @Parameter(description = "Notification log ID", required = true)
            @PathVariable Long id) {
        return notificationLogService.deleteNotificationLog(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}