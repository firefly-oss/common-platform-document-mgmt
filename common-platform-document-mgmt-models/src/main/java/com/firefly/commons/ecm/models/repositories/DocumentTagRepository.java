package com.firefly.commons.ecm.models.repositories;

import com.firefly.commons.ecm.models.entities.DocumentTag;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Repository for managing DocumentTag entities in the Enterprise Content Management system.
 */
@Repository
public interface DocumentTagRepository extends BaseRepository<DocumentTag, UUID> {

}
