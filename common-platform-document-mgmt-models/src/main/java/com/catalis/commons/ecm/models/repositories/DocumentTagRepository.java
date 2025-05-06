package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.entities.DocumentTag;
import org.springframework.stereotype.Repository;


/**
 * Repository for managing DocumentTag entities in the Enterprise Content Management system.
 */
@Repository
public interface DocumentTagRepository extends BaseRepository<DocumentTag, Long> {

}
