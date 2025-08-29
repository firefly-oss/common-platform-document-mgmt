package com.firefly.commons.ecm.core.services;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.interfaces.dtos.DocumentPermissionDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing DocumentPermission entities in the Enterprise Content Management system.
 */
public interface DocumentPermissionService {

    /**
     * Get a document permission by its ID.
     *
     * @param id The document permission ID
     * @return A Mono emitting the document permission if found, or empty if not found
     */
    Mono<DocumentPermissionDTO> getById(Long id);

    /**
     * Filter document permissions based on the provided filter request.
     *
     * @param filterRequest The filter request containing filtering and pagination parameters
     * @return A Mono emitting a pagination response with the filtered document permissions
     */
    Mono<PaginationResponse<DocumentPermissionDTO>> filter(FilterRequest<DocumentPermissionDTO> filterRequest);

    /**
     * Update an existing document permission.
     *
     * @param documentPermission The document permission to update
     * @return A Mono emitting the updated document permission
     */
    Mono<DocumentPermissionDTO> update(DocumentPermissionDTO documentPermission);

    /**
     * Create a new document permission.
     *
     * @param documentPermission The document permission to create
     * @return A Mono emitting the created document permission
     */
    Mono<DocumentPermissionDTO> create(DocumentPermissionDTO documentPermission);

    /**
     * Delete a document permission by its ID.
     *
     * @param id The ID of the document permission to delete
     * @return A Mono completing when the document permission is deleted
     */
    Mono<Void> delete(Long id);
}
