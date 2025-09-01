package com.firefly.commons.ecm.models.repositories;

import com.firefly.commons.ecm.models.entities.Tag;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Repository for managing Tag entities in the Enterprise Content Management system.
 */
@Repository
public interface TagRepository extends BaseRepository<Tag, UUID> {

}
