-- Create casts for Enterprise Content Management system enums

-- Document Status casts
CREATE CAST (varchar AS document_status) WITH INOUT AS IMPLICIT;
CREATE CAST (document_status AS varchar) WITH INOUT AS IMPLICIT;

-- Document Type casts
CREATE CAST (varchar AS document_type) WITH INOUT AS IMPLICIT;
CREATE CAST (document_type AS varchar) WITH INOUT AS IMPLICIT;

-- Security Level casts
CREATE CAST (varchar AS security_level) WITH INOUT AS IMPLICIT;
CREATE CAST (security_level AS varchar) WITH INOUT AS IMPLICIT;

-- Storage Type casts
CREATE CAST (varchar AS storage_type) WITH INOUT AS IMPLICIT;
CREATE CAST (storage_type AS varchar) WITH INOUT AS IMPLICIT;

-- Permission Type casts
CREATE CAST (varchar AS permission_type) WITH INOUT AS IMPLICIT;
CREATE CAST (permission_type AS varchar) WITH INOUT AS IMPLICIT;

-- Signature Status casts
CREATE CAST (varchar AS signature_status) WITH INOUT AS IMPLICIT;
CREATE CAST (signature_status AS varchar) WITH INOUT AS IMPLICIT;

-- Signature Type casts
CREATE CAST (varchar AS signature_type) WITH INOUT AS IMPLICIT;
CREATE CAST (signature_type AS varchar) WITH INOUT AS IMPLICIT;

-- Signature Format casts
CREATE CAST (varchar AS signature_format) WITH INOUT AS IMPLICIT;
CREATE CAST (signature_format AS varchar) WITH INOUT AS IMPLICIT;

-- Verification Status casts
CREATE CAST (varchar AS verification_status) WITH INOUT AS IMPLICIT;
CREATE CAST (verification_status AS varchar) WITH INOUT AS IMPLICIT;
