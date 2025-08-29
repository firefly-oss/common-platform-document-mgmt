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
