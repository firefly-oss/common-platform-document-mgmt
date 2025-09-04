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


package com.firefly.commons.ecm.interfaces.enums;

/**
 * Enum representing different statuses of a document signature in the Enterprise Content Management system.
 */
public enum SignatureStatus {
    /**
     * Signature is pending (not yet signed)
     */
    PENDING,
    
    /**
     * Document is in the process of being signed
     */
    IN_PROGRESS,
    
    /**
     * Document has been successfully signed
     */
    SIGNED,
    
    /**
     * Signing request has been rejected by the signer
     */
    REJECTED,
    
    /**
     * Signing request has expired
     */
    EXPIRED,
    
    /**
     * Signature has been revoked
     */
    REVOKED,
    
    /**
     * Signature has failed due to technical issues
     */
    FAILED,
    
    /**
     * Signature has been canceled
     */
    CANCELED
}
