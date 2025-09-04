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
