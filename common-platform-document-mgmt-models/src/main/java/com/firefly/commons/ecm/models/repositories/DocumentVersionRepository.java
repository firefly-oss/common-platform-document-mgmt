package com.firefly.commons.ecm.models.repositories;

import com.firefly.commons.ecm.models.entities.DocumentVersion;
import org.springframework.stereotype.Repository;


/**
 * Repository for managing DocumentVersion entities in the Enterprise Content Management system.
 */
@Repository
public interface DocumentVersionRepository extends BaseRepository<DocumentVersion, Long> {

}
