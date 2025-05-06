package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.models.entities.Tag;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing Tag entities in the Enterprise Content Management system.
 */
public interface TagService {

    /**
     * Get a tag by its ID.
     *
     * @param id The tag ID
     * @return A Mono emitting the tag if found, or empty if not found
     */
    Mono<Tag> getById(Long id);

    /**
     * Filter tags based on the provided filter request.
     *
     * @param filterRequest The filter request containing filtering and pagination parameters
     * @return A Mono emitting a pagination response with the filtered tags
     */
    Mono<PaginationResponse<Tag>> filter(FilterRequest<Tag> filterRequest);

    /**
     * Update an existing tag.
     *
     * @param tag The tag to update
     * @return A Mono emitting the updated tag
     */
    Mono<Tag> update(Tag tag);

    /**
     * Create a new tag.
     *
     * @param tag The tag to create
     * @return A Mono emitting the created tag
     */
    Mono<Tag> create(Tag tag);

    /**
     * Delete a tag by its ID.
     *
     * @param id The ID of the tag to delete
     * @return A Mono completing when the tag is deleted
     */
    Mono<Void> delete(Long id);
}
