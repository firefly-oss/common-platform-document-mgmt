package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.entities.Document;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing Document entities in the Enterprise Content Management system.
 */
@Repository
public interface DocumentRepository extends BaseRepository<Document, Long> {

}
