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

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.filters.FilterUtils;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.core.mappers.DocumentTagMapper;
import com.firefly.commons.ecm.core.services.DocumentTagService;
import com.firefly.commons.ecm.interfaces.dtos.DocumentTagDTO;
import com.firefly.commons.ecm.models.entities.DocumentTag;
import com.firefly.commons.ecm.models.repositories.DocumentTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import java.util.UUID;
/**
 * Implementation of the DocumentTagService interface.
 */
@Service
@Transactional
public class DocumentTagServiceImpl implements DocumentTagService {

    @Autowired
    private DocumentTagRepository repository;

    @Autowired
    private DocumentTagMapper mapper;

    @Override
    public Mono<DocumentTagDTO> getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<DocumentTagDTO>> filter(FilterRequest<DocumentTagDTO> filterRequest) {
        return FilterUtils.createFilter(
                DocumentTag.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<DocumentTagDTO> update(DocumentTagDTO documentTag) {
        if (documentTag.getId() == null) {
            return Mono.error(new IllegalArgumentException("ID cannot be null for update operation"));
        }

        return repository.findById(documentTag.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Document tag not found with ID: " + documentTag.getId())))
                .flatMap(existingEntity -> {
                    DocumentTag entityToUpdate = mapper.toEntity(documentTag);
                    // Preserve created info
                    entityToUpdate.setCreatedAt(existingEntity.getCreatedAt());
                    entityToUpdate.setCreatedBy(existingEntity.getCreatedBy());
                    return repository.save(entityToUpdate);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<DocumentTagDTO> create(DocumentTagDTO documentTag) {
        // Ensure ID is null for create operation
        documentTag.setId(null);

        DocumentTag entity = mapper.toEntity(documentTag);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Document tag not found with ID: " + id)))
                .flatMap(entity -> repository.delete(entity));
    }
}
