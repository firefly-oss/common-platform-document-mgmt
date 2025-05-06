package com.catalis.commons.ecm.interfaces.enums;

/**
 * Enum representing different security levels for documents in the Enterprise Content Management system.
 */
public enum SecurityLevel {
    /**
     * Public documents accessible to all users
     */
    PUBLIC,
    
    /**
     * Internal documents accessible to all authenticated users
     */
    INTERNAL,
    
    /**
     * Confidential documents with restricted access
     */
    CONFIDENTIAL,
    
    /**
     * Restricted documents with very limited access
     */
    RESTRICTED,
    
    /**
     * Secret documents with highly restricted access
     */
    SECRET,
    
    /**
     * Top secret documents with extremely restricted access
     */
    TOP_SECRET
}
