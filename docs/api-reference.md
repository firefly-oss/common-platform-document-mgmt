# API Reference

Complete REST API documentation for the Firefly OpenCore Platform Document Management Microservice.

## Base URLs

- **Local Development**: `http://localhost:8080`
- **Production**: `https://api.getfirefly.io/ecm`

## Authentication

All API endpoints require proper authentication. The service supports multi-tenant architecture with tenant-based isolation.

## Documents API

### List Documents

```http
GET /api/v1/documents
```

**Parameters:**
- Filter request parameters (query parameters for filtering)

**Response:**
```json
{
  "content": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "name": "Contract Agreement",
      "description": "Important contract document",
      "fileName": "contract.pdf",
      "fileExtension": "pdf",
      "mimeType": "application/pdf",
      "fileSize": 1024000,
      "documentType": "CONTRACT",
      "documentStatus": "ACTIVE",
      "storageType": "S3",
      "storagePath": "/documents/550e8400-e29b-41d4-a716-446655440000/contract.pdf",
      "securityLevel": "CONFIDENTIAL",
      "folderId": "550e8400-e29b-41d4-a716-446655440001",
      "isEncrypted": false,
      "isIndexed": true,
      "isLocked": false,
      "tenantId": "tenant-123",
      "createdAt": "2023-01-01T00:00:00",
      "createdBy": "user@getfirefly.io",
      "updatedAt": "2023-01-01T00:00:00",
      "updatedBy": "user@getfirefly.io",
      "version": 1
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "size": 20,
  "number": 0
}
```

### Get Document by ID

```http
GET /api/v1/documents/{id}
```

**Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Contract Agreement",
  "description": "Important contract document",
  "fileName": "contract.pdf",
  "fileExtension": "pdf",
  "mimeType": "application/pdf",
  "fileSize": 1024000,
  "documentType": "CONTRACT",
  "documentStatus": "ACTIVE",
  "storageType": "S3",
  "storagePath": "/documents/550e8400-e29b-41d4-a716-446655440000/contract.pdf",
  "securityLevel": "CONFIDENTIAL",
  "folderId": "550e8400-e29b-41d4-a716-446655440001",
  "isEncrypted": false,
  "isIndexed": true,
  "isLocked": false,
  "tenantId": "tenant-123",
  "createdAt": "2023-01-01T00:00:00",
  "createdBy": "user@getfirefly.io",
  "updatedAt": "2023-01-01T00:00:00",
  "updatedBy": "user@getfirefly.io",
  "version": 1
}
```

### Create Document

```http
POST /api/v1/documents
```

**Request Body:**
```json
{
  "name": "New Document",
  "description": "Document description",
  "documentType": "DOCUMENT",
  "documentStatus": "DRAFT",
  "storageType": "S3",
  "securityLevel": "INTERNAL",
  "folderId": "550e8400-e29b-41d4-a716-446655440001",
  "tenantId": "tenant-123"
}
```

**Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "name": "New Document",
  "description": "Document description",
  "documentType": "DOCUMENT",
  "documentStatus": "DRAFT",
  "storageType": "S3",
  "securityLevel": "INTERNAL",
  "folderId": "550e8400-e29b-41d4-a716-446655440001",
  "tenantId": "tenant-123",
  "createdAt": "2023-01-01T00:00:00",
  "createdBy": "user@getfirefly.io",
  "version": 1
}
```

### Update Document

```http
PUT /api/v1/documents/{id}
```

**Request Body:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Updated Document Name",
  "description": "Updated description",
  "documentType": "CONTRACT",
  "documentStatus": "ACTIVE",
  "securityLevel": "CONFIDENTIAL"
}
```

### Delete Document

```http
DELETE /api/v1/documents/{id}
```

**Response:** `204 No Content`

## ECM Content Operations

### Upload Document Content

```http
POST /api/v1/documents/{id}/upload
Content-Type: multipart/form-data
```

**Request:**
- `file`: Document file (multipart)

**ECM Available Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Contract Agreement",
  "fileName": "contract.pdf",
  "mimeType": "application/pdf",
  "fileSize": 1024000,
  "storagePath": "/documents/550e8400-e29b-41d4-a716-446655440000/contract.pdf",
  "storageType": "S3",
  "updatedAt": "2023-01-01T00:00:00",
  "updatedBy": "user@getfirefly.io"
}
```

