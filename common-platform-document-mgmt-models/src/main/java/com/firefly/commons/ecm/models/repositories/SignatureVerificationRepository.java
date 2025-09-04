/*
 * Copyright 2025 Firefly Software Solutions Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.firefly.commons.ecm.models.repositories;

import com.firefly.commons.ecm.interfaces.enums.VerificationStatus;
import com.firefly.commons.ecm.models.entities.SignatureVerification;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
/**
 * Repository for managing SignatureVerification entities in the Enterprise Content Management system.
 */
@Repository
public interface SignatureVerificationRepository extends BaseRepository<SignatureVerification, UUID> {
    
    /**
     * Find all verifications for a document signature.
     *
     * @param documentSignatureId The document signature ID
     * @return A Flux emitting all verifications for the document signature
     */
    Flux<SignatureVerification> findByDocumentSignatureId(UUID documentSignatureId);
    
    /**
     * Find the latest verification for a document signature.
     *
     * @param documentSignatureId The document signature ID
     * @return A Mono emitting the latest verification for the document signature
     */
    Mono<SignatureVerification> findFirstByDocumentSignatureIdOrderByVerificationTimestampDesc(UUID documentSignatureId);
    
    /**
     * Find all verifications with a specific status.
     *
     * @param verificationStatus The verification status
     * @return A Flux emitting all verifications with the specified status
     */
    Flux<SignatureVerification> findByVerificationStatus(VerificationStatus verificationStatus);
    
    /**
     * Count the number of verifications for a document signature.
     *
     * @param documentSignatureId The document signature ID
     * @return A Mono emitting the count of verifications for the document signature
     */
    Mono<Long> countByDocumentSignatureId(UUID documentSignatureId);
}
