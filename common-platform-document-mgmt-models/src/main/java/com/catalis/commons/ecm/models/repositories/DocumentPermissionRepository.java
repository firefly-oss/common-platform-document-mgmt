package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.entities.DocumentPermission;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing DocumentPermission entities in the Enterprise Content Management system.
 */
@Repository
public interface DocumentPermissionRepository extends BaseRepository<DocumentPermission, Long> {

}
