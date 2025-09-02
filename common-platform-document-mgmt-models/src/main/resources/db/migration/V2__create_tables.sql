-- Create tables for Enterprise Content Management system

-- Enable UUID extension for UUID generation
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Folders Table
CREATE TABLE folders (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    parent_folder_id UUID,
    path VARCHAR(1000),
    security_level security_level NOT NULL DEFAULT 'PUBLIC',
    is_system_folder BOOLEAN DEFAULT FALSE,
    tenant_id VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(255),
    version BIGINT DEFAULT 0,
    CONSTRAINT fk_folder_parent FOREIGN KEY (parent_folder_id) REFERENCES folders(id) ON DELETE SET NULL
);

-- Documents Table
CREATE TABLE documents (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    file_name VARCHAR(255),
    file_extension VARCHAR(50),
    mime_type VARCHAR(100),
    file_size BIGINT,
    document_type document_type NOT NULL DEFAULT 'DOCUMENT',
    document_status document_status NOT NULL DEFAULT 'DRAFT',
    storage_type storage_type NOT NULL DEFAULT 'LOCAL_FILESYSTEM',
    storage_path VARCHAR(1000),
    security_level security_level NOT NULL DEFAULT 'PUBLIC',
    folder_id UUID,
    is_encrypted BOOLEAN DEFAULT FALSE,
    is_indexed BOOLEAN DEFAULT FALSE,
    is_locked BOOLEAN DEFAULT FALSE,
    locked_by VARCHAR(255),
    locked_until TIMESTAMP WITH TIME ZONE,
    expiration_date TIMESTAMP WITH TIME ZONE,
    retention_date TIMESTAMP WITH TIME ZONE,
    tenant_id VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(255),
    version BIGINT DEFAULT 0,
    checksum VARCHAR(255),
    CONSTRAINT fk_document_folder FOREIGN KEY (folder_id) REFERENCES folders(id) ON DELETE SET NULL
);

-- Document Versions Table
CREATE TABLE document_versions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    document_id UUID NOT NULL,
    version_number INTEGER NOT NULL,
    file_name VARCHAR(255),
    file_extension VARCHAR(50),
    mime_type VARCHAR(100),
    file_size BIGINT,
    storage_type storage_type NOT NULL DEFAULT 'LOCAL_FILESYSTEM',
    storage_path VARCHAR(1000),
    is_encrypted BOOLEAN DEFAULT FALSE,
    change_summary TEXT,
    is_major_version BOOLEAN DEFAULT FALSE,
    tenant_id VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    CONSTRAINT fk_document_version_document FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE,
    CONSTRAINT uk_document_version UNIQUE (document_id, version_number)
);

-- Document Metadata Table
CREATE TABLE document_metadata (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    document_id UUID NOT NULL,
    metadata_key VARCHAR(255) NOT NULL,
    metadata_value TEXT,
    metadata_type VARCHAR(100),
    is_searchable BOOLEAN DEFAULT TRUE,
    is_system_metadata BOOLEAN DEFAULT FALSE,
    tenant_id VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(255),
    version BIGINT DEFAULT 0,
    CONSTRAINT fk_document_metadata_document FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE,
    CONSTRAINT uk_document_metadata_key UNIQUE (document_id, metadata_key)
);

-- Document Permissions Table
CREATE TABLE document_permissions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    document_id UUID NOT NULL,
    party_id UUID NOT NULL,
    permission_type permission_type NOT NULL,
    is_granted BOOLEAN DEFAULT TRUE,
    expiration_date TIMESTAMP WITH TIME ZONE,
    tenant_id VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(255),
    version BIGINT DEFAULT 0,
    CONSTRAINT fk_document_permission_document FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE,
    CONSTRAINT uk_document_permission UNIQUE (document_id, party_id, permission_type)
);

-- Tags Table
CREATE TABLE tags (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    color VARCHAR(50),
    is_system_tag BOOLEAN DEFAULT FALSE,
    tenant_id VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(255),
    version BIGINT DEFAULT 0,
    CONSTRAINT uk_tag_name_tenant UNIQUE (name, tenant_id)
);

-- Document Tags Table
CREATE TABLE document_tags (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    document_id UUID NOT NULL,
    tag_id UUID NOT NULL,
    tenant_id VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    CONSTRAINT fk_document_tag_document FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE,
    CONSTRAINT fk_document_tag_tag FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE,
    CONSTRAINT uk_document_tag UNIQUE (document_id, tag_id)
);

-- Signature Providers Table
CREATE TABLE signature_providers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    provider_code VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    is_default BOOLEAN DEFAULT FALSE,
    tenant_id VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(255),
    version BIGINT DEFAULT 0,
    CONSTRAINT uk_signature_provider_name_tenant UNIQUE (name, tenant_id),
    CONSTRAINT uk_signature_provider_code_tenant UNIQUE (provider_code, tenant_id)
);

