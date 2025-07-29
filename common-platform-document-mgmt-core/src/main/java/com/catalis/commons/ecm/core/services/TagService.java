package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.interfaces.dtos.TagDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing tags.
 */
public interface TagService {
    /**
     * Filters the tags based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for TagDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of tags
     */
    Mono<PaginationResponse<TagDTO>> filterTags(FilterRequest<TagDTO> filterRequest);
    
    /**
     * Creates a new tag based on the provided information.
     *
     * @param tagDTO the DTO object containing details of the tag to be created
     * @return a Mono that emits the created TagDTO object
     */
    Mono<TagDTO> createTag(TagDTO tagDTO);
    
    /**
     * Updates an existing tag with updated information.
     *
     * @param tagId the unique identifier of the tag to be updated
     * @param tagDTO the data transfer object containing the updated details of the tag
     * @return a reactive Mono containing the updated TagDTO
     */
    Mono<TagDTO> updateTag(Long tagId, TagDTO tagDTO);
    
    /**
     * Deletes a tag identified by its unique ID.
     *
     * @param tagId the unique identifier of the tag to be deleted
     * @return a Mono that completes when the tag is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deleteTag(Long tagId);
    
    /**
     * Retrieves a tag by its unique identifier.
     *
     * @param tagId the unique identifier of the tag to retrieve
     * @return a Mono emitting the {@link TagDTO} representing the tag if found,
     *         or an empty Mono if the tag does not exist
     */
    Mono<TagDTO> getTagById(Long tagId);
}