package com.catalis.commons.ecm.interfaces.enums;

/**
 * Enum representing the various statuses a document can have in the Enterprise Content Management system.
 */
public enum DocumentStatus {
    /**
     * Document is in draft state, not yet finalized
     */
    DRAFT,
    
    /**
     * Document is under review
     */
    UNDER_REVIEW,
    
    /**
     * Document has been approved
     */
    APPROVED,
    
    /**
     * Document has been rejected
     */
    REJECTED,
    
    /**
     * Document is published and available
     */
    PUBLISHED,
    
    /**
     * Document is archived (no longer active but preserved)
     */
    ARCHIVED,
    
    /**
     * Document is marked for deletion
     */
    MARKED_FOR_DELETION,
    
    /**
     * Document is deleted (soft delete)
     */
    DELETED,
    
    /**
     * Document is locked for editing
     */
    LOCKED,
    
    /**
     * Document is expired
     */
    EXPIRED
}