**ECM Unavailable Response:**
```json
{
  "error": "Internal Server Error",
  "message": "Document content upload requires ECM DocumentContentPort to be configured",
  "status": 500,
  "timestamp": "2023-01-01T00:00:00",
  "path": "/api/v1/documents/{id}/upload"
}
```

### Download Document Content

```http
GET /api/v1/documents/{id}/download
```

**ECM Available Response:**
- Content-Type: `application/octet-stream`
- Content-Disposition: `attachment; filename="contract.pdf"`
- Body: Document content stream

**ECM Unavailable Response:**
```json
{
  "error": "Internal Server Error",
  "message": "Document content download requires ECM DocumentContentPort to be configured",
  "status": 500
}
```

### Create Document Version

```http
POST /api/v1/documents/{id}/create-version
Content-Type: multipart/form-data
```

**Request:**
- `file`: New version file (multipart)
- `versionComment`: Version comment (optional)

**Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Contract Agreement",
  "fileName": "contract_v2.pdf",
  "mimeType": "application/pdf",
  "fileSize": 1024000,
  "version": 2,
  "updatedAt": "2023-01-01T00:00:00",
  "updatedBy": "user@getfirefly.io"
}
```

### Get Document Content Metadata

```http
GET /api/v1/documents/{id}/metadata
```

**Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Contract Agreement",
  "fileName": "contract.pdf",
  "fileExtension": "pdf",
  "mimeType": "application/pdf",
  "fileSize": 1024000,
  "storagePath": "/documents/550e8400-e29b-41d4-a716-446655440000/contract.pdf",
  "storageType": "S3"
}
```

## Document Signature API

### List Document Signatures

```http
GET /api/v1/document-signatures
```

**Response:**
```json
{
  "content": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440002",
      "documentId": "550e8400-e29b-41d4-a716-446655440000",
      "documentVersionId": "550e8400-e29b-41d4-a716-446655440003",
      "signatureProviderId": "550e8400-e29b-41d4-a716-446655440004",
      "signerPartyId": "550e8400-e29b-41d4-a716-446655440005",
      "signerName": "John Doe",
      "signerEmail": "signer@getfirefly.io",
      "signatureType": "ELECTRONIC",
      "signatureFormat": "PKCS7",
      "signatureStatus": "PENDING",
      "customMessage": "Please sign this important document",
      "language": "en",
      "signerTimeZone": "UTC",
      "signingOrder": 1,
      "signerRole": "Signer",
      "signatureRequired": true,
      "authenticationMethod": "EMAIL",
      "expirationDate": "2023-01-31T23:59:59",
      "tenantId": "tenant-123",
      "createdAt": "2023-01-01T00:00:00",
      "createdBy": "user@getfirefly.io",
      "version": 1
    }
  ]
}
```

### Create Document Signature

```http
POST /api/v1/document-signatures
```

**Request Body:**
```json
{
  "documentId": "550e8400-e29b-41d4-a716-446655440000",
  "documentVersionId": "550e8400-e29b-41d4-a716-446655440003",
  "signatureProviderId": "550e8400-e29b-41d4-a716-446655440004",
  "signerPartyId": "550e8400-e29b-41d4-a716-446655440005",
  "signerName": "John Doe",
  "signerEmail": "signer@getfirefly.io",
  "signatureType": "ELECTRONIC",
  "signatureFormat": "PKCS7",
  "customMessage": "Please sign this important document",
  "language": "en",
  "signerTimeZone": "UTC",
  "signingOrder": 1,
  "signerRole": "Signer",
  "signatureRequired": true,
  "authenticationMethod": "EMAIL",
  "expirationDate": "2023-01-31T23:59:59",
  "tenantId": "tenant-123"
}
```