-- Document Signatures Table
CREATE TABLE document_signatures (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    document_id UUID NOT NULL,
    document_version_id UUID,
    signature_provider_id UUID NOT NULL,
    signer_party_id UUID,
    signer_name VARCHAR(255),
    signer_email VARCHAR(255),
    signature_type signature_type NOT NULL,
    signature_format signature_format NOT NULL,
    signature_status signature_status NOT NULL DEFAULT 'PENDING',
    signature_data TEXT,
    signature_certificate TEXT,
    signature_position_x INTEGER,
    signature_position_y INTEGER,
    signature_page INTEGER,
    signature_width INTEGER,
    signature_height INTEGER,
    signature_reason VARCHAR(500),
    signature_location VARCHAR(255),
    signature_contact_info VARCHAR(255),
    expiration_date TIMESTAMP WITH TIME ZONE,
    signed_at TIMESTAMP WITH TIME ZONE,
    tenant_id VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(255),
    version BIGINT DEFAULT 0,
    CONSTRAINT fk_document_signature_document FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE,
    CONSTRAINT fk_document_signature_document_version FOREIGN KEY (document_version_id) REFERENCES document_versions(id) ON DELETE CASCADE,
    CONSTRAINT fk_document_signature_provider FOREIGN KEY (signature_provider_id) REFERENCES signature_providers(id) ON DELETE RESTRICT
);

-- Signature Requests Table
CREATE TABLE signature_requests (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    document_signature_id UUID NOT NULL,
    request_reference VARCHAR(100) NOT NULL,
    request_status signature_status NOT NULL DEFAULT 'PENDING',
    request_message TEXT,
    notification_sent BOOLEAN DEFAULT FALSE,
    notification_sent_at TIMESTAMP WITH TIME ZONE,
    reminder_sent BOOLEAN DEFAULT FALSE,
    reminder_sent_at TIMESTAMP WITH TIME ZONE,
    expiration_date TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE,
    tenant_id VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(255),
    version BIGINT DEFAULT 0,
    CONSTRAINT fk_signature_request_document_signature FOREIGN KEY (document_signature_id) REFERENCES document_signatures(id) ON DELETE CASCADE,
    CONSTRAINT uk_signature_request_reference UNIQUE (request_reference)
);

-- Signature Verifications Table
CREATE TABLE signature_verifications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    document_signature_id UUID NOT NULL,
    verification_status verification_status NOT NULL DEFAULT 'NOT_VERIFIED',
    verification_details TEXT,
    verification_provider VARCHAR(255),
    verification_timestamp TIMESTAMP WITH TIME ZONE,
    certificate_valid BOOLEAN,
    certificate_details TEXT,
    certificate_issuer VARCHAR(500),
    certificate_subject VARCHAR(500),
    certificate_valid_from TIMESTAMP WITH TIME ZONE,
    certificate_valid_until TIMESTAMP WITH TIME ZONE,
    document_integrity_valid BOOLEAN,
    tenant_id VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(255),
    version BIGINT DEFAULT 0,
    CONSTRAINT fk_signature_verification_document_signature FOREIGN KEY (document_signature_id) REFERENCES document_signatures(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_folders_parent_folder_id ON folders(parent_folder_id);
CREATE INDEX idx_folders_tenant_id ON folders(tenant_id);

CREATE INDEX idx_documents_folder_id ON documents(folder_id);
CREATE INDEX idx_documents_document_type ON documents(document_type);
CREATE INDEX idx_documents_document_status ON documents(document_status);
CREATE INDEX idx_documents_security_level ON documents(security_level);
CREATE INDEX idx_documents_tenant_id ON documents(tenant_id);
CREATE INDEX idx_documents_checksum ON documents(checksum);

CREATE INDEX idx_document_versions_document_id ON document_versions(document_id);
CREATE INDEX idx_document_versions_tenant_id ON document_versions(tenant_id);

CREATE INDEX idx_document_metadata_document_id ON document_metadata(document_id);
CREATE INDEX idx_document_metadata_is_searchable ON document_metadata(is_searchable);
CREATE INDEX idx_document_metadata_tenant_id ON document_metadata(tenant_id);

CREATE INDEX idx_document_permissions_document_id ON document_permissions(document_id);
CREATE INDEX idx_document_permissions_party_id ON document_permissions(party_id);
CREATE INDEX idx_document_permissions_tenant_id ON document_permissions(tenant_id);

CREATE INDEX idx_tags_tenant_id ON tags(tenant_id);

CREATE INDEX idx_document_tags_document_id ON document_tags(document_id);
CREATE INDEX idx_document_tags_tag_id ON document_tags(tag_id);
CREATE INDEX idx_document_tags_tenant_id ON document_tags(tenant_id);

CREATE INDEX idx_signature_providers_tenant_id ON signature_providers(tenant_id);
CREATE INDEX idx_signature_providers_is_default ON signature_providers(is_default);

CREATE INDEX idx_document_signatures_document_id ON document_signatures(document_id);
CREATE INDEX idx_document_signatures_document_version_id ON document_signatures(document_version_id);
CREATE INDEX idx_document_signatures_signature_provider_id ON document_signatures(signature_provider_id);
CREATE INDEX idx_document_signatures_signer_party_id ON document_signatures(signer_party_id);
CREATE INDEX idx_document_signatures_signature_status ON document_signatures(signature_status);
CREATE INDEX idx_document_signatures_tenant_id ON document_signatures(tenant_id);

CREATE INDEX idx_signature_requests_document_signature_id ON signature_requests(document_signature_id);
CREATE INDEX idx_signature_requests_request_status ON signature_requests(request_status);
CREATE INDEX idx_signature_requests_tenant_id ON signature_requests(tenant_id);

CREATE INDEX idx_signature_verifications_document_signature_id ON signature_verifications(document_signature_id);
CREATE INDEX idx_signature_verifications_verification_status ON signature_verifications(verification_status);
CREATE INDEX idx_signature_verifications_tenant_id ON signature_verifications(tenant_id);
