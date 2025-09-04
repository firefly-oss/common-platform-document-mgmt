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
 * Enum representing different types of documents in the Enterprise Content Management system.
 */
public enum DocumentType {
    /**
     * Regular document (e.g., text files, spreadsheets, presentations)
     */
    DOCUMENT,
    
    /**
     * Image file (e.g., JPG, PNG, GIF)
     */
    IMAGE,
    
    /**
     * PDF document
     */
    PDF,
    
    /**
     * Contract document
     */
    CONTRACT,
    
    /**
     * Invoice document
     */
    INVOICE,
    
    /**
     * Receipt document
     */
    RECEIPT,
    
    /**
     * Form document
     */
    FORM,
    
    /**
     * Report document
     */
    REPORT,
    
    /**
     * Email document
     */
    EMAIL,
    
    /**
     * Audio file
     */
    AUDIO,
    
    /**
     * Video file
     */
    VIDEO,
    
    /**
     * Archive file (e.g., ZIP, RAR)
     */
    ARCHIVE,
    
    /**
     * Other document type not covered by the above categories
     */
    OTHER
}
