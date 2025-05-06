package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.entities.Folder;
import org.springframework.stereotype.Repository;


/**
 * Repository for managing Folder entities in the Enterprise Content Management system.
 */
@Repository
public interface FolderRepository extends BaseRepository<Folder, Long> {

}
