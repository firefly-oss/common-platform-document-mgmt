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


package com.firefly.commons.ecm.core.services;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.interfaces.dtos.FolderDTO;
import reactor.core.publisher.Mono;
import java.util.UUID;
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
    Mono<FolderDTO> getById(UUID id);

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
    Mono<Void> delete(UUID id);
}
