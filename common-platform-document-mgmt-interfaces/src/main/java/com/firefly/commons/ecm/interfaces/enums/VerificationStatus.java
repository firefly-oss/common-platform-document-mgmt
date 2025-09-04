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