**ECM Available Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440002",
  "documentId": "550e8400-e29b-41d4-a716-446655440000",
  "signerEmail": "signer@getfirefly.io",
  "signatureType": "ELECTRONIC",
  "signatureStatus": "SENT",
  "externalRequestId": "docusign-envelope-123",
  "createdAt": "2023-01-01T00:00:00",
  "version": 1
}
```

**ECM Unavailable Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440002",
  "documentId": "550e8400-e29b-41d4-a716-446655440000",
  "signerEmail": "signer@getfirefly.io",
  "signatureType": "ELECTRONIC",
  "signatureStatus": "PENDING",
  "createdAt": "2023-01-01T00:00:00",
  "version": 1
}
```

### Get Document Signature by ID

```http
GET /api/v1/document-signatures/{id}
```

**Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440002",
  "documentId": "550e8400-e29b-41d4-a716-446655440000",
  "documentVersionId": "550e8400-e29b-41d4-a716-446655440003",
  "signatureProviderId": "550e8400-e29b-41d4-a716-446655440004",
  "signerPartyId": "550e8400-e29b-41d4-a716-446655440005",
  "signerName": "John Doe",
  "signerEmail": "signer@getfirefly.io",
  "signatureType": "ELECTRONIC",
  "signatureFormat": "PKCS7",
  "signatureStatus": "COMPLETED",
  "signatureData": "base64-encoded-signature-data",
  "signatureCertificate": "base64-encoded-certificate",
  "signedAt": "2023-01-15T10:30:00",
  "tenantId": "tenant-123",
  "createdAt": "2023-01-01T00:00:00",
  "version": 1
}
```

## Folder Management API

### List Folders

```http
GET /api/folders
```

**Response:**
```json
{
  "content": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440003",
      "name": "Contracts",
      "description": "Legal contracts folder",
      "parentFolderId": null,
      "createdAt": "2023-01-01T00:00:00Z"
    }
  ]
}
```

### Create Folder

```http
POST /api/folders
```

**Request Body:**
```json
{
  "name": "New Folder",
  "description": "Folder description",
  "parentFolderId": "550e8400-e29b-41d4-a716-446655440003"
}
```

## Tag Management API

### List Tags

```http
GET /api/tags
```

**Response:**
```json
{
  "content": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440004",
      "name": "urgent",
      "description": "Urgent documents",
      "color": "#FF0000"
    }
  ]
}
```

## Error Responses

### Standard Error Format

```json
{
  "error": "Error Type",
  "message": "Detailed error message",
  "status": 400,
  "timestamp": "2023-01-01T00:00:00Z",
  "path": "/api/endpoint"
}
```

### Common HTTP Status Codes

- `200 OK` - Successful operation
- `201 Created` - Resource created successfully
- `204 No Content` - Successful operation with no response body
- `400 Bad Request` - Invalid request parameters
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - ECM port unavailable or server error

## Health Check Endpoints

### Overall Health

```http
GET /actuator/health
```

**Response:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    },
    "ecm": {
      "status": "UP",
      "details": {
        "documentContentPort": "available",
        "signatureRequestPort": "available"
      }
    }
  }
}
```

### ECM Integration Status

```http
GET /actuator/health/ecm
```

**Response:**
```json
{
  "status": "UP",
  "details": {
    "documentContentPort": "available",
    "documentVersionPort": "available",
    "signatureRequestPort": "available",
    "signatureEnvelopePort": "available"
  }
}
```

## OpenAPI Specification

When the service is running, the complete OpenAPI specification is available at:
- **JSON Format**: `/v3/api-docs`
- **Interactive UI**: `/swagger-ui.html`
