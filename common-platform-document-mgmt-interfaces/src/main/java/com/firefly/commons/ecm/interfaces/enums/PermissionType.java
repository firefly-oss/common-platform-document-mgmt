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
 * Enum representing different permission types for documents in the Enterprise Content Management system.
 * Aligned with lib-ecm PermissionType enum.
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
     * Permission to execute a document
     */
    EXECUTE,
    
    /**
     * Permission to create new documents
     */
    CREATE,
    
    /**
     * Permission to move a document
     */
    MOVE,
    
    /**
     * Permission to copy a document
     */
    COPY,
    
    /**
     * Permission to view document metadata
     */
    VIEW_METADATA,
    
    /**
     * Permission to modify document metadata
     */
    MODIFY_METADATA,
    
    /**
     * Permission to view document versions
     */
    VIEW_VERSIONS,
    
    /**
     * Permission to create new document versions
     */
    CREATE_VERSION,
    
    /**
     * Permission to view audit information
     */
    VIEW_AUDIT,
    
    /**
     * Permission to manage document permissions
     */
    MANAGE_PERMISSIONS,
    
    /**
     * Permission to checkout a document
     */
    CHECKOUT,
    
    /**
     * Permission to checkin a document
     */
    CHECKIN,
    
    /**
     * Permission to sign a document
     */
    SIGN,
    
    /**
     * Permission to send document for signature
     */
    SEND_FOR_SIGNATURE,
    
    /**
     * Administrative permissions (full control)
     */
    ADMIN
}
