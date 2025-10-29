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

package com.firefly.commons.ecm.core.services;

import com.firefly.commons.ecm.interfaces.dtos.DocumentDTO;
import com.firefly.core.ecm.domain.dto.search.DocumentSearchCriteria;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Service interface for document search operations backed by ECM search port.
 */
public interface DocumentSearchService {

    Flux<DocumentDTO> fullTextSearch(String query, Integer limit);

    Flux<DocumentDTO> searchByName(String namePattern, Integer limit);

    Flux<DocumentDTO> searchByMetadata(Map<String, Object> metadata, Integer limit);

    Flux<DocumentDTO> searchByTags(Set<String> tags, Boolean matchAll, Integer limit);

    Flux<DocumentDTO> searchByMimeType(String mimeType, Integer limit);

    Flux<DocumentDTO> searchByExtension(String extension, Integer limit);

    Flux<DocumentDTO> searchBySize(Long minSize, Long maxSize, Integer limit);

    Flux<DocumentDTO> searchByCreationDate(Instant fromDate, Instant toDate, Integer limit);

    Flux<DocumentDTO> searchByModificationDate(Instant fromDate, Instant toDate, Integer limit);

    Flux<DocumentDTO> searchByCreator(UUID createdBy, Integer limit);

    Flux<DocumentDTO> advancedSearch(DocumentSearchCriteria searchCriteria);
}