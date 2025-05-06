package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.entities.Tag;
import org.springframework.stereotype.Repository;


/**
 * Repository for managing Tag entities in the Enterprise Content Management system.
 */
@Repository
public interface TagRepository extends BaseRepository<Tag, Long> {

}
