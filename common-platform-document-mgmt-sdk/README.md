# Common Platform Document Management - SDK Module

This module provides the Software Development Kit (SDK) components for integrating with the document management system, including API specifications and client libraries.

## Table of Contents

- [Overview](#overview)
- [Hexagonal Architecture](#hexagonal-architecture)
- [API Specification](#api-specification)
- [Client Libraries](#client-libraries)
- [Usage](#usage)

## Overview

The SDK module provides the tools and specifications needed for external systems to integrate with the document management system. It includes the OpenAPI specification that defines the REST API, as well as client libraries that simplify integration for different programming languages.

## Hexagonal Architecture

In the hexagonal architecture pattern, the SDK module serves as an adapter for external systems:

- It defines the API contract that external systems can use to interact with the application
- It provides client libraries that implement this contract, making it easier for external systems to integrate
- It acts as a bridge between the core domain and external consumers

This approach provides several benefits:
- **Consistency**: The API contract is defined in a single place, ensuring consistency across all integrations
- **Simplicity**: Client libraries abstract away the details of HTTP communication, making integration simpler
- **Versioning**: The API can evolve in a controlled manner, with clear versioning and backward compatibility

## API Specification

The module includes an OpenAPI (formerly Swagger) specification that defines the REST API:

```yaml
openapi: 3.0.3
info:
  title: Document Management API
  description: API for managing enterprise documents, including versioning, tagging, references, and digital signatures
  version: 1.0.0
servers:
  - url: /api/v1
paths:
  /documents:
    get:
      summary: Get all documents
      # ...
    post:
      summary: Create a new document
      # ...
  /documents/{id}:
    get:
      summary: Get a document by ID
      # ...
    put:
      summary: Update a document
      # ...
    delete:
      summary: Delete a document
      # ...
  # Additional paths for document versions, tags, references, signatures, etc.
```

The OpenAPI specification is located at `src/main/resources/api-spec/openapi.yml` and includes:

- Endpoint definitions for all API operations
- Request and response schemas
- Authentication requirements
- Error responses
- Examples and documentation

This specification can be used to:
- Generate client libraries for different programming languages
- Create interactive API documentation
- Set up automated testing
- Configure API gateways and proxies

## Client Libraries

The module includes client libraries for different programming languages:

- **Java Client**: A Java client library that uses Spring WebClient for reactive API calls
- **TypeScript Client**: A TypeScript client library for web applications
- **Python Client**: A Python client library for data processing applications

Example of the Java client:

```java
public class DocumentManagementClient {
    private final WebClient webClient;
    
    public DocumentManagementClient(String baseUrl, String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    
    public Mono<DocumentDTO> getDocument(Long id) {
        return webClient.get()
                .uri("/documents/{id}", id)
                .retrieve()
                .bodyToMono(DocumentDTO.class);
    }
    
    public Flux<DocumentDTO> getAllDocuments() {
        return webClient.get()
                .uri("/documents")
                .retrieve()
                .bodyToFlux(DocumentDTO.class);
    }
    
    public Mono<DocumentDTO> createDocument(DocumentDTO document) {
        return webClient.post()
                .uri("/documents")
                .bodyValue(document)
                .retrieve()
                .bodyToMono(DocumentDTO.class);
    }
    
    // Additional methods for other API operations
}
```

The client libraries are generated from the OpenAPI specification using tools like OpenAPI Generator, ensuring that they are always in sync with the API definition.

## Usage

### Java Client

```java
// Create a client
DocumentManagementClient client = new DocumentManagementClient(
    "https://api.example.com",
    "your-api-key"
);

// Get a document
client.getDocument(123L)
    .subscribe(document -> {
        System.out.println("Document title: " + document.getTitle());
    });

// Create a document
DocumentDTO newDocument = DocumentDTO.builder()
    .title("New Document")
    .description("This is a new document")
    .ownerDepartment("IT")
    .documentTypeId(1L)
    .build();

client.createDocument(newDocument)
    .subscribe(createdDocument -> {
        System.out.println("Created document with ID: " + createdDocument.getId());
    });
```

### TypeScript Client

```typescript
// Create a client
const client = new DocumentManagementClient({
    baseUrl: 'https://api.example.com',
    apiKey: 'your-api-key'
});

// Get a document
client.getDocument(123)
    .then(document => {
        console.log(`Document title: ${document.title}`);
    });

// Create a document
const newDocument = {
    title: 'New Document',
    description: 'This is a new document',
    ownerDepartment: 'IT',
    documentTypeId: 1
};

client.createDocument(newDocument)
    .then(createdDocument => {
        console.log(`Created document with ID: ${createdDocument.id}`);
    });
```

### Python Client

```python
# Create a client
client = DocumentManagementClient(
    base_url='https://api.example.com',
    api_key='your-api-key'
)

# Get a document
document = client.get_document(123)
print(f"Document title: {document.title}")

# Create a document
new_document = {
    'title': 'New Document',
    'description': 'This is a new document',
    'ownerDepartment': 'IT',
    'documentTypeId': 1
}

created_document = client.create_document(new_document)
print(f"Created document with ID: {created_document.id}")
```

By providing these client libraries, the SDK module makes it easy for external systems to integrate with the document management system, regardless of the programming language they use.