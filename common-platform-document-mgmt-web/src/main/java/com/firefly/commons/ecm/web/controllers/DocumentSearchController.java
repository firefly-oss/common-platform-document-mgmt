/*
 * Copyright 2025 Firefly Software Solutions Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.firefly.commons.ecm.web.controllers;

import com.firefly.commons.ecm.core.services.DocumentSearchService;
import com.firefly.commons.ecm.interfaces.dtos.DocumentDTO;
import com.firefly.core.ecm.domain.dto.search.DocumentSearchCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/documents/search")
@RequiredArgsConstructor
@Tag(name = "Document Search Controller", description = "API for searching documents via ECM search port")
public class DocumentSearchController {

    private final DocumentSearchService documentSearchService;

    @GetMapping
    @Operation(summary = "Full-text search", description = "Performs full-text search across content and metadata")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved search results",
                    content = @Content(schema = @Schema(implementation = DocumentDTO.class)))
    })
    public Flux<DocumentDTO> fullTextSearch(
            @Parameter(description = "Search query") @RequestParam("q") String query,
            @Parameter(description = "Maximum number of results") @RequestParam(value = "limit", required = false) Integer limit) {
        return documentSearchService.fullTextSearch(query, limit);
    }

    @GetMapping("/name")
    @Operation(summary = "Search by name pattern", description = "Searches documents by name (supports wildcards)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved search results",
                    content = @Content(schema = @Schema(implementation = DocumentDTO.class)))
    })
    public Flux<DocumentDTO> searchByName(
            @Parameter(description = "Name pattern (supports wildcards)") @RequestParam("pattern") String pattern,
            @Parameter(description = "Maximum number of results") @RequestParam(value = "limit", required = false) Integer limit) {
        return documentSearchService.searchByName(pattern, limit);
    }

    @GetMapping("/tags")
    @Operation(summary = "Search by tags", description = "Searches documents by tags")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved search results",
                    content = @Content(schema = @Schema(implementation = DocumentDTO.class)))
    })
    public Flux<DocumentDTO> searchByTags(
            @Parameter(description = "Comma-separated list of tags") @RequestParam("tags") Set<String> tags,
            @Parameter(description = "Match all tags if true, any tag if false") @RequestParam(value = "matchAll", required = false) Boolean matchAll,
            @Parameter(description = "Maximum number of results") @RequestParam(value = "limit", required = false) Integer limit) {
        return documentSearchService.searchByTags(tags, matchAll, limit);
    }

    @PostMapping("/advanced")
    @Operation(summary = "Advanced search", description = "Performs advanced search with multiple criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved search results",
                    content = @Content(schema = @Schema(implementation = DocumentDTO.class)))
    })
    public Flux<DocumentDTO> advancedSearch(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Advanced search criteria",
                    required = true,
                    content = @Content(schema = @Schema(implementation = DocumentSearchCriteria.class)))
            @RequestBody DocumentSearchCriteria criteria) {
        return documentSearchService.advancedSearch(criteria);
    }
}