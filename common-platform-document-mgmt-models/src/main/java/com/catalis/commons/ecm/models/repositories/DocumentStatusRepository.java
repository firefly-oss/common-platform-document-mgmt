package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.entities.DocumentStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentStatusRepository extends BaseRepository<DocumentStatus, Long> {
}