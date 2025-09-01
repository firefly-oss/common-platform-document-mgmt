package com.firefly.commons.ecm.models.repositories;

import com.firefly.commons.ecm.models.entities.Folder;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Repository for managing Folder entities in the Enterprise Content Management system.
 */
@Repository
public interface FolderRepository extends BaseRepository<Folder, UUID> {

}
