package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.entities.SignatureProof;
import org.springframework.stereotype.Repository;

@Repository
public interface SignatureProofRepository extends BaseRepository<SignatureProof, Long> {
}