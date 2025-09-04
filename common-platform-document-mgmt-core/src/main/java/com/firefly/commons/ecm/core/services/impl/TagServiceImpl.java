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
import com.firefly.commons.ecm.core.mappers.TagMapper;
import com.firefly.commons.ecm.core.services.TagService;
import com.firefly.commons.ecm.interfaces.dtos.TagDTO;
import com.firefly.commons.ecm.models.entities.Tag;
import com.firefly.commons.ecm.models.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import java.util.UUID;
/**
 * Implementation of the TagService interface.
 */
@Service
@Transactional
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository repository;

    @Autowired
    private TagMapper mapper;

    @Override
    public Mono<TagDTO> getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<TagDTO>> filter(FilterRequest<TagDTO> filterRequest) {
        return FilterUtils.createFilter(
                Tag.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<TagDTO> update(TagDTO tag) {
        if (tag.getId() == null) {
            return Mono.error(new IllegalArgumentException("ID cannot be null for update operation"));
        }

        return repository.findById(tag.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Tag not found with ID: " + tag.getId())))
                .flatMap(existingEntity -> {
                    Tag entityToUpdate = mapper.toEntity(tag);
                    // Preserve created info
                    entityToUpdate.setCreatedAt(existingEntity.getCreatedAt());
                    entityToUpdate.setCreatedBy(existingEntity.getCreatedBy());
                    return repository.save(entityToUpdate);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TagDTO> create(TagDTO tag) {
        // Ensure ID is null for create operation
        tag.setId(null);

        Tag entity = mapper.toEntity(tag);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Tag not found with ID: " + id)))
                .flatMap(entity -> repository.delete(entity));
    }
}
