package com.firefly.commons.ecm.models.repositories;

import com.firefly.commons.ecm.models.entities.DocumentVersion;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;


/**
 * Repository for managing DocumentVersion entities in the Enterprise Content Management system.
 */
@Repository
public interface DocumentVersionRepository extends BaseRepository<DocumentVersion, UUID> {

    /**
     * Find all document versions by document ID.
     *
     * @param documentId The document ID
     * @return A Flux emitting all versions for the specified document
     */
    Flux<DocumentVersion> findByDocumentId(UUID documentId);
}
