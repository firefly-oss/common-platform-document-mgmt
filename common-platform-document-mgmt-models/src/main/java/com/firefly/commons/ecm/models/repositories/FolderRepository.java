package com.firefly.commons.ecm.models.repositories;

import com.firefly.commons.ecm.models.entities.Folder;
import org.springframework.stereotype.Repository;


/**
 * Repository for managing Folder entities in the Enterprise Content Management system.
 */
@Repository
public interface FolderRepository extends BaseRepository<Folder, Long> {

}
