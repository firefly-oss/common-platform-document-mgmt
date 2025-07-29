-- V1__Create_Tables.sql
-- Create all tables for the document management system

-- ============================================================
-- LOOKUP TABLES
-- These tables replace hard-coded enums to provide flexibility
-- and allow dynamic configuration of system statuses and types
-- ============================================================

-- Document status lookup table
CREATE TABLE IF NOT EXISTS document_status (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL
);

-- Signature request status lookup table
CREATE TABLE IF NOT EXISTS signature_request_status (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL
);

-- Signature status lookup table
CREATE TABLE IF NOT EXISTS signature_status (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL
);

-- Proof type lookup table
CREATE TABLE IF NOT EXISTS proof_type (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL
);

-- Notification type lookup table
CREATE TABLE IF NOT EXISTS notification_type (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL
);

-- Document type lookup table
CREATE TABLE IF NOT EXISTS document_type (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL
);

-- Reference type lookup table
CREATE TABLE IF NOT EXISTS reference_type (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL
);

-- ============================================================
-- CORE ENTITIES
-- These tables represent the main business objects in the system
-- ============================================================

-- Document table
CREATE TABLE IF NOT EXISTS document (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    creation_date TIMESTAMP NOT NULL,
    owner_department VARCHAR(100),
    status_id BIGINT NOT NULL REFERENCES document_status(id),
    document_type_id BIGINT NOT NULL REFERENCES document_type(id),
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NOT NULL
);

-- File table
CREATE TABLE IF NOT EXISTS file (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL REFERENCES document(id),
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(100) NOT NULL,
    blob_storage_url TEXT NOT NULL,
    upload_date TIMESTAMP NOT NULL,
    uploaded_by VARCHAR(100) NOT NULL,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- VERSIONING SYSTEM
-- Maintains immutable copies of document versions for audit trail
-- and allows rollback to previous versions
-- ============================================================

-- Document version table
CREATE TABLE IF NOT EXISTS document_version (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL REFERENCES document(id),
    version_number BIGINT NOT NULL,
    change_summary TEXT,
    file_name VARCHAR(255) NOT NULL,
    blob_storage_url TEXT NOT NULL,
    effective_date TIMESTAMP NOT NULL,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NOT NULL,
    CONSTRAINT unique_document_version UNIQUE (document_id, version_number)
);

-- ============================================================
-- TAGGING SYSTEM
-- Flexible many-to-many tagging for categorization and filtering
-- ============================================================

-- Tag table
CREATE TABLE IF NOT EXISTS tag (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    color VARCHAR(50),
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NOT NULL
);

-- Document tag association table
CREATE TABLE IF NOT EXISTS document_tag (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL REFERENCES document(id),
    tag_id BIGINT NOT NULL REFERENCES tag(id),
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_document_tag UNIQUE (document_id, tag_id)
);

-- ============================================================
-- DOCUMENT RELATIONSHIPS
-- Manages hierarchical and associative relationships between documents
-- ============================================================

-- Document reference table
CREATE TABLE IF NOT EXISTS document_reference (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL REFERENCES document(id),
    related_document_id BIGINT NOT NULL REFERENCES document(id),
    reference_type_id BIGINT NOT NULL REFERENCES reference_type(id),
    note TEXT,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NOT NULL,
    CONSTRAINT unique_document_reference UNIQUE (document_id, related_document_id, reference_type_id)
);

-- ============================================================
-- ELECTRONIC SIGNATURE WORKFLOW
-- Manages the complete e-signature process from request to completion
-- ============================================================

-- Signature request table
CREATE TABLE IF NOT EXISTS signature_request (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL REFERENCES document(id),
    request_date TIMESTAMP NOT NULL,
    status_id BIGINT NOT NULL REFERENCES signature_request_status(id),
    signatory_order BIGINT,
    intervening_parties TEXT,
    external_signature_id VARCHAR(255),
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NOT NULL
);

-- Signature signer table
CREATE TABLE IF NOT EXISTS signature_signer (
    id BIGSERIAL PRIMARY KEY,
    signature_request_id BIGINT NOT NULL REFERENCES signature_request(id),
    signer_email VARCHAR(255) NOT NULL,
    signer_name VARCHAR(255) NOT NULL,
    sign_order BIGINT,
    status_id BIGINT NOT NULL REFERENCES signature_status(id),
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NOT NULL
);

-- Signature proof table
CREATE TABLE IF NOT EXISTS signature_proof (
    id BIGSERIAL PRIMARY KEY,
    signature_request_id BIGINT NOT NULL REFERENCES signature_request(id),
    proof_url TEXT NOT NULL,
    proof_type_id BIGINT NOT NULL REFERENCES proof_type(id),
    proof_date TIMESTAMP NOT NULL,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NOT NULL
);

-- ============================================================
-- NOTIFICATION SYSTEM
-- Tracks all communications sent to users regarding documents and signatures
-- ============================================================

-- Notification log table
CREATE TABLE IF NOT EXISTS notification_log (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT REFERENCES document(id),
    signature_request_id BIGINT REFERENCES signature_request(id),
    notification_type_id BIGINT NOT NULL REFERENCES notification_type(id),
    recipient VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    sent_at TIMESTAMP NOT NULL,
    success BOOLEAN NOT NULL,
    external_id VARCHAR(255),
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK (document_id IS NOT NULL OR signature_request_id IS NOT NULL)
);

-- Create indexes for better query performance
CREATE INDEX idx_document_status ON document(status_id);
CREATE INDEX idx_document_type ON document(document_type_id);
CREATE INDEX idx_file_document ON file(document_id);
CREATE INDEX idx_document_version_document ON document_version(document_id);
CREATE INDEX idx_document_tag_document ON document_tag(document_id);
CREATE INDEX idx_document_tag_tag ON document_tag(tag_id);
CREATE INDEX idx_document_reference_document ON document_reference(document_id);
CREATE INDEX idx_document_reference_related ON document_reference(related_document_id);
CREATE INDEX idx_signature_request_document ON signature_request(document_id);
CREATE INDEX idx_signature_request_status ON signature_request(status_id);
CREATE INDEX idx_signature_signer_request ON signature_signer(signature_request_id);
CREATE INDEX idx_signature_signer_status ON signature_signer(status_id);
CREATE INDEX idx_signature_proof_request ON signature_proof(signature_request_id);
CREATE INDEX idx_notification_log_document ON notification_log(document_id);
CREATE INDEX idx_notification_log_request ON notification_log(signature_request_id);
CREATE INDEX idx_notification_log_type ON notification_log(notification_type_id);