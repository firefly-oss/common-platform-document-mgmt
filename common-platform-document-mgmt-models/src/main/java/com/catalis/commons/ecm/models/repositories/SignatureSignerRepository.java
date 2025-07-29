package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.entities.SignatureSigner;
import org.springframework.stereotype.Repository;

@Repository
public interface SignatureSignerRepository extends BaseRepository<SignatureSigner, Long> {
}