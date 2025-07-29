package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.entities.SignatureRequestStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface SignatureRequestStatusRepository extends BaseRepository<SignatureRequestStatus, Long> {
}