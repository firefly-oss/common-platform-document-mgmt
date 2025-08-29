package com.firefly.commons.ecm.interfaces.enums;

/**
 * Enum representing different permission types for documents in the Enterprise Content Management system.
 */
public enum PermissionType {
    /**
     * Permission to read/view a document
     */
    READ,
    
    /**
     * Permission to write/edit a document
     */
    WRITE,
    
    /**
     * Permission to delete a document
     */
    DELETE,
    
    /**
     * Permission to share a document with others
     */
    SHARE,
    
    /**
     * Permission to print a document
     */
    PRINT,
    
    /**
     * Permission to download a document
     */
    DOWNLOAD,
    
    /**
     * Full control over a document (all permissions)
     */
    FULL_CONTROL,
    
    /**
     * Permission to change document permissions
     */
    CHANGE_PERMISSIONS,
    
    /**
     * Permission to view document metadata only
     */
    VIEW_METADATA,
    
    /**
     * Permission to edit document metadata
     */
    EDIT_METADATA
}
