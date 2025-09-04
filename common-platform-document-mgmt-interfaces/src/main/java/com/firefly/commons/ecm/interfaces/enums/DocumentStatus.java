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
