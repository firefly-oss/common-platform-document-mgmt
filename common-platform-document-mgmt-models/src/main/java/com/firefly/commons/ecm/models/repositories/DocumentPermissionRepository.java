package com.firefly.commons.ecm.models.repositories;

import com.firefly.commons.ecm.models.entities.DocumentPermission;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository for managing DocumentPermission entities in the Enterprise Content Management system.
 */
@Repository
public interface DocumentPermissionRepository extends BaseRepository<DocumentPermission, UUID> {

}
