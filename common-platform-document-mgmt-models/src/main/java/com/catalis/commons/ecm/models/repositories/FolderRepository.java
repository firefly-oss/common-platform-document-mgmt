package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.interfaces.enums.SecurityLevel;
import com.catalis.commons.ecm.models.Folder;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for managing Folder entities in the Enterprise Content Management system.
 */
@Repository
public interface FolderRepository extends R2dbcRepository<Folder, UUID> {

    /**
     * Find subfolders of a folder.
     *
     * @param parentFolderId The parent folder ID
     * @param tenantId The tenant ID
     * @return A Flux of subfolders
     */
    Flux<Folder> findByParentFolderIdAndTenantId(UUID parentFolderId, String tenantId);

    /**
     * Find root folders (folders with no parent).
     *
     * @param tenantId The tenant ID
     * @return A Flux of root folders
     */
    Flux<Folder> findByParentFolderIdIsNullAndTenantId(String tenantId);

    /**
     * Find folders by name.
     *
     * @param name The folder name
     * @param tenantId The tenant ID
     * @return A Flux of folders with the specified name
     */
    Flux<Folder> findByNameAndTenantId(String name, String tenantId);

    /**
     * Find folders by name containing the specified text.
     *
     * @param name The text to search for in folder names
     * @param tenantId The tenant ID
     * @return A Flux of folders with names containing the specified text
     */
    Flux<Folder> findByNameContainingIgnoreCaseAndTenantId(String name, String tenantId);

    /**
     * Find folders by security level.
     *
     * @param securityLevel The security level
     * @param tenantId The tenant ID
     * @return A Flux of folders with the specified security level
     */
    Flux<Folder> findBySecurityLevelAndTenantId(SecurityLevel securityLevel, String tenantId);

    /**
     * Find system folders.
     *
     * @param tenantId The tenant ID
     * @return A Flux of system folders
     */
    Flux<Folder> findByIsSystemFolderTrueAndTenantId(String tenantId);

    /**
     * Find folders by path.
     *
     * @param path The folder path
     * @param tenantId The tenant ID
     * @return A Flux of folders with the specified path
     */
    Flux<Folder> findByPathAndTenantId(String path, String tenantId);

    /**
     * Find folders by path containing the specified text.
     *
     * @param path The text to search for in folder paths
     * @param tenantId The tenant ID
     * @return A Flux of folders with paths containing the specified text
     */
    Flux<Folder> findByPathContainingAndTenantId(String path, String tenantId);

    /**
     * Search for folders by name or description.
     *
     * @param searchTerm The search term
     * @param tenantId The tenant ID
     * @return A Flux of folders matching the search criteria
     */
    @Query("SELECT * FROM folders WHERE tenant_id = :tenantId AND " +
           "(LOWER(name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Flux<Folder> searchFolders(@Param("searchTerm") String searchTerm, @Param("tenantId") String tenantId);

    /**
     * Find folders by tenant ID.
     *
     * @param tenantId The tenant ID
     * @return A Flux of folders for the specified tenant
     */
    Flux<Folder> findByTenantId(String tenantId);

    /**
     * Find a folder by ID and tenant ID.
     *
     * @param id The folder ID
     * @param tenantId The tenant ID
     * @return A Mono of the folder with the specified ID and tenant ID
     */
    Mono<Folder> findByIdAndTenantId(UUID id, String tenantId);
}
