-- Migration script to convert all primary keys and foreign keys from BIGINT to UUID
-- This script handles the conversion while preserving existing data relationships

-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Step 1: Add temporary UUID columns to all tables
-- These will become the new primary keys

-- Add UUID columns to main tables
ALTER TABLE folders ADD COLUMN uuid_id UUID DEFAULT uuid_generate_v4();
ALTER TABLE documents ADD COLUMN uuid_id UUID DEFAULT uuid_generate_v4();
ALTER TABLE document_versions ADD COLUMN uuid_id UUID DEFAULT uuid_generate_v4();
ALTER TABLE document_metadata ADD COLUMN uuid_id UUID DEFAULT uuid_generate_v4();
ALTER TABLE document_permissions ADD COLUMN uuid_id UUID DEFAULT uuid_generate_v4();
ALTER TABLE tags ADD COLUMN uuid_id UUID DEFAULT uuid_generate_v4();
ALTER TABLE document_tags ADD COLUMN uuid_id UUID DEFAULT uuid_generate_v4();
ALTER TABLE signature_providers ADD COLUMN uuid_id UUID DEFAULT uuid_generate_v4();
ALTER TABLE document_signatures ADD COLUMN uuid_id UUID DEFAULT uuid_generate_v4();
ALTER TABLE signature_requests ADD COLUMN uuid_id UUID DEFAULT uuid_generate_v4();
ALTER TABLE signature_verifications ADD COLUMN uuid_id UUID DEFAULT uuid_generate_v4();

-- Step 2: Add temporary UUID foreign key columns
-- These will reference the new UUID primary keys

-- Folders table foreign keys
ALTER TABLE folders ADD COLUMN uuid_parent_folder_id UUID;

-- Documents table foreign keys
ALTER TABLE documents ADD COLUMN uuid_folder_id UUID;

-- Document versions table foreign keys
ALTER TABLE document_versions ADD COLUMN uuid_document_id UUID;

-- Document metadata table foreign keys
ALTER TABLE document_metadata ADD COLUMN uuid_document_id UUID;

-- Document permissions table foreign keys
ALTER TABLE document_permissions ADD COLUMN uuid_document_id UUID;

-- Document tags table foreign keys
ALTER TABLE document_tags ADD COLUMN uuid_document_id UUID;
ALTER TABLE document_tags ADD COLUMN uuid_tag_id UUID;

-- Document signatures table foreign keys
ALTER TABLE document_signatures ADD COLUMN uuid_document_id UUID;
ALTER TABLE document_signatures ADD COLUMN uuid_document_version_id UUID;
ALTER TABLE document_signatures ADD COLUMN uuid_signature_provider_id UUID;

-- Signature requests table foreign keys
ALTER TABLE signature_requests ADD COLUMN uuid_document_signature_id UUID;

-- Signature verifications table foreign keys
ALTER TABLE signature_verifications ADD COLUMN uuid_document_signature_id UUID;

-- Step 3: Update UUID foreign key columns with corresponding UUID values
-- This preserves the relationships using the new UUID system

-- Update folders parent relationships
UPDATE folders 
SET uuid_parent_folder_id = parent.uuid_id 
FROM folders parent 
WHERE folders.parent_folder_id = parent.id;

-- Update documents folder relationships
UPDATE documents 
SET uuid_folder_id = f.uuid_id 
FROM folders f 
WHERE documents.folder_id = f.id;

-- Update document versions relationships
UPDATE document_versions 
SET uuid_document_id = d.uuid_id 
FROM documents d 
WHERE document_versions.document_id = d.id;

-- Update document metadata relationships
UPDATE document_metadata 
SET uuid_document_id = d.uuid_id 
FROM documents d 
WHERE document_metadata.document_id = d.id;

-- Update document permissions relationships
UPDATE document_permissions 
SET uuid_document_id = d.uuid_id 
FROM documents d 
WHERE document_permissions.document_id = d.id;

-- Update document tags relationships
UPDATE document_tags 
SET uuid_document_id = d.uuid_id 
FROM documents d 
WHERE document_tags.document_id = d.id;

UPDATE document_tags 
SET uuid_tag_id = t.uuid_id 
FROM tags t 
WHERE document_tags.tag_id = t.id;

-- Update document signatures relationships
UPDATE document_signatures 
SET uuid_document_id = d.uuid_id 
FROM documents d 
WHERE document_signatures.document_id = d.id;

UPDATE document_signatures 
SET uuid_document_version_id = dv.uuid_id 
FROM document_versions dv 
WHERE document_signatures.document_version_id = dv.id;

UPDATE document_signatures 
SET uuid_signature_provider_id = sp.uuid_id 
FROM signature_providers sp 
WHERE document_signatures.signature_provider_id = sp.id;

