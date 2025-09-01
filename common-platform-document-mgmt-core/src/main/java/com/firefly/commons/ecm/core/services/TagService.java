package com.firefly.commons.ecm.core.services;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.interfaces.dtos.TagDTO;
import reactor.core.publisher.Mono;
import java.util.UUID;
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
    Mono<TagDTO> getById(UUID id);

    /**
     * Filter tags based on the provided filter request.
     *
     * @param filterRequest The filter request containing filtering and pagination parameters
     * @return A Mono emitting a pagination response with the filtered tags
     */
    Mono<PaginationResponse<TagDTO>> filter(FilterRequest<TagDTO> filterRequest);

    /**
     * Update an existing tag.
     *
     * @param tag The tag to update
     * @return A Mono emitting the updated tag
     */
    Mono<TagDTO> update(TagDTO tag);

    /**
     * Create a new tag.
     *
     * @param tag The tag to create
     * @return A Mono emitting the created tag
     */
    Mono<TagDTO> create(TagDTO tag);

    /**
     * Delete a tag by its ID.
     *
     * @param id The ID of the tag to delete
     * @return A Mono completing when the tag is deleted
     */
    Mono<Void> delete(UUID id);
}
