package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.DocumentTag;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for managing DocumentTag entities in the Enterprise Content Management system.
 */
@Repository
public interface DocumentTagRepository extends R2dbcRepository<DocumentTag, UUID> {

    /**
     * Find document tags by document ID.
     *
     * @param documentId The document ID
     * @param tenantId The tenant ID
     * @return A Flux of document tags for the specified document
     */
    Flux<DocumentTag> findByDocumentIdAndTenantId(UUID documentId, String tenantId);

    /**
     * Find document tags by tag ID.
     *
     * @param tagId The tag ID
     * @param tenantId The tenant ID
     * @return A Flux of document tags for the specified tag
     */
    Flux<DocumentTag> findByTagIdAndTenantId(UUID tagId, String tenantId);

    /**
     * Find document tag by document ID and tag ID.
     *
     * @param documentId The document ID
     * @param tagId The tag ID
     * @param tenantId The tenant ID
     * @return A Mono of the document tag with the specified document ID and tag ID
     */
    Mono<DocumentTag> findByDocumentIdAndTagIdAndTenantId(UUID documentId, UUID tagId, String tenantId);

    /**
     * Delete document tags by document ID.
     *
     * @param documentId The document ID
     * @param tenantId The tenant ID
     * @return A Mono of the number of deleted document tags
     */
    Mono<Long> deleteByDocumentIdAndTenantId(UUID documentId, String tenantId);

    /**
     * Delete document tag by document ID and tag ID.
     *
     * @param documentId The document ID
     * @param tagId The tag ID
     * @param tenantId The tenant ID
     * @return A Mono of the number of deleted document tags
     */
    Mono<Long> deleteByDocumentIdAndTagIdAndTenantId(UUID documentId, UUID tagId, String tenantId);

    /**
     * Find document tags by tenant ID.
     *
     * @param tenantId The tenant ID
     * @return A Flux of document tags for the specified tenant
     */
    Flux<DocumentTag> findByTenantId(String tenantId);

    /**
     * Find a document tag by ID and tenant ID.
     *
     * @param id The document tag ID
     * @param tenantId The tenant ID
     * @return A Mono of the document tag with the specified ID and tenant ID
     */
    Mono<DocumentTag> findByIdAndTenantId(UUID id, String tenantId);

    /**
     * Find tag IDs for a document.
     *
     * @param documentId The document ID
     * @param tenantId The tenant ID
     * @return A Flux of tag IDs
     */
    @Query("SELECT tag_id FROM document_tags WHERE document_id = :documentId AND tenant_id = :tenantId")
    Flux<UUID> findTagIdsByDocumentIdAndTenantId(UUID documentId, String tenantId);

    /**
     * Find document IDs for a tag.
     *
     * @param tagId The tag ID
     * @param tenantId The tenant ID
     * @return A Flux of document IDs
     */
    @Query("SELECT document_id FROM document_tags WHERE tag_id = :tagId AND tenant_id = :tenantId")
    Flux<UUID> findDocumentIdsByTagIdAndTenantId(UUID tagId, String tenantId);
}
