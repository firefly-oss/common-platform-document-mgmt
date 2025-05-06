package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.DocumentMetadata;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for managing DocumentMetadata entities in the Enterprise Content Management system.
 */
@Repository
public interface DocumentMetadataRepository extends R2dbcRepository<DocumentMetadata, UUID> {

    /**
     * Find all metadata for a document.
     *
     * @param documentId The document ID
     * @param tenantId The tenant ID
     * @return A Flux of document metadata for the specified document
     */
    Flux<DocumentMetadata> findByDocumentIdAndTenantId(UUID documentId, String tenantId);

    /**
     * Find all searchable metadata for a document.
     *
     * @param documentId The document ID
     * @param tenantId The tenant ID
     * @return A Flux of searchable document metadata
     */
    Flux<DocumentMetadata> findByDocumentIdAndIsSearchableTrueAndTenantId(UUID documentId, String tenantId);

    /**
     * Find all system metadata for a document.
     *
     * @param documentId The document ID
     * @param tenantId The tenant ID
     * @return A Flux of system document metadata
     */
    Flux<DocumentMetadata> findByDocumentIdAndIsSystemMetadataTrueAndTenantId(UUID documentId, String tenantId);

    /**
     * Find metadata by key for a document.
     *
     * @param documentId The document ID
     * @param key The metadata key
     * @param tenantId The tenant ID
     * @return A Mono of the document metadata with the specified key
     */
    Mono<DocumentMetadata> findByDocumentIdAndKeyAndTenantId(UUID documentId, String key, String tenantId);

    /**
     * Find documents by metadata key and value.
     *
     * @param key The metadata key
     * @param value The metadata value
     * @param tenantId The tenant ID
     * @return A Flux of document metadata with the specified key and value
     */
    Flux<DocumentMetadata> findByKeyAndValueAndTenantId(String key, String value, String tenantId);

    /**
     * Search for documents by metadata value.
     *
     * @param searchTerm The search term
     * @param tenantId The tenant ID
     * @return A Flux of document metadata matching the search criteria
     */
    @Query("SELECT * FROM document_metadata WHERE tenant_id = :tenantId AND is_searchable = true AND " +
           "LOWER(metadata_value) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Flux<DocumentMetadata> searchByMetadataValue(@Param("searchTerm") String searchTerm, @Param("tenantId") String tenantId);

    /**
     * Delete all metadata for a document.
     *
     * @param documentId The document ID
     * @param tenantId The tenant ID
     * @return A Mono of the number of deleted metadata entries
     */
    Mono<Long> deleteByDocumentIdAndTenantId(UUID documentId, String tenantId);

    /**
     * Find document metadata by tenant ID.
     *
     * @param tenantId The tenant ID
     * @return A Flux of document metadata for the specified tenant
     */
    Flux<DocumentMetadata> findByTenantId(String tenantId);

    /**
     * Find document metadata by ID and tenant ID.
     *
     * @param id The document metadata ID
     * @param tenantId The tenant ID
     * @return A Mono of the document metadata with the specified ID and tenant ID
     */
    Mono<DocumentMetadata> findByIdAndTenantId(UUID id, String tenantId);
}
