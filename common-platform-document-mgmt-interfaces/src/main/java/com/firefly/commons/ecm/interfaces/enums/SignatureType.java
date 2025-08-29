package com.firefly.commons.ecm.interfaces.enums;

/**
 * Enum representing different types of electronic signatures in the Enterprise Content Management system.
 */
public enum SignatureType {
    /**
     * Basic electronic signature
     */
    BASIC,
    
    /**
     * Advanced electronic signature
     */
    ADVANCED,
    
    /**
     * Advanced electronic signature with qualified certificate
     */
    ADVANCED_WITH_QCERT,
    
    /**
     * Qualified electronic signature (highest level, legally equivalent to handwritten)
     */
    QUALIFIED,
    
    /**
     * Digital signature using certificates
     */
    DIGITAL,
    
    /**
     * Signature based on biometric data
     */
    BIOMETRIC
}
