package com.firefly.commons.ecm.interfaces.enums;

/**
 * Enum representing different statuses of signature verification in the Enterprise Content Management system.
 */
public enum VerificationStatus {
    /**
     * Signature is valid
     */
    VALID,
    
    /**
     * Signature is invalid
     */
    INVALID,
    
    /**
     * Signature verification is indeterminate (cannot be determined)
     */
    INDETERMINATE,
    
    /**
     * Signature verification is in progress
     */
    IN_PROGRESS,
    
    /**
     * Signature verification has failed due to technical issues
     */
    FAILED,
    
    /**
     * Signature verification has not been performed yet
     */
    NOT_VERIFIED
}
