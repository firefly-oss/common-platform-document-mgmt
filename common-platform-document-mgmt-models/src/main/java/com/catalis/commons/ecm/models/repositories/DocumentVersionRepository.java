package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.entities.DocumentVersion;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentVersionRepository extends BaseRepository<DocumentVersion, Long> {
}