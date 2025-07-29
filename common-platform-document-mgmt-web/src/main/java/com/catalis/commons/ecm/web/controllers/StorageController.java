package com.catalis.commons.ecm.web.controllers;

import com.catalis.commons.ecm.core.services.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * REST controller for storage operations.
 * This controller provides endpoints for uploading, downloading, and managing files.
 */
@RestController
@RequestMapping("/api/v1/storage")
@Tag(name = "Storage", description = "API for file storage operations")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @Operation(summary = "Upload file", description = "Uploads a file to the storage provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "File uploaded successfully",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid file or path"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<String>> uploadFile(
            @Parameter(description = "File to upload", required = true)
            @RequestPart("file") FilePart filePart,
            @Parameter(description = "Path where the file should be stored", required = true)
            @RequestParam("path") String path,
            @Parameter(description = "Storage provider name (optional)")
            @RequestParam(required = false) String provider) {
        
        Mono<String> result = provider != null ?
                storageService.uploadFile(filePart, path, provider) :
                storageService.uploadFile(filePart, path);
        
        return result.map(fileUrl -> ResponseEntity.created(URI.create(fileUrl)).body(fileUrl));
    }

    @Operation(summary = "Download file", description = "Downloads a file from the storage provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File downloaded successfully",
                    content = @Content(mediaType = "application/octet-stream")),
            @ApiResponse(responseCode = "404", description = "File not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Mono<ResponseEntity<Flux<DataBuffer>>> downloadFile(
            @Parameter(description = "URL of the file to download", required = true)
            @RequestParam("url") String fileUrl,
            @Parameter(description = "Storage provider name (optional)")
            @RequestParam(required = false) String provider) {
        
        // Extract the filename from the URL for the Content-Disposition header
        String decodedUrl = URLDecoder.decode(fileUrl, StandardCharsets.UTF_8);
        String fileName = decodedUrl.substring(decodedUrl.lastIndexOf('/') + 1);
        
        Flux<DataBuffer> result = provider != null ?
                storageService.downloadFile(fileUrl, provider) :
                storageService.downloadFile(fileUrl);
        
        return Mono.just(ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(result));
    }

    @Operation(summary = "Delete file", description = "Deletes a file from the storage provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "File deleted successfully"),
            @ApiResponse(responseCode = "404", description = "File not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteFile(
            @Parameter(description = "URL of the file to delete", required = true)
            @RequestParam("url") String fileUrl,
            @Parameter(description = "Storage provider name (optional)")
            @RequestParam(required = false) String provider) {
        
        Mono<Void> result = provider != null ?
                storageService.deleteFile(fileUrl, provider) :
                storageService.deleteFile(fileUrl);
        
        return result.then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

    @Operation(summary = "Check if file exists", description = "Checks if a file exists in the storage provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File existence check result",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/exists", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Boolean>> fileExists(
            @Parameter(description = "URL of the file to check", required = true)
            @RequestParam("url") String fileUrl,
            @Parameter(description = "Storage provider name (optional)")
            @RequestParam(required = false) String provider) {
        
        Mono<Boolean> result = provider != null ?
                storageService.fileExists(fileUrl, provider) :
                storageService.fileExists(fileUrl);
        
        return result.map(ResponseEntity::ok);
    }

    @Operation(summary = "Generate pre-signed URL", description = "Generates a pre-signed URL for temporary access to a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pre-signed URL generated successfully",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "File not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/presigned-url", produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<ResponseEntity<String>> generatePresignedUrl(
            @Parameter(description = "URL of the file", required = true)
            @RequestParam("url") String fileUrl,
            @Parameter(description = "Expiration time in seconds", required = true)
            @RequestParam("expiration") long expirationInSeconds,
            @Parameter(description = "Storage provider name (optional)")
            @RequestParam(required = false) String provider) {
        
        Mono<String> result = provider != null ?
                storageService.generatePresignedUrl(fileUrl, expirationInSeconds, provider) :
                storageService.generatePresignedUrl(fileUrl, expirationInSeconds);
        
        return result.map(ResponseEntity::ok);
    }
}