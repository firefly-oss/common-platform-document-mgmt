package com.catalis.commons.ecm.core.services;

import com.catalis.commons.ecm.interfaces.dtos.cmis.CmisObjectDTO;
import com.catalis.commons.ecm.interfaces.dtos.cmis.CmisRepositoryDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Service interface for CMIS operations.
 */
public interface CmisService {

    /**
     * Get all available repositories.
     *
     * @return Flux of repositories
     */
    Flux<CmisRepositoryDTO> getRepositories();

    /**
     * Get a repository by ID.
     *
     * @param repositoryId the repository ID
     * @return the repository
     */
    Mono<CmisRepositoryDTO> getRepository(String repositoryId);

    /**
     * Get the root folder of a repository.
     *
     * @param repositoryId the repository ID
     * @return the root folder
     */
    Mono<CmisObjectDTO> getRootFolder(String repositoryId);

    /**
     * Get an object by ID.
     *
     * @param repositoryId the repository ID
     * @param objectId the object ID
     * @return the object
     */
    Mono<CmisObjectDTO> getObject(String repositoryId, String objectId);

    /**
     * Get an object by path.
     *
     * @param repositoryId the repository ID
     * @param path the path
     * @return the object
     */
    Mono<CmisObjectDTO> getObjectByPath(String repositoryId, String path);

    /**
     * Get the children of a folder.
     *
     * @param repositoryId the repository ID
     * @param folderId the folder ID
     * @return the children
     */
    Flux<CmisObjectDTO> getChildren(String repositoryId, String folderId);

    /**
     * Create a document.
     *
     * @param repositoryId the repository ID
     * @param folderId the folder ID
     * @param name the document name
     * @param contentType the content type
     * @param content the content
     * @param properties additional properties
     * @return the created document
     */
    Mono<CmisObjectDTO> createDocument(String repositoryId, String folderId, String name, 
                                      String contentType, byte[] content, Map<String, Object> properties);

    /**
     * Create a folder.
     *
     * @param repositoryId the repository ID
     * @param parentFolderId the parent folder ID
     * @param name the folder name
     * @param properties additional properties
     * @return the created folder
     */
    Mono<CmisObjectDTO> createFolder(String repositoryId, String parentFolderId, String name, 
                                    Map<String, Object> properties);

    /**
     * Update an object's properties.
     *
     * @param repositoryId the repository ID
     * @param objectId the object ID
     * @param properties the properties to update
     * @return the updated object
     */
    Mono<CmisObjectDTO> updateProperties(String repositoryId, String objectId, Map<String, Object> properties);

    /**
     * Delete an object.
     *
     * @param repositoryId the repository ID
     * @param objectId the object ID
     * @param allVersions whether to delete all versions
     * @return completion signal
     */
    Mono<Void> deleteObject(String repositoryId, String objectId, boolean allVersions);

    /**
     * Get the content stream of a document.
     *
     * @param repositoryId the repository ID
     * @param objectId the object ID
     * @return the content stream
     */
    Mono<byte[]> getContentStream(String repositoryId, String objectId);

    /**
     * Set the content stream of a document.
     *
     * @param repositoryId the repository ID
     * @param objectId the object ID
     * @param contentType the content type
     * @param content the content
     * @param overwrite whether to overwrite existing content
     * @return the updated document
     */
    Mono<CmisObjectDTO> setContentStream(String repositoryId, String objectId, String contentType, 
                                        byte[] content, boolean overwrite);

    /**
     * Query the repository.
     *
     * @param repositoryId the repository ID
     * @param statement the query statement
     * @param searchAllVersions whether to search all versions
     * @return the query results
     */
    Flux<CmisObjectDTO> query(String repositoryId, String statement, boolean searchAllVersions);

    /**
     * Check out a document.
     *
     * @param repositoryId the repository ID
     * @param objectId the object ID
     * @return the private working copy
     */
    Mono<CmisObjectDTO> checkOut(String repositoryId, String objectId);

    /**
     * Cancel a check out.
     *
     * @param repositoryId the repository ID
     * @param objectId the object ID
     * @return completion signal
     */
    Mono<Void> cancelCheckOut(String repositoryId, String objectId);

    /**
     * Check in a document.
     *
     * @param repositoryId the repository ID
     * @param objectId the object ID
     * @param major whether it's a major version
     * @param properties the properties to set
     * @param contentType the content type
     * @param content the content
     * @param comment the check-in comment
     * @return the checked-in document
     */
    Mono<CmisObjectDTO> checkIn(String repositoryId, String objectId, boolean major, 
                               Map<String, Object> properties, String contentType, 
                               byte[] content, String comment);

    /**
     * Get all versions of a document.
     *
     * @param repositoryId the repository ID
     * @param objectId the object ID
     * @return the versions
     */
    Flux<CmisObjectDTO> getAllVersions(String repositoryId, String objectId);
}