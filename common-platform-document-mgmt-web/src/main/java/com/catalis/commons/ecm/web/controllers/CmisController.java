package com.catalis.commons.ecm.web.controllers;

import com.catalis.commons.ecm.interfaces.dtos.cmis.CmisObjectDTO;
import com.catalis.commons.ecm.interfaces.dtos.cmis.CmisRepositoryDTO;
import com.catalis.commons.ecm.core.services.CmisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for CMIS operations.
 */
@RestController
@RequestMapping("/api/v1/cmis")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "CMIS Controller", description = "API for CMIS operations")
public class CmisController {

    private final CmisService cmisService;

    @GetMapping("/repositories")
    @Operation(summary = "Get all repositories", description = "Returns all available CMIS repositories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved repositories",
                    content = @Content(schema = @Schema(implementation = CmisRepositoryDTO.class)))
    })
    public Flux<CmisRepositoryDTO> getRepositories() {
        return cmisService.getRepositories();
    }

    @GetMapping("/repositories/{repositoryId}")
    @Operation(summary = "Get repository by ID", description = "Returns a repository by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved repository",
                    content = @Content(schema = @Schema(implementation = CmisRepositoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Repository not found")
    })
    public Mono<CmisRepositoryDTO> getRepository(
            @Parameter(description = "ID of the repository to retrieve") @PathVariable String repositoryId) {
        return cmisService.getRepository(repositoryId)
                .switchIfEmpty(Mono.error(new RuntimeException("Repository not found")));
    }

    @GetMapping("/repositories/{repositoryId}/rootFolder")
    @Operation(summary = "Get root folder", description = "Returns the root folder of a repository")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved root folder",
                    content = @Content(schema = @Schema(implementation = CmisObjectDTO.class))),
            @ApiResponse(responseCode = "404", description = "Repository not found")
    })
    public Mono<CmisObjectDTO> getRootFolder(
            @Parameter(description = "ID of the repository") @PathVariable String repositoryId) {
        return cmisService.getRootFolder(repositoryId)
                .switchIfEmpty(Mono.error(new RuntimeException("Root folder not found")));
    }

    @GetMapping("/repositories/{repositoryId}/objects/{objectId}")
    @Operation(summary = "Get object by ID", description = "Returns an object by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved object",
                    content = @Content(schema = @Schema(implementation = CmisObjectDTO.class))),
            @ApiResponse(responseCode = "404", description = "Object not found")
    })
    public Mono<CmisObjectDTO> getObject(
            @Parameter(description = "ID of the repository") @PathVariable String repositoryId,
            @Parameter(description = "ID of the object to retrieve") @PathVariable String objectId) {
        return cmisService.getObject(repositoryId, objectId)
                .switchIfEmpty(Mono.error(new RuntimeException("Object not found")));
    }

    @GetMapping("/repositories/{repositoryId}/objects")
    @Operation(summary = "Get object by path", description = "Returns an object by its path")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved object",
                    content = @Content(schema = @Schema(implementation = CmisObjectDTO.class))),
            @ApiResponse(responseCode = "404", description = "Object not found")
    })
    public Mono<CmisObjectDTO> getObjectByPath(
            @Parameter(description = "ID of the repository") @PathVariable String repositoryId,
            @Parameter(description = "Path of the object to retrieve") @RequestParam String path) {
        return cmisService.getObjectByPath(repositoryId, path)
                .switchIfEmpty(Mono.error(new RuntimeException("Object not found")));
    }

    @GetMapping("/repositories/{repositoryId}/folders/{folderId}/children")
    @Operation(summary = "Get children of a folder", description = "Returns the children of a folder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved children",
                    content = @Content(schema = @Schema(implementation = CmisObjectDTO.class))),
            @ApiResponse(responseCode = "404", description = "Folder not found")
    })
    public Flux<CmisObjectDTO> getChildren(
            @Parameter(description = "ID of the repository") @PathVariable String repositoryId,
            @Parameter(description = "ID of the folder") @PathVariable String folderId) {
        return cmisService.getChildren(repositoryId, folderId);
    }

    @PostMapping("/repositories/{repositoryId}/folders/{folderId}/documents")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a document", description = "Creates a new document in a folder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document created successfully",
                    content = @Content(schema = @Schema(implementation = CmisObjectDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid document data"),
            @ApiResponse(responseCode = "404", description = "Repository or folder not found")
    })
    public Mono<CmisObjectDTO> createDocument(
            @Parameter(description = "ID of the repository") @PathVariable String repositoryId,
            @Parameter(description = "ID of the folder") @PathVariable String folderId,
            @Parameter(description = "Name of the document") @RequestParam String name,
            @Parameter(description = "Content type of the document") @RequestParam String contentType,
            @Parameter(description = "Content of the document") @RequestPart("content") FilePart content,
            @Parameter(description = "Properties of the document") @RequestParam(required = false) Map<String, Object> properties) {
        
        if (properties == null) {
            properties = new HashMap<>();
        }
        
        Map<String, Object> finalProperties = properties;
        
        return content.content()
                .reduce(new byte[0], (acc, dataBuffer) -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    byte[] result = new byte[acc.length + bytes.length];
                    System.arraycopy(acc, 0, result, 0, acc.length);
                    System.arraycopy(bytes, 0, result, acc.length, bytes.length);
                    return result;
                })
                .flatMap(bytes -> cmisService.createDocument(repositoryId, folderId, name, contentType, bytes, finalProperties))
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to create document")));
    }

    @PostMapping("/repositories/{repositoryId}/folders/{parentFolderId}/folders")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a folder", description = "Creates a new folder in a parent folder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Folder created successfully",
                    content = @Content(schema = @Schema(implementation = CmisObjectDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid folder data"),
            @ApiResponse(responseCode = "404", description = "Repository or parent folder not found")
    })
    public Mono<CmisObjectDTO> createFolder(
            @Parameter(description = "ID of the repository") @PathVariable String repositoryId,
            @Parameter(description = "ID of the parent folder") @PathVariable String parentFolderId,
            @Parameter(description = "Name of the folder") @RequestParam String name,
            @Parameter(description = "Properties of the folder") @RequestParam(required = false) Map<String, Object> properties) {
        
        if (properties == null) {
            properties = new HashMap<>();
        }
        
        return cmisService.createFolder(repositoryId, parentFolderId, name, properties)
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to create folder")));
    }

    @PutMapping("/repositories/{repositoryId}/objects/{objectId}")
    @Operation(summary = "Update object properties", description = "Updates the properties of an object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Object properties updated successfully",
                    content = @Content(schema = @Schema(implementation = CmisObjectDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid properties"),
            @ApiResponse(responseCode = "404", description = "Repository or object not found")
    })
    public Mono<CmisObjectDTO> updateProperties(
            @Parameter(description = "ID of the repository") @PathVariable String repositoryId,
            @Parameter(description = "ID of the object") @PathVariable String objectId,
            @Parameter(description = "Properties to update") @RequestBody Map<String, Object> properties) {
        
        return cmisService.updateProperties(repositoryId, objectId, properties)
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to update properties")));
    }

    @DeleteMapping("/repositories/{repositoryId}/objects/{objectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an object", description = "Deletes an object by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Object deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Repository or object not found")
    })
    public Mono<Void> deleteObject(
            @Parameter(description = "ID of the repository") @PathVariable String repositoryId,
            @Parameter(description = "ID of the object to delete") @PathVariable String objectId,
            @Parameter(description = "Whether to delete all versions") @RequestParam(defaultValue = "false") boolean allVersions) {
        
        return cmisService.deleteObject(repositoryId, objectId, allVersions)
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to delete object")));
    }

    @GetMapping(value = "/repositories/{repositoryId}/objects/{objectId}/content", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "Get content stream", description = "Returns the content stream of a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved content stream"),
            @ApiResponse(responseCode = "404", description = "Repository or object not found")
    })
    public Mono<ResponseEntity<byte[]>> getContentStream(
            @Parameter(description = "ID of the repository") @PathVariable String repositoryId,
            @Parameter(description = "ID of the object") @PathVariable String objectId) {
        
        return cmisService.getContentStream(repositoryId, objectId)
                .map(content -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(content))
                .switchIfEmpty(Mono.error(new RuntimeException("Content not found")));
    }

    @PutMapping("/repositories/{repositoryId}/objects/{objectId}/content")
    @Operation(summary = "Set content stream", description = "Sets the content stream of a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Content stream set successfully",
                    content = @Content(schema = @Schema(implementation = CmisObjectDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid content"),
            @ApiResponse(responseCode = "404", description = "Repository or object not found")
    })
    public Mono<CmisObjectDTO> setContentStream(
            @Parameter(description = "ID of the repository") @PathVariable String repositoryId,
            @Parameter(description = "ID of the object") @PathVariable String objectId,
            @Parameter(description = "Content type of the document") @RequestParam String contentType,
            @Parameter(description = "Whether to overwrite existing content") @RequestParam(defaultValue = "true") boolean overwrite,
            @Parameter(description = "Content of the document") @RequestPart("content") FilePart content) {
        
        return content.content()
                .reduce(new byte[0], (acc, dataBuffer) -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    byte[] result = new byte[acc.length + bytes.length];
                    System.arraycopy(acc, 0, result, 0, acc.length);
                    System.arraycopy(bytes, 0, result, acc.length, bytes.length);
                    return result;
                })
                .flatMap(bytes -> cmisService.setContentStream(repositoryId, objectId, contentType, bytes, overwrite))
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to set content stream")));
    }

    @GetMapping("/repositories/{repositoryId}/query")
    @Operation(summary = "Query repository", description = "Executes a query against the repository")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query executed successfully",
                    content = @Content(schema = @Schema(implementation = CmisObjectDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid query"),
            @ApiResponse(responseCode = "404", description = "Repository not found")
    })
    public Flux<CmisObjectDTO> query(
            @Parameter(description = "ID of the repository") @PathVariable String repositoryId,
            @Parameter(description = "Query statement") @RequestParam String statement,
            @Parameter(description = "Whether to search all versions") @RequestParam(defaultValue = "false") boolean searchAllVersions) {
        
        return cmisService.query(repositoryId, statement, searchAllVersions);
    }

    @PostMapping("/repositories/{repositoryId}/objects/{objectId}/checkout")
    @Operation(summary = "Check out a document", description = "Checks out a document for editing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document checked out successfully",
                    content = @Content(schema = @Schema(implementation = CmisObjectDTO.class))),
            @ApiResponse(responseCode = "404", description = "Repository or object not found")
    })
    public Mono<CmisObjectDTO> checkOut(
            @Parameter(description = "ID of the repository") @PathVariable String repositoryId,
            @Parameter(description = "ID of the object") @PathVariable String objectId) {
        
        return cmisService.checkOut(repositoryId, objectId)
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to check out document")));
    }

    @DeleteMapping("/repositories/{repositoryId}/objects/{objectId}/checkout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancel check out", description = "Cancels a check out")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Check out canceled successfully"),
            @ApiResponse(responseCode = "404", description = "Repository or object not found")
    })
    public Mono<Void> cancelCheckOut(
            @Parameter(description = "ID of the repository") @PathVariable String repositoryId,
            @Parameter(description = "ID of the object") @PathVariable String objectId) {
        
        return cmisService.cancelCheckOut(repositoryId, objectId)
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to cancel check out")));
    }

    @PostMapping("/repositories/{repositoryId}/objects/{objectId}/checkin")
    @Operation(summary = "Check in a document", description = "Checks in a document after editing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document checked in successfully",
                    content = @Content(schema = @Schema(implementation = CmisObjectDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid check-in data"),
            @ApiResponse(responseCode = "404", description = "Repository or object not found")
    })
    public Mono<CmisObjectDTO> checkIn(
            @Parameter(description = "ID of the repository") @PathVariable String repositoryId,
            @Parameter(description = "ID of the object") @PathVariable String objectId,
            @Parameter(description = "Whether it's a major version") @RequestParam(defaultValue = "true") boolean major,
            @Parameter(description = "Check-in comment") @RequestParam(required = false) String comment,
            @Parameter(description = "Content type of the document") @RequestParam(required = false) String contentType,
            @Parameter(description = "Properties to set") @RequestParam(required = false) Map<String, Object> properties,
            @Parameter(description = "Content of the document") @RequestPart(value = "content", required = false) FilePart content) {
        
        if (properties == null) {
            properties = new HashMap<>();
        }
        
        Map<String, Object> finalProperties = properties;
        String finalComment = comment != null ? comment : "";
        
        if (content != null) {
            return content.content()
                    .reduce(new byte[0], (acc, dataBuffer) -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        byte[] result = new byte[acc.length + bytes.length];
                        System.arraycopy(acc, 0, result, 0, acc.length);
                        System.arraycopy(bytes, 0, result, acc.length, bytes.length);
                        return result;
                    })
                    .flatMap(bytes -> cmisService.checkIn(repositoryId, objectId, major, finalProperties, contentType, bytes, finalComment))
                    .switchIfEmpty(Mono.error(new RuntimeException("Failed to check in document")));
        } else {
            return cmisService.checkIn(repositoryId, objectId, major, finalProperties, null, null, finalComment)
                    .switchIfEmpty(Mono.error(new RuntimeException("Failed to check in document")));
        }
    }

    @GetMapping("/repositories/{repositoryId}/objects/{objectId}/versions")
    @Operation(summary = "Get all versions", description = "Returns all versions of a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved versions",
                    content = @Content(schema = @Schema(implementation = CmisObjectDTO.class))),
            @ApiResponse(responseCode = "404", description = "Repository or object not found")
    })
    public Flux<CmisObjectDTO> getAllVersions(
            @Parameter(description = "ID of the repository") @PathVariable String repositoryId,
            @Parameter(description = "ID of the object") @PathVariable String objectId) {
        
        return cmisService.getAllVersions(repositoryId, objectId);
    }
}