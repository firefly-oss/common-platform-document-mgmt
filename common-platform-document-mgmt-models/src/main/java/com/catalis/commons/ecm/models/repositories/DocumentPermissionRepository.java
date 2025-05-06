package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.interfaces.enums.PermissionType;
import com.catalis.commons.ecm.models.DocumentPermission;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Repository for managing DocumentPermission entities in the Enterprise Content Management system.
 */
@Repository
public interface DocumentPermissionRepository extends R2dbcRepository<DocumentPermission, UUID> {

    /**
     * Find document permissions by document ID.
     *
     * @param documentId The document ID
     * @param tenantId The tenant ID
     * @return A Flux of document permissions for the specified document
     */
    Flux<DocumentPermission> findByDocumentIdAndTenantId(UUID documentId, String tenantId);

    /**
     * Find document permissions by user ID.
     *
     * @param userId The user ID
     * @param tenantId The tenant ID
     * @return A Flux of document permissions for the specified user
     */
    Flux<DocumentPermission> findByUserIdAndTenantId(String userId, String tenantId);

    /**
     * Find document permissions by group ID.
     *
     * @param groupId The group ID
     * @param tenantId The tenant ID
     * @return A Flux of document permissions for the specified group
     */
    Flux<DocumentPermission> findByGroupIdAndTenantId(String groupId, String tenantId);

    /**
     * Find document permissions by permission type.
     *
     * @param permissionType The permission type
     * @param tenantId The tenant ID
     * @return A Flux of document permissions with the specified permission type
     */
    Flux<DocumentPermission> findByPermissionTypeAndTenantId(PermissionType permissionType, String tenantId);

    /**
     * Find document permissions by document ID and user ID.
     *
     * @param documentId The document ID
     * @param userId The user ID
     * @param tenantId The tenant ID
     * @return A Flux of document permissions for the specified document and user
     */
    Flux<DocumentPermission> findByDocumentIdAndUserIdAndTenantId(UUID documentId, String userId, String tenantId);

    /**
     * Find document permissions by document ID and group ID.
     *
     * @param documentId The document ID
     * @param groupId The group ID
     * @param tenantId The tenant ID
     * @return A Flux of document permissions for the specified document and group
     */
    Flux<DocumentPermission> findByDocumentIdAndGroupIdAndTenantId(UUID documentId, String groupId, String tenantId);

    /**
     * Find document permissions by document ID, user ID, and permission type.
     *
     * @param documentId The document ID
     * @param userId The user ID
     * @param permissionType The permission type
     * @param tenantId The tenant ID
     * @return A Mono of the document permission with the specified document ID, user ID, and permission type
     */
    Mono<DocumentPermission> findByDocumentIdAndUserIdAndPermissionTypeAndTenantId(UUID documentId, String userId, PermissionType permissionType, String tenantId);

    /**
     * Find document permissions by document ID, group ID, and permission type.
     *
     * @param documentId The document ID
     * @param groupId The group ID
     * @param permissionType The permission type
     * @param tenantId The tenant ID
     * @return A Mono of the document permission with the specified document ID, group ID, and permission type
     */
    Mono<DocumentPermission> findByDocumentIdAndGroupIdAndPermissionTypeAndTenantId(UUID documentId, String groupId, PermissionType permissionType, String tenantId);

    /**
     * Find expired document permissions.
     *
     * @param now The current date and time
     * @param tenantId The tenant ID
     * @return A Flux of expired document permissions
     */
    Flux<DocumentPermission> findByExpirationDateLessThanAndTenantId(LocalDateTime now, String tenantId);

    /**
     * Delete document permissions by document ID.
     *
     * @param documentId The document ID
     * @param tenantId The tenant ID
     * @return A Mono of the number of deleted document permissions
     */
    Mono<Long> deleteByDocumentIdAndTenantId(UUID documentId, String tenantId);

    /**
     * Find document permissions by tenant ID.
     *
     * @param tenantId The tenant ID
     * @return A Flux of document permissions for the specified tenant
     */
    Flux<DocumentPermission> findByTenantId(String tenantId);

    /**
     * Find a document permission by ID and tenant ID.
     *
     * @param id The document permission ID
     * @param tenantId The tenant ID
     * @return A Mono of the document permission with the specified ID and tenant ID
     */
    Mono<DocumentPermission> findByIdAndTenantId(UUID id, String tenantId);

    /**
     * Check if a user has a specific permission for a document.
     *
     * @param documentId The document ID
     * @param userId The user ID
     * @param permissionType The permission type
     * @param tenantId The tenant ID
     * @return A Mono of Boolean indicating whether the user has the permission
     */
    @Query("SELECT EXISTS(SELECT 1 FROM document_permissions WHERE document_id = :documentId AND user_id = :userId " +
           "AND permission_type = :permissionType AND is_granted = true AND tenant_id = :tenantId " +
           "AND (expiration_date IS NULL OR expiration_date > NOW()))")
    Mono<Boolean> hasUserPermission(UUID documentId, String userId, PermissionType permissionType, String tenantId);

    /**
     * Check if a group has a specific permission for a document.
     *
     * @param documentId The document ID
     * @param groupId The group ID
     * @param permissionType The permission type
     * @param tenantId The tenant ID
     * @return A Mono of Boolean indicating whether the group has the permission
     */
    @Query("SELECT EXISTS(SELECT 1 FROM document_permissions WHERE document_id = :documentId AND group_id = :groupId " +
           "AND permission_type = :permissionType AND is_granted = true AND tenant_id = :tenantId " +
           "AND (expiration_date IS NULL OR expiration_date > NOW()))")
    Mono<Boolean> hasGroupPermission(UUID documentId, String groupId, PermissionType permissionType, String tenantId);
}
