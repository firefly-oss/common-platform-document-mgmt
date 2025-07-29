package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.entities.SignatureStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface SignatureStatusRepository extends BaseRepository<SignatureStatus, Long> {
}