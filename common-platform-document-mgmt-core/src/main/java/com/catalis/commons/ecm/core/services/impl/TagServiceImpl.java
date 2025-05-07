package com.catalis.commons.ecm.core.services.impl;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.TagMapper;
import com.catalis.commons.ecm.core.services.TagService;
import com.catalis.commons.ecm.interfaces.dtos.TagDTO;
import com.catalis.commons.ecm.models.entities.Tag;
import com.catalis.commons.ecm.models.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

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
    public Mono<TagDTO> getById(Long id) {
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
    public Mono<Void> delete(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Tag not found with ID: " + id)))
                .flatMap(entity -> repository.delete(entity));
    }
}