-- Update signature requests relationships
UPDATE signature_requests 
SET uuid_document_signature_id = ds.uuid_id 
FROM document_signatures ds 
WHERE signature_requests.document_signature_id = ds.id;

-- Update signature verifications relationships
UPDATE signature_verifications 
SET uuid_document_signature_id = ds.uuid_id 
FROM document_signatures ds 
WHERE signature_verifications.document_signature_id = ds.id;

-- Step 4: Drop existing foreign key constraints
-- We need to remove these before we can drop the old columns

ALTER TABLE folders DROP CONSTRAINT IF EXISTS fk_folder_parent;
ALTER TABLE documents DROP CONSTRAINT IF EXISTS fk_document_folder;
ALTER TABLE document_versions DROP CONSTRAINT IF EXISTS fk_document_version_document;
ALTER TABLE document_metadata DROP CONSTRAINT IF EXISTS fk_document_metadata_document;
ALTER TABLE document_permissions DROP CONSTRAINT IF EXISTS fk_document_permission_document;
ALTER TABLE document_tags DROP CONSTRAINT IF EXISTS fk_document_tag_document;
ALTER TABLE document_tags DROP CONSTRAINT IF EXISTS fk_document_tag_tag;
ALTER TABLE document_signatures DROP CONSTRAINT IF EXISTS fk_document_signature_document;
ALTER TABLE document_signatures DROP CONSTRAINT IF EXISTS fk_document_signature_document_version;
ALTER TABLE document_signatures DROP CONSTRAINT IF EXISTS fk_document_signature_provider;
ALTER TABLE signature_requests DROP CONSTRAINT IF EXISTS fk_signature_request_document_signature;
ALTER TABLE signature_verifications DROP CONSTRAINT IF EXISTS fk_signature_verification_document_signature;

-- Step 5: Drop existing indexes that reference the old columns
DROP INDEX IF EXISTS idx_folders_parent_folder_id;
DROP INDEX IF EXISTS idx_documents_folder_id;
DROP INDEX IF EXISTS idx_document_versions_document_id;
DROP INDEX IF EXISTS idx_document_metadata_document_id;
DROP INDEX IF EXISTS idx_document_permissions_document_id;
DROP INDEX IF EXISTS idx_document_permissions_party_id;
DROP INDEX IF EXISTS idx_document_tags_document_id;
DROP INDEX IF EXISTS idx_document_tags_tag_id;
DROP INDEX IF EXISTS idx_document_signatures_document_id;
DROP INDEX IF EXISTS idx_document_signatures_document_version_id;
DROP INDEX IF EXISTS idx_document_signatures_signature_provider_id;
DROP INDEX IF EXISTS idx_document_signatures_signer_party_id;
DROP INDEX IF EXISTS idx_signature_requests_document_signature_id;
DROP INDEX IF EXISTS idx_signature_verifications_document_signature_id;

-- Step 6: Drop old primary key constraints and columns
-- Remove the old BIGINT primary keys

ALTER TABLE folders DROP CONSTRAINT folders_pkey;
ALTER TABLE documents DROP CONSTRAINT documents_pkey;
ALTER TABLE document_versions DROP CONSTRAINT document_versions_pkey;
ALTER TABLE document_metadata DROP CONSTRAINT document_metadata_pkey;
ALTER TABLE document_permissions DROP CONSTRAINT document_permissions_pkey;
ALTER TABLE tags DROP CONSTRAINT tags_pkey;
ALTER TABLE document_tags DROP CONSTRAINT document_tags_pkey;
ALTER TABLE signature_providers DROP CONSTRAINT signature_providers_pkey;
ALTER TABLE document_signatures DROP CONSTRAINT document_signatures_pkey;
ALTER TABLE signature_requests DROP CONSTRAINT signature_requests_pkey;
ALTER TABLE signature_verifications DROP CONSTRAINT signature_verifications_pkey;

-- Drop old foreign key columns
ALTER TABLE folders DROP COLUMN id;
ALTER TABLE folders DROP COLUMN parent_folder_id;

ALTER TABLE documents DROP COLUMN id;
ALTER TABLE documents DROP COLUMN folder_id;

ALTER TABLE document_versions DROP COLUMN id;
ALTER TABLE document_versions DROP COLUMN document_id;

ALTER TABLE document_metadata DROP COLUMN id;
ALTER TABLE document_metadata DROP COLUMN document_id;

ALTER TABLE document_permissions DROP COLUMN id;
ALTER TABLE document_permissions DROP COLUMN document_id;

ALTER TABLE tags DROP COLUMN id;

