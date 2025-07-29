package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.interfaces.dtos.FileDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing files.
 */
public interface FileService {
    /**
     * Filters the files based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for FileDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of files
     */
    Mono<PaginationResponse<FileDTO>> filterFiles(FilterRequest<FileDTO> filterRequest);
    
    /**
     * Creates a new file based on the provided information.
     *
     * @param fileDTO the DTO object containing details of the file to be created
     * @return a Mono that emits the created FileDTO object
     */
    Mono<FileDTO> createFile(FileDTO fileDTO);
    
    /**
     * Updates an existing file with updated information.
     *
     * @param fileId the unique identifier of the file to be updated
     * @param fileDTO the data transfer object containing the updated details of the file
     * @return a reactive Mono containing the updated FileDTO
     */
    Mono<FileDTO> updateFile(Long fileId, FileDTO fileDTO);
    
    /**
     * Deletes a file identified by its unique ID.
     *
     * @param fileId the unique identifier of the file to be deleted
     * @return a Mono that completes when the file is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deleteFile(Long fileId);
    
    /**
     * Retrieves a file by its unique identifier.
     *
     * @param fileId the unique identifier of the file to retrieve
     * @return a Mono emitting the {@link FileDTO} representing the file if found,
     *         or an empty Mono if the file does not exist
     */
    Mono<FileDTO> getFileById(Long fileId);
}