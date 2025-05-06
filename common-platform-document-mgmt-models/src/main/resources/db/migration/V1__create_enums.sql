-- Create enums for Enterprise Content Management system

-- Document Status Enum
CREATE TYPE document_status AS ENUM (
    'DRAFT',
    'UNDER_REVIEW',
    'APPROVED',
    'REJECTED',
    'PUBLISHED',
    'ARCHIVED',
    'MARKED_FOR_DELETION',
    'DELETED',
    'LOCKED',
    'EXPIRED'
);

-- Document Type Enum
CREATE TYPE document_type AS ENUM (
    'DOCUMENT',
    'IMAGE',
    'PDF',
    'CONTRACT',
    'INVOICE',
    'RECEIPT',
    'FORM',
    'REPORT',
    'EMAIL',
    'AUDIO',
    'VIDEO',
    'ARCHIVE',
    'OTHER'
);

-- Security Level Enum
CREATE TYPE security_level AS ENUM (
    'PUBLIC',
    'INTERNAL',
    'CONFIDENTIAL',
    'RESTRICTED',
    'SECRET',
    'TOP_SECRET'
);

-- Storage Type Enum
CREATE TYPE storage_type AS ENUM (
    'LOCAL_FILESYSTEM',
    'DATABASE',
    'S3',
    'AZURE_BLOB',
    'GOOGLE_CLOUD_STORAGE',
    'CDN',
    'DISTRIBUTED_FS',
    'EXTERNAL_REFERENCE'
);

-- Permission Type Enum
CREATE TYPE permission_type AS ENUM (
    'READ',
    'WRITE',
    'DELETE',
    'SHARE',
    'PRINT',
    'DOWNLOAD',
    'FULL_CONTROL',
    'CHANGE_PERMISSIONS',
    'VIEW_METADATA',
    'EDIT_METADATA'
);

-- Signature Status Enum
CREATE TYPE signature_status AS ENUM (
    'PENDING',
    'IN_PROGRESS',
    'SIGNED',
    'REJECTED',
    'EXPIRED',
    'REVOKED',
    'FAILED',
    'CANCELED'
);

-- Signature Type Enum
CREATE TYPE signature_type AS ENUM (
    'BASIC',
    'ADVANCED',
    'ADVANCED_WITH_QCERT',
    'QUALIFIED',
    'DIGITAL',
    'BIOMETRIC'
);

-- Signature Format Enum
CREATE TYPE signature_format AS ENUM (
    'PADES',
    'XADES',
    'CADES',
    'JADES',
    'PDF_VISIBLE',
    'PDF_INVISIBLE',
    'TIMESTAMP'
);

-- Verification Status Enum
CREATE TYPE verification_status AS ENUM (
    'VALID',
    'INVALID',
    'INDETERMINATE',
    'IN_PROGRESS',
    'FAILED',
    'NOT_VERIFIED'
);