ALTER TABLE document_tags DROP COLUMN id;
ALTER TABLE document_tags DROP COLUMN document_id;
ALTER TABLE document_tags DROP COLUMN tag_id;

ALTER TABLE signature_providers DROP COLUMN id;

ALTER TABLE document_signatures DROP COLUMN id;
ALTER TABLE document_signatures DROP COLUMN document_id;
ALTER TABLE document_signatures DROP COLUMN document_version_id;
ALTER TABLE document_signatures DROP COLUMN signature_provider_id;

ALTER TABLE signature_requests DROP COLUMN id;
ALTER TABLE signature_requests DROP COLUMN document_signature_id;

ALTER TABLE signature_verifications DROP COLUMN id;
ALTER TABLE signature_verifications DROP COLUMN document_signature_id;

-- Step 7: Rename UUID columns to become the new primary/foreign keys

-- Rename UUID primary key columns to 'id'
ALTER TABLE folders RENAME COLUMN uuid_id TO id;
ALTER TABLE documents RENAME COLUMN uuid_id TO id;
ALTER TABLE document_versions RENAME COLUMN uuid_id TO id;
ALTER TABLE document_metadata RENAME COLUMN uuid_id TO id;
ALTER TABLE document_permissions RENAME COLUMN uuid_id TO id;
ALTER TABLE tags RENAME COLUMN uuid_id TO id;
ALTER TABLE document_tags RENAME COLUMN uuid_id TO id;
ALTER TABLE signature_providers RENAME COLUMN uuid_id TO id;
ALTER TABLE document_signatures RENAME COLUMN uuid_id TO id;
ALTER TABLE signature_requests RENAME COLUMN uuid_id TO id;
ALTER TABLE signature_verifications RENAME COLUMN uuid_id TO id;

-- Rename UUID foreign key columns
ALTER TABLE folders RENAME COLUMN uuid_parent_folder_id TO parent_folder_id;
ALTER TABLE documents RENAME COLUMN uuid_folder_id TO folder_id;
ALTER TABLE document_versions RENAME COLUMN uuid_document_id TO document_id;
ALTER TABLE document_metadata RENAME COLUMN uuid_document_id TO document_id;
ALTER TABLE document_permissions RENAME COLUMN uuid_document_id TO document_id;
ALTER TABLE document_tags RENAME COLUMN uuid_document_id TO document_id;
ALTER TABLE document_tags RENAME COLUMN uuid_tag_id TO tag_id;
ALTER TABLE document_signatures RENAME COLUMN uuid_document_id TO document_id;
ALTER TABLE document_signatures RENAME COLUMN uuid_document_version_id TO document_version_id;
ALTER TABLE document_signatures RENAME COLUMN uuid_signature_provider_id TO signature_provider_id;
ALTER TABLE signature_requests RENAME COLUMN uuid_document_signature_id TO document_signature_id;
ALTER TABLE signature_verifications RENAME COLUMN uuid_document_signature_id TO document_signature_id;

-- Step 8: Add new primary key constraints with UUID columns
ALTER TABLE folders ADD CONSTRAINT folders_pkey PRIMARY KEY (id);
ALTER TABLE documents ADD CONSTRAINT documents_pkey PRIMARY KEY (id);
ALTER TABLE document_versions ADD CONSTRAINT document_versions_pkey PRIMARY KEY (id);
ALTER TABLE document_metadata ADD CONSTRAINT document_metadata_pkey PRIMARY KEY (id);
ALTER TABLE document_permissions ADD CONSTRAINT document_permissions_pkey PRIMARY KEY (id);
ALTER TABLE tags ADD CONSTRAINT tags_pkey PRIMARY KEY (id);
ALTER TABLE document_tags ADD CONSTRAINT document_tags_pkey PRIMARY KEY (id);
ALTER TABLE signature_providers ADD CONSTRAINT signature_providers_pkey PRIMARY KEY (id);
ALTER TABLE document_signatures ADD CONSTRAINT document_signatures_pkey PRIMARY KEY (id);
ALTER TABLE signature_requests ADD CONSTRAINT signature_requests_pkey PRIMARY KEY (id);
ALTER TABLE signature_verifications ADD CONSTRAINT signature_verifications_pkey PRIMARY KEY (id);

