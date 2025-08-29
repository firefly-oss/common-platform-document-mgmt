package com.firefly.commons.ecm.interfaces.enums;

/**
 * Enum representing different storage types for documents in the Enterprise Content Management system.
 */
public enum StorageType {
    /**
     * Document is stored in the local file system
     */
    LOCAL_FILESYSTEM,
    
    /**
     * Document is stored in a database as a BLOB
     */
    DATABASE,
    
    /**
     * Document is stored in Amazon S3
     */
    S3,
    
    /**
     * Document is stored in Azure Blob Storage
     */
    AZURE_BLOB,
    
    /**
     * Document is stored in Google Cloud Storage
     */
    GOOGLE_CLOUD_STORAGE,
    
    /**
     * Document is stored in a content delivery network
     */
    CDN,
    
    /**
     * Document is stored in a distributed file system
     */
    DISTRIBUTED_FS,
    
    /**
     * Document is stored in an external system with reference only
     */
    EXTERNAL_REFERENCE
}
