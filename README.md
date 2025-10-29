# Firefly OpenCore Platform - Document Management Microservice

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java](https://img.shields.io/badge/Java-21+-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.6+-red.svg)](https://maven.apache.org/)

A comprehensive, enterprise-grade document management microservice built on Spring Boot WebFlux with reactive programming principles. This microservice is a core component of the **Firefly OpenCore Platform** - an open-source core banking solution that provides advanced document lifecycle management, digital signature capabilities, and seamless integration with the Firefly lib-ecm-core framework for financial institutions.

## 🎯 Overview

The Document Management Microservice provides comprehensive Enterprise Content Management (ECM) capabilities through seamless integration with the lib-ecm-core library. It offers flexible deployment options from standalone document metadata management to full-featured ECM with cloud storage and digital signatures.

### Key Features

- **📄 Document Lifecycle Management**: Complete CRUD operations with metadata, versioning, and audit trails
- **🔐 Digital Signatures**: Integration with e-signature providers (DocuSign, Adobe Sign) through lib-ecm-core
- **☁️ Multi-Cloud Storage**: Support for S3, Azure Blob Storage, and local file systems via ECM adapters
- **📁 Hierarchical Organization**: Folder structure with inheritance-based permissions
- **🏷️ Flexible Tagging**: Document categorization and search capabilities
- **⚡ Reactive Architecture**: Built with Spring WebFlux for high-performance, non-blocking operations
- **🔧 Graceful Degradation**: Works with or without ECM adapters configured

### ECM Integration Capabilities

The microservice leverages **lib-ecm-core** to provide:

- **EcmPortProvider**: Central access to ECM functionality through port-based architecture
- **DocumentContentPort**: Content storage and retrieval operations
- **DocumentVersionPort**: Version management and history tracking
- **SignatureRequestPort**: Digital signature workflow integration
- **SignatureEnvelopePort**: Signature envelope management

## 🚀 Quick Start

### Prerequisites

- **Java 21+** - OpenJDK or Oracle JDK
- **Maven 3.6+** - Build tool
- **PostgreSQL 12+** - Database
- **Docker** (optional) - For containerized deployment

### 1. Clone and Build

```bash
git clone https://github.com/firefly-oss/common-platform-document-mgmt.git
cd common-platform-document-mgmt
mvn clean install
```

### 2. Database Setup

```sql
-- Create database
CREATE DATABASE firefly_ecm;

-- Create user (optional)
CREATE USER firefly_user WITH PASSWORD 'firefly_password';
GRANT ALL PRIVILEGES ON DATABASE firefly_ecm TO firefly_user;
```

### 3. Basic Configuration

Create `application-local.yml`:

```yaml
spring:
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/firefly_ecm
    username: firefly_user
    password: firefly_password
  
  flyway:
    url: jdbc:postgresql://localhost:5432/firefly_ecm
    user: firefly_user
    password: firefly_password

# Business logic configuration
firefly:
  ecm:
    integration:
      signature:
        custom-message: "Please review and sign this document"
        language: "en"
        expiration-days: 30
      document:
        security-level: "INTERNAL"
        retention-days: 2555
```

### 4. Run the Application

```bash
cd common-platform-document-mgmt-web
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

The service will be available at `http://localhost:8080`

## 🏗️ Architecture

The microservice follows a modular, multi-module Maven architecture:

```
common-platform-document-mgmt/
├── common-platform-document-mgmt-web/          # REST API layer
├── common-platform-document-mgmt-core/         # Business logic & services
├── common-platform-document-mgmt-interfaces/   # DTOs & contracts
├── common-platform-document-mgmt-models/       # JPA entities & repositories
└── common-platform-document-mgmt-sdk/          # Client SDK
```

### Database Schema

```mermaid
erDiagram
    DOCUMENTS {
        UUID id PK
        VARCHAR name
        TEXT description
        VARCHAR file_name
        VARCHAR file_extension
        VARCHAR mime_type
        BIGINT file_size
        ENUM document_type
        ENUM document_status
        ENUM storage_type
        VARCHAR storage_path
        ENUM security_level
        UUID folder_id FK
        BOOLEAN is_encrypted
        BOOLEAN is_indexed
        BOOLEAN is_locked
        VARCHAR locked_by
        TIMESTAMP locked_until
        TIMESTAMP expiration_date
        TIMESTAMP retention_date
        VARCHAR tenant_id
        TIMESTAMP created_at
        VARCHAR created_by
        TIMESTAMP updated_at
        VARCHAR updated_by
        BIGINT version
    }

    FOLDERS {
        UUID id PK
        VARCHAR name
        TEXT description
        UUID parent_folder_id FK
        VARCHAR path
        ENUM security_level
        BOOLEAN is_system_folder
        VARCHAR tenant_id
        TIMESTAMP created_at
        VARCHAR created_by
        TIMESTAMP updated_at
        VARCHAR updated_by
        BIGINT version
    }

    TAGS {
        UUID id PK
        VARCHAR name
        TEXT description
        VARCHAR color
        BOOLEAN is_system_tag
        VARCHAR tenant_id
        TIMESTAMP created_at
        VARCHAR created_by
        TIMESTAMP updated_at
        VARCHAR updated_by
        BIGINT version
    }

    DOCUMENT_TAGS {
        UUID id PK
        UUID document_id FK
        UUID tag_id FK
        VARCHAR tenant_id
        TIMESTAMP created_at
        VARCHAR created_by
    }

    DOCUMENT_SIGNATURES {
        UUID id PK
        UUID document_id FK
        UUID document_version_id FK
        UUID signature_provider_id FK
        UUID signer_party_id FK
        VARCHAR signer_name
        VARCHAR signer_email
        ENUM signature_type
        ENUM signature_format
        ENUM signature_status
        TEXT signature_data
        TEXT signature_certificate
        VARCHAR custom_message
        VARCHAR language
        VARCHAR signer_time_zone
        INTEGER signing_order
        VARCHAR signer_role
        BOOLEAN signature_required
        VARCHAR authentication_method
        TIMESTAMP expiration_date
        TIMESTAMP signed_at
        VARCHAR tenant_id
        TIMESTAMP created_at
        VARCHAR created_by
        TIMESTAMP updated_at
        VARCHAR updated_by
        BIGINT version
    }

    SIGNATURE_REQUESTS {
        UUID id PK
        UUID document_signature_id FK
        VARCHAR request_reference
        ENUM request_status
        TEXT request_message
        BOOLEAN notification_sent
        TIMESTAMP notification_sent_at
        BOOLEAN reminder_sent
        TIMESTAMP reminder_sent_at
        TIMESTAMP expiration_date
        TIMESTAMP completed_at
        VARCHAR tenant_id
        TIMESTAMP created_at
        VARCHAR created_by
        TIMESTAMP updated_at
        VARCHAR updated_by
        BIGINT version
    }

    DOCUMENT_VERSIONS {
        UUID id PK
        UUID document_id FK
        INTEGER version_number
        VARCHAR file_name
        VARCHAR file_extension
        VARCHAR mime_type
        BIGINT file_size
        ENUM storage_type
        VARCHAR storage_path
        BOOLEAN is_encrypted
        TEXT change_summary
        BOOLEAN is_major_version
        VARCHAR tenant_id
        TIMESTAMP created_at
        VARCHAR created_by
    }

    DOCUMENT_METADATA {
        UUID id PK
        UUID document_id FK
        VARCHAR metadata_key
        TEXT metadata_value
        VARCHAR metadata_type
        BOOLEAN is_searchable
        BOOLEAN is_system_metadata
        VARCHAR tenant_id
        TIMESTAMP created_at
        VARCHAR created_by
        TIMESTAMP updated_at
        VARCHAR updated_by
        BIGINT version
    }

    DOCUMENT_PERMISSIONS {
        UUID id PK
        UUID document_id FK
        UUID party_id FK
        ENUM permission_type
        BOOLEAN is_granted
        TIMESTAMP expiration_date
        VARCHAR tenant_id
        TIMESTAMP created_at
        VARCHAR created_by
        TIMESTAMP updated_at
        VARCHAR updated_by
        BIGINT version
    }

    DOCUMENTS ||--o{ DOCUMENT_TAGS : "has"
    TAGS ||--o{ DOCUMENT_TAGS : "tagged_with"
    DOCUMENTS ||--o{ DOCUMENT_SIGNATURES : "signed_by"
    DOCUMENT_SIGNATURES ||--o{ SIGNATURE_REQUESTS : "requested_via"
    DOCUMENTS ||--o{ DOCUMENT_VERSIONS : "versioned_as"
    DOCUMENTS ||--o{ DOCUMENT_METADATA : "described_by"
    DOCUMENTS ||--o{ DOCUMENT_PERMISSIONS : "secured_by"
    FOLDERS ||--o{ DOCUMENTS : "contains"
    FOLDERS ||--o{ FOLDERS : "parent_of"
```

### Technology Stack

- **Runtime**: Java 21+
- **Framework**: Spring Boot 3.x with WebFlux
- **Database**: PostgreSQL with R2DBC
- **Build**: Maven 3.6+
- **ECM Integration**: lib-ecm-core

## 🔧 ECM Integration

### Deployment Scenarios

1. **Standalone**: Document metadata management without content storage
2. **Local Storage**: File system-based storage for development
3. **Cloud Storage**: Production-ready cloud storage (S3, Azure Blob)
4. **Full ECM**: Complete ECM functionality with all adapters

### ECM Configuration Example

```yaml
firefly:
  ecm:
    # Provider configuration (handled by lib-ecm-core)
    document-content:
      provider: s3
      s3:
        bucket: firefly-documents
        region: us-east-1
        access-key: ${AWS_ACCESS_KEY}
        secret-key: ${AWS_SECRET_KEY}
    
    signature:
      provider: docusign
      docusign:
        integration-key: ${DOCUSIGN_INTEGRATION_KEY}
        user-id: ${DOCUSIGN_USER_ID}
        account-id: ${DOCUSIGN_ACCOUNT_ID}
        base-path: https://demo.docusign.net/restapi
    
    # Business logic configuration (handled by microservice)
    integration:
      signature:
        custom-message: "Please review and sign this document"
        language: "en"
        expiration-days: 30
      document:
        security-level: "INTERNAL"
        retention-days: 2555
```

## 📚 Documentation

Comprehensive documentation is available in the [`docs/`](./docs/) directory:

- **[API Reference](./docs/api-reference.md)** - Complete REST API documentation with verified endpoints
- **[Architecture Guide](./docs/architecture.md)** - System architecture and ECM integration patterns
- **[Configuration Guide](./docs/configuration.md)** - Configuration options for all deployment scenarios
- **[Integration Overview](./docs/integration-overview.md)** - Complete lib-ecm-core integration guide
- **[Integration Examples](./docs/integration-examples.md)** - Practical code examples and use cases

## 🛠️ Development

### Running Tests

```bash
mvn test
```

### Building Docker Image

```bash
mvn clean package -DskipTests
docker build -t firefly/document-mgmt:latest .
```

### API Documentation

Quick reference of newly exposed endpoints (all responses are reactive streams or JSON objects):

- GET /api/v1/documents/search?q={query}&limit={n}
- GET /api/v1/documents/search/name?pattern={wildcard}&limit={n}
- GET /api/v1/documents/search/tags?tags=tag1,tag2&matchAll=true&limit={n}
- POST /api/v1/documents/search/advanced  (body: DocumentSearchCriteria JSON)
- GET /api/v1/documents/{documentId}/permissions/check?principalId={uuid}&permission={READ|WRITE|...}

Example requests:

```bash
# Full‑text search
curl -s "http://localhost:8080/api/v1/documents/search?q=contract&limit=10"

# Name pattern search
curl -s "http://localhost:8080/api/v1/documents/search/name?pattern=contract*.pdf&limit=5"

# Tags search (match all)
curl -s "http://localhost:8080/api/v1/documents/search/tags?tags=legal,contract&matchAll=true"

# Advanced search
curl -s -X POST "http://localhost:8080/api/v1/documents/search/advanced" \
  -H "Content-Type: application/json" \
  -d '{
        "query":"nda",
        "mimeType":"application/pdf",
        "minSize":1024,
        "tags":["legal"],
        "limit":20
      }'

# Permission check
curl -s "http://localhost:8080/api/v1/documents/{documentId}/permissions/check?principalId={principalId}&permission=READ"
```

When running locally, API documentation is available at:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/v3/api-docs

## 🔍 Health Checks

The service provides comprehensive health checks:

- **Overall Health**: `/actuator/health`
- **Database**: `/actuator/health/db`
- **ECM Integration**: `/actuator/health/ecm`

## 🤝 Contributing

We welcome contributions to the Firefly OpenCore Platform! Please see our [Contributing Guidelines](CONTRIBUTING.md) for details on how to submit pull requests, report issues, and contribute to the project.

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## 📞 Support

- **Documentation**: [docs/](./docs/)
- **Issues**: [GitHub Issues](https://github.com/firefly-oss/common-platform-document-mgmt/issues)
- **Community**: [Firefly Community](https://community.getfirefly.io)
- **Email**: dev@getfirefly.io

---

**Firefly OpenCore Platform** - Building the future of open-source core banking solutions.  
Visit us at [getfirefly.io](https://getfirefly.io) to learn more about our comprehensive financial technology platform.
