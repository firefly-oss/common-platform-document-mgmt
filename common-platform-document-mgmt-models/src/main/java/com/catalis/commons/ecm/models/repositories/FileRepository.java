package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.entities.File;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends BaseRepository<File, Long> {
}