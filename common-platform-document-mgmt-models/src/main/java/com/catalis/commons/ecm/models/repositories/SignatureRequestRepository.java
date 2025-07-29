package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.entities.SignatureRequest;
import org.springframework.stereotype.Repository;

@Repository
public interface SignatureRequestRepository extends BaseRepository<SignatureRequest, Long> {
}