package com.firefly.commons.ecm.interfaces.enums;

/**
 * Enum representing different formats of electronic signatures in the Enterprise Content Management system.
 */
public enum SignatureFormat {
    /**
     * PDF Advanced Electronic Signatures (for PDF documents)
     */
    PADES,
    
    /**
     * XML Advanced Electronic Signatures (for XML documents)
     */
    XADES,
    
    /**
     * CMS Advanced Electronic Signatures (for any type of document)
     */
    CADES,
    
    /**
     * JSON Advanced Electronic Signatures (for JSON documents)
     */
    JADES,
    
    /**
     * Signature in PDF visible format
     */
    PDF_VISIBLE,
    
    /**
     * Signature in PDF invisible format
     */
    PDF_INVISIBLE,
    
    /**
     * Timestamp signature
     */
    TIMESTAMP
}
