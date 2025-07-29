package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.TagMapper;
import com.catalis.commons.ecm.interfaces.dtos.TagDTO;
import com.catalis.commons.ecm.models.entities.Tag;
import com.catalis.commons.ecm.models.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository repository;

    @Autowired
    private TagMapper mapper;

    @Override
    public Mono<PaginationResponse<TagDTO>> filterTags(FilterRequest<TagDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        Tag.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<TagDTO> createTag(TagDTO tagDTO) {
        return Mono.just(tagDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TagDTO> updateTag(Long tagId, TagDTO tagDTO) {
        return repository.findById(tagId)
                .switchIfEmpty(Mono.error(new RuntimeException("Tag not found with ID: " + tagId)))
                .flatMap(existingTag -> {
                    Tag updatedTag = mapper.toEntity(tagDTO);
                    updatedTag.setId(tagId);
                    return repository.save(updatedTag);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteTag(Long tagId) {
        return repository.findById(tagId)
                .switchIfEmpty(Mono.error(new RuntimeException("Tag not found with ID: " + tagId)))
                .flatMap(tag -> repository.deleteById(tagId));
    }

    @Override
    public Mono<TagDTO> getTagById(Long tagId) {
        return repository.findById(tagId)
                .switchIfEmpty(Mono.error(new RuntimeException("Tag not found with ID: " + tagId)))
                .map(mapper::toDTO);
    }
}