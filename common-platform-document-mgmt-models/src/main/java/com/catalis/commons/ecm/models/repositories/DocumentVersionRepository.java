package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.DocumentVersion;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for managing DocumentVersion entities in the Enterprise Content Management system.
 */
@Repository
public interface DocumentVersionRepository extends R2dbcRepository<DocumentVersion, UUID> {

    /**
     * Find all versions of a document.
     *
     * @param documentId The document ID
     * @param tenantId The tenant ID
     * @return A Flux of document versions for the specified document
     */
    Flux<DocumentVersion> findByDocumentIdAndTenantIdOrderByVersionNumberDesc(UUID documentId, String tenantId);

    /**
     * Find the latest version of a document.
     *
     * @param documentId The document ID
     * @param tenantId The tenant ID
     * @return A Mono of the latest document version
     */
    @Query("SELECT * FROM document_versions WHERE document_id = :documentId AND tenant_id = :tenantId ORDER BY version_number DESC LIMIT 1")
    Mono<DocumentVersion> findLatestVersionByDocumentIdAndTenantId(UUID documentId, String tenantId);

    /**
     * Find a specific version of a document.
     *
     * @param documentId The document ID
     * @param versionNumber The version number
     * @param tenantId The tenant ID
     * @return A Mono of the specified document version
     */
    Mono<DocumentVersion> findByDocumentIdAndVersionNumberAndTenantId(UUID documentId, Integer versionNumber, String tenantId);

    /**
     * Find all major versions of a document.
     *
     * @param documentId The document ID
     * @param tenantId The tenant ID
     * @return A Flux of major document versions
     */
    Flux<DocumentVersion> findByDocumentIdAndIsMajorVersionTrueAndTenantIdOrderByVersionNumberDesc(UUID documentId, String tenantId);

    /**
     * Count the number of versions for a document.
     *
     * @param documentId The document ID
     * @param tenantId The tenant ID
     * @return A Mono of the count of versions
     */
    Mono<Long> countByDocumentIdAndTenantId(UUID documentId, String tenantId);

    /**
     * Find document versions by tenant ID.
     *
     * @param tenantId The tenant ID
     * @return A Flux of document versions for the specified tenant
     */
    Flux<DocumentVersion> findByTenantId(String tenantId);

    /**
     * Find a document version by ID and tenant ID.
     *
     * @param id The document version ID
     * @param tenantId The tenant ID
     * @return A Mono of the document version with the specified ID and tenant ID
     */
    Mono<DocumentVersion> findByIdAndTenantId(UUID id, String tenantId);
}
