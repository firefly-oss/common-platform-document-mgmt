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

package com.firefly.commons.ecm.core.services.impl;

import com.firefly.commons.ecm.core.mappers.EcmDomainMapper;
import com.firefly.commons.ecm.core.services.DocumentSearchService;
import com.firefly.commons.ecm.interfaces.dtos.DocumentDTO;
import com.firefly.core.ecm.domain.dto.search.DocumentSearchCriteria;
import com.firefly.core.ecm.port.document.DocumentSearchPort;
import com.firefly.core.ecm.service.EcmPortProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentSearchServiceImpl implements DocumentSearchService {

    private final EcmPortProvider ecmPortProvider;
    private final EcmDomainMapper ecmDomainMapper;

    private DocumentSearchPort getSearchPortOrThrow() {
        return ecmPortProvider.getDocumentSearchPort()
                .orElseThrow(() -> new RuntimeException("Document search requires ECM DocumentSearchPort to be configured"));
    }

    @Override
    public Flux<DocumentDTO> fullTextSearch(String query, Integer limit) {
        return getSearchPortOrThrow().fullTextSearch(query, limit)
                .map(ecmDomainMapper::fromEcmDocument);
    }

    @Override
    public Flux<DocumentDTO> searchByName(String namePattern, Integer limit) {
        return getSearchPortOrThrow().searchByName(namePattern, limit)
                .map(ecmDomainMapper::fromEcmDocument);
    }

    @Override
    public Flux<DocumentDTO> searchByMetadata(Map<String, Object> metadata, Integer limit) {
        return getSearchPortOrThrow().searchByMetadata(metadata, limit)
                .map(ecmDomainMapper::fromEcmDocument);
    }

    @Override
    public Flux<DocumentDTO> searchByTags(Set<String> tags, Boolean matchAll, Integer limit) {
        return getSearchPortOrThrow().searchByTags(tags, matchAll, limit)
                .map(ecmDomainMapper::fromEcmDocument);
    }

    @Override
    public Flux<DocumentDTO> searchByMimeType(String mimeType, Integer limit) {
        return getSearchPortOrThrow().searchByMimeType(mimeType, limit)
                .map(ecmDomainMapper::fromEcmDocument);
    }

    @Override
    public Flux<DocumentDTO> searchByExtension(String extension, Integer limit) {
        return getSearchPortOrThrow().searchByExtension(extension, limit)
                .map(ecmDomainMapper::fromEcmDocument);
    }

    @Override
    public Flux<DocumentDTO> searchBySize(Long minSize, Long maxSize, Integer limit) {
        return getSearchPortOrThrow().searchBySize(minSize, maxSize, limit)
                .map(ecmDomainMapper::fromEcmDocument);
    }

    @Override
    public Flux<DocumentDTO> searchByCreationDate(Instant fromDate, Instant toDate, Integer limit) {
        return getSearchPortOrThrow().searchByCreationDate(fromDate, toDate, limit)
                .map(ecmDomainMapper::fromEcmDocument);
    }

    @Override
    public Flux<DocumentDTO> searchByModificationDate(Instant fromDate, Instant toDate, Integer limit) {
        return getSearchPortOrThrow().searchByModificationDate(fromDate, toDate, limit)
                .map(ecmDomainMapper::fromEcmDocument);
    }

    @Override
    public Flux<DocumentDTO> searchByCreator(UUID createdBy, Integer limit) {
        return getSearchPortOrThrow().searchByCreator(createdBy, limit)
                .map(ecmDomainMapper::fromEcmDocument);
    }

    @Override
    public Flux<DocumentDTO> advancedSearch(DocumentSearchCriteria searchCriteria) {
        return getSearchPortOrThrow().advancedSearch(searchCriteria)
                .map(ecmDomainMapper::fromEcmDocument);
    }
}