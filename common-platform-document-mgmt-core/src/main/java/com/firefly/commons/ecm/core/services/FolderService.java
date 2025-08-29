package com.firefly.commons.ecm.core.services;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.interfaces.dtos.FolderDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing Folder entities in the Enterprise Content Management system.
 */
public interface FolderService {

    /**
     * Get a folder by its ID.
     *
     * @param id The folder ID
     * @return A Mono emitting the folder if found, or empty if not found
     */
    Mono<FolderDTO> getById(Long id);

    /**
     * Filter folders based on the provided filter request.
     *
     * @param filterRequest The filter request containing filtering and pagination parameters
     * @return A Mono emitting a pagination response with the filtered folders
     */
    Mono<PaginationResponse<FolderDTO>> filter(FilterRequest<FolderDTO> filterRequest);

    /**
     * Update an existing folder.
     *
     * @param folder The folder to update
     * @return A Mono emitting the updated folder
     */
    Mono<FolderDTO> update(FolderDTO folder);

    /**
     * Create a new folder.
     *
     * @param folder The folder to create
     * @return A Mono emitting the created folder
     */
    Mono<FolderDTO> create(FolderDTO folder);

    /**
     * Delete a folder by its ID.
     *
     * @param id The ID of the folder to delete
     * @return A Mono completing when the folder is deleted
     */
    Mono<Void> delete(Long id);
}