-- Step 9: Add new foreign key constraints with UUID columns
ALTER TABLE folders ADD CONSTRAINT fk_folder_parent FOREIGN KEY (parent_folder_id) REFERENCES folders(id) ON DELETE SET NULL;
ALTER TABLE documents ADD CONSTRAINT fk_document_folder FOREIGN KEY (folder_id) REFERENCES folders(id) ON DELETE SET NULL;
ALTER TABLE document_versions ADD CONSTRAINT fk_document_version_document FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE;
ALTER TABLE document_metadata ADD CONSTRAINT fk_document_metadata_document FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE;
ALTER TABLE document_permissions ADD CONSTRAINT fk_document_permission_document FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE;
ALTER TABLE document_tags ADD CONSTRAINT fk_document_tag_document FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE;
ALTER TABLE document_tags ADD CONSTRAINT fk_document_tag_tag FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE;
ALTER TABLE document_signatures ADD CONSTRAINT fk_document_signature_document FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE;
ALTER TABLE document_signatures ADD CONSTRAINT fk_document_signature_document_version FOREIGN KEY (document_version_id) REFERENCES document_versions(id) ON DELETE CASCADE;
ALTER TABLE document_signatures ADD CONSTRAINT fk_document_signature_provider FOREIGN KEY (signature_provider_id) REFERENCES signature_providers(id) ON DELETE RESTRICT;
ALTER TABLE signature_requests ADD CONSTRAINT fk_signature_request_document_signature FOREIGN KEY (document_signature_id) REFERENCES document_signatures(id) ON DELETE CASCADE;
ALTER TABLE signature_verifications ADD CONSTRAINT fk_signature_verification_document_signature FOREIGN KEY (document_signature_id) REFERENCES document_signatures(id) ON DELETE CASCADE;

-- Step 10: Recreate indexes with UUID columns for better performance
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

-- Step 11: Recreate unique constraints that were dropped
ALTER TABLE document_versions ADD CONSTRAINT uk_document_version UNIQUE (document_id, version_number);
ALTER TABLE document_metadata ADD CONSTRAINT uk_document_metadata_key UNIQUE (document_id, metadata_key);
ALTER TABLE document_permissions ADD CONSTRAINT uk_document_permission UNIQUE (document_id, party_id, permission_type);
ALTER TABLE tags ADD CONSTRAINT uk_tag_name_tenant UNIQUE (name, tenant_id);
ALTER TABLE document_tags ADD CONSTRAINT uk_document_tag UNIQUE (document_id, tag_id);
ALTER TABLE signature_providers ADD CONSTRAINT uk_signature_provider_name_tenant UNIQUE (name, tenant_id);
ALTER TABLE signature_providers ADD CONSTRAINT uk_signature_provider_code_tenant UNIQUE (provider_code, tenant_id);
ALTER TABLE signature_requests ADD CONSTRAINT uk_signature_request_reference UNIQUE (request_reference);

-- Step 12: Set NOT NULL constraints on UUID primary keys
ALTER TABLE folders ALTER COLUMN id SET NOT NULL;
ALTER TABLE documents ALTER COLUMN id SET NOT NULL;
ALTER TABLE document_versions ALTER COLUMN id SET NOT NULL;
ALTER TABLE document_metadata ALTER COLUMN id SET NOT NULL;
ALTER TABLE document_permissions ALTER COLUMN id SET NOT NULL;
ALTER TABLE tags ALTER COLUMN id SET NOT NULL;
ALTER TABLE document_tags ALTER COLUMN id SET NOT NULL;
ALTER TABLE signature_providers ALTER COLUMN id SET NOT NULL;
ALTER TABLE document_signatures ALTER COLUMN id SET NOT NULL;
ALTER TABLE signature_requests ALTER COLUMN id SET NOT NULL;
ALTER TABLE signature_verifications ALTER COLUMN id SET NOT NULL;

-- Step 13: Set default UUID generation for new records
ALTER TABLE folders ALTER COLUMN id SET DEFAULT uuid_generate_v4();
ALTER TABLE documents ALTER COLUMN id SET DEFAULT uuid_generate_v4();
ALTER TABLE document_versions ALTER COLUMN id SET DEFAULT uuid_generate_v4();
ALTER TABLE document_metadata ALTER COLUMN id SET DEFAULT uuid_generate_v4();
ALTER TABLE document_permissions ALTER COLUMN id SET DEFAULT uuid_generate_v4();
ALTER TABLE tags ALTER COLUMN id SET DEFAULT uuid_generate_v4();
ALTER TABLE document_tags ALTER COLUMN id SET DEFAULT uuid_generate_v4();
ALTER TABLE signature_providers ALTER COLUMN id SET DEFAULT uuid_generate_v4();
ALTER TABLE document_signatures ALTER COLUMN id SET DEFAULT uuid_generate_v4();
ALTER TABLE signature_requests ALTER COLUMN id SET DEFAULT uuid_generate_v4();
ALTER TABLE signature_verifications ALTER COLUMN id SET DEFAULT uuid_generate_v4();

-- Migration completed successfully
-- All tables now use UUID as primary keys and foreign keys
-- All relationships have been preserved
-- All indexes and constraints have been recreated
