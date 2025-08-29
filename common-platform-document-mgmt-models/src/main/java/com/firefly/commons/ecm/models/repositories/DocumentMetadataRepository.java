package com.firefly.commons.ecm.models.repositories;

import com.firefly.commons.ecm.models.entities.DocumentMetadata;
import org.springframework.stereotype.Repository;


/**
 * Repository for managing DocumentMetadata entities in the Enterprise Content Management system.
 */
@Repository
public interface DocumentMetadataRepository extends BaseRepository<DocumentMetadata, Long> {

}
