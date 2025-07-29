package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.entities.Tag;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends BaseRepository<Tag, Long> {
}