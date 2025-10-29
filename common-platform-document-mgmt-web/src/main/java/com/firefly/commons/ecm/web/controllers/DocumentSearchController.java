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

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.core.services.DocumentSearchService;
import com.firefly.commons.ecm.interfaces.dtos.DocumentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/documents/search")
@RequiredArgsConstructor
@Tag(name = "Document Search Controller", description = "Filter-based document search API")
public class DocumentSearchController {

    private final DocumentSearchService documentSearchService;

    @PostMapping("/filter")
    @Operation(summary = "Filter documents", description = "Filters documents using a FilterRequest, consistent with other services")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved documents",
                    content = @Content(schema = @Schema(implementation = PaginationResponse.class)))
    })
    public Mono<PaginationResponse<DocumentDTO>> filter(@RequestBody FilterRequest<DocumentDTO> filterRequest) {
        return documentSearchService.filter(filterRequest != null ? filterRequest : new FilterRequest<>());
    }
}
