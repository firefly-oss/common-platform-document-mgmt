package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.interfaces.enums.DocumentStatus;
import com.catalis.commons.ecm.interfaces.enums.DocumentType;
import com.catalis.commons.ecm.models.Document;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Repository for managing Document entities in the Enterprise Content Management system.
 */
@Repository
public interface DocumentRepository extends R2dbcRepository<Document, UUID> {

    /**
     * Find documents by folder ID.
     *
     * @param folderId The folder ID
     * @param tenantId The tenant ID
     * @return A Flux of documents in the specified folder
     */
    Flux<Document> findByFolderIdAndTenantId(UUID folderId, String tenantId);

    /**
     * Find documents by document type.
     *
     * @param documentType The document type
     * @param tenantId The tenant ID
     * @return A Flux of documents of the specified type
     */
    Flux<Document> findByDocumentTypeAndTenantId(DocumentType documentType, String tenantId);

    /**
     * Find documents by document status.
     *
     * @param documentStatus The document status
     * @param tenantId The tenant ID
     * @return A Flux of documents with the specified status
     */
    Flux<Document> findByDocumentStatusAndTenantId(DocumentStatus documentStatus, String tenantId);

    /**
     * Find documents by name containing the specified text.
     *
     * @param name The text to search for in document names
     * @param tenantId The tenant ID
     * @return A Flux of documents with names containing the specified text
     */
    Flux<Document> findByNameContainingIgnoreCaseAndTenantId(String name, String tenantId);

    /**
     * Find documents created by a specific user.
     *
     * @param createdBy The user who created the documents
     * @param tenantId The tenant ID
     * @return A Flux of documents created by the specified user
     */
    Flux<Document> findByCreatedByAndTenantId(String createdBy, String tenantId);

    /**
     * Find documents that are locked.
     *
     * @param tenantId The tenant ID
     * @return A Flux of locked documents
     */
    Flux<Document> findByIsLockedTrueAndTenantId(String tenantId);

    /**
     * Find documents that are locked by a specific user.
     *
     * @param lockedBy The user who locked the documents
     * @param tenantId The tenant ID
     * @return A Flux of documents locked by the specified user
     */
    Flux<Document> findByIsLockedTrueAndLockedByAndTenantId(String lockedBy, String tenantId);

    /**
     * Find documents that have expired.
     *
     * @param now The current date and time
     * @param tenantId The tenant ID
     * @return A Flux of expired documents
     */
    Flux<Document> findByExpirationDateLessThanAndTenantId(LocalDateTime now, String tenantId);

    /**
     * Find documents by file extension.
     *
     * @param fileExtension The file extension
     * @param tenantId The tenant ID
     * @return A Flux of documents with the specified file extension
     */
    Flux<Document> findByFileExtensionIgnoreCaseAndTenantId(String fileExtension, String tenantId);

    /**
     * Search for documents by name, description, or file name.
     *
     * @param searchTerm The search term
     * @param tenantId The tenant ID
     * @return A Flux of documents matching the search criteria
     */
    @Query("SELECT * FROM documents WHERE tenant_id = :tenantId AND " +
           "(LOWER(name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(file_name) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Flux<Document> searchDocuments(@Param("searchTerm") String searchTerm, @Param("tenantId") String tenantId);

    /**
     * Find documents by tenant ID.
     *
     * @param tenantId The tenant ID
     * @return A Flux of documents for the specified tenant
     */
    Flux<Document> findByTenantId(String tenantId);

    /**
     * Find a document by ID and tenant ID.
     *
     * @param id The document ID
     * @param tenantId The tenant ID
     * @return A Mono of the document with the specified ID and tenant ID
     */
    Mono<Document> findByIdAndTenantId(UUID id, String tenantId);
}
