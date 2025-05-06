package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.Tag;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for managing Tag entities in the Enterprise Content Management system.
 */
@Repository
public interface TagRepository extends R2dbcRepository<Tag, UUID> {

    /**
     * Find tags by name.
     *
     * @param name The tag name
     * @param tenantId The tenant ID
     * @return A Flux of tags with the specified name
     */
    Flux<Tag> findByNameAndTenantId(String name, String tenantId);

    /**
     * Find tags by name containing the specified text.
     *
     * @param name The text to search for in tag names
     * @param tenantId The tenant ID
     * @return A Flux of tags with names containing the specified text
     */
    Flux<Tag> findByNameContainingIgnoreCaseAndTenantId(String name, String tenantId);

    /**
     * Find system tags.
     *
     * @param tenantId The tenant ID
     * @return A Flux of system tags
     */
    Flux<Tag> findByIsSystemTagTrueAndTenantId(String tenantId);

    /**
     * Find user-defined tags.
     *
     * @param tenantId The tenant ID
     * @return A Flux of user-defined tags
     */
    Flux<Tag> findByIsSystemTagFalseAndTenantId(String tenantId);

    /**
     * Search for tags by name or description.
     *
     * @param searchTerm The search term
     * @param tenantId The tenant ID
     * @return A Flux of tags matching the search criteria
     */
    @Query("SELECT * FROM tags WHERE tenant_id = :tenantId AND " +
           "(LOWER(name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Flux<Tag> searchTags(@Param("searchTerm") String searchTerm, @Param("tenantId") String tenantId);

    /**
     * Find tags by tenant ID.
     *
     * @param tenantId The tenant ID
     * @return A Flux of tags for the specified tenant
     */
    Flux<Tag> findByTenantId(String tenantId);

    /**
     * Find a tag by ID and tenant ID.
     *
     * @param id The tag ID
     * @param tenantId The tenant ID
     * @return A Mono of the tag with the specified ID and tenant ID
     */
    Mono<Tag> findByIdAndTenantId(UUID id, String tenantId);
}
