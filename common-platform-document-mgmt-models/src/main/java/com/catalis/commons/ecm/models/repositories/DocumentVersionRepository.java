package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.entities.DocumentVersion;
import org.springframework.stereotype.Repository;


/**
 * Repository for managing DocumentVersion entities in the Enterprise Content Management system.
 */
@Repository
public interface DocumentVersionRepository extends BaseRepository<DocumentVersion, Long> {

}
