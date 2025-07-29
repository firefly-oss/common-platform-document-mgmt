# Common Platform Document Management - Interfaces Module

This module contains the interfaces and Data Transfer Objects (DTOs) used for communication between the core domain and external systems.

## Table of Contents

- [Overview](#overview)
- [Hexagonal Architecture](#hexagonal-architecture)
- [Data Transfer Objects](#data-transfer-objects)
- [Usage](#usage)

## Overview

The Interfaces module defines the contract for communication between the core domain and external systems. It contains Data Transfer Objects (DTOs) that represent the data structures used in these communications, ensuring a clean separation between the internal domain models and the external representation of data.

## Hexagonal Architecture

In the hexagonal architecture pattern, the interfaces module plays a crucial role:

- It defines the DTOs that are used to transfer data across the boundaries of the hexagonal architecture
- It provides a clear contract for communication between the core domain and external systems
- It helps maintain the separation of concerns by ensuring that domain models are not exposed directly to external systems

This separation provides several benefits:
- **Isolation**: The core domain is isolated from changes in the external representation of data
- **Flexibility**: The external representation can evolve independently of the internal domain models
- **Security**: Sensitive information in domain models can be filtered out in the DTOs

## Data Transfer Objects

The module includes DTOs for various entities in the system:

- **DocumentDTO**: Represents a document in the system
- **DocumentVersionDTO**: Represents a version of a document
- **DocumentTagDTO**: Represents a tag associated with a document
- **DocumentReferenceDTO**: Represents a reference between documents
- **SignatureRequestDTO**: Represents a request for document signatures
- **SignatureProofDTO**: Represents a proof of signature
- **FileDTO**: Represents a file in the system

Example of a DTO:

```java
public class DocumentDTO {
    private Long id;
    private String title;
    private String description;
    private String ownerDepartment;
    private Long statusId;
    private Long documentTypeId;
    private LocalDateTime creationDate;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    private String createdBy;
    private String updatedBy;
    
    // Getters and setters
    // Builder pattern methods
}
```

The DTOs are designed to be:
- **Immutable**: Once created, they cannot be modified
- **Serializable**: They can be easily converted to and from JSON or other formats
- **Validated**: They include validation annotations to ensure data integrity

## Usage

The DTOs in this module are used throughout the application for communication between layers:

1. **API Layer**: The web controllers use these DTOs to receive requests and send responses
2. **Service Layer**: The core services use these DTOs to communicate with external systems
3. **Provider Interfaces**: The provider interfaces use these DTOs to define the contract for external providers

Example of using a DTO in a controller:

```java
@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {
    private final DocumentService documentService;
    
    @PostMapping
    public Mono<DocumentDTO> createDocument(@RequestBody DocumentDTO documentDTO) {
        return documentService.createDocument(documentDTO);
    }
    
    @GetMapping("/{id}")
    public Mono<DocumentDTO> getDocument(@PathVariable Long id) {
        return documentService.getDocument(id);
    }
}
```

Example of using a DTO in a service:

```java
@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    
    @Override
    public Mono<DocumentDTO> createDocument(DocumentDTO documentDTO) {
        Document document = documentMapper.toEntity(documentDTO);
        return documentRepository.save(document)
                .map(documentMapper::toDto);
    }
    
    @Override
    public Mono<DocumentDTO> getDocument(Long id) {
        return documentRepository.findById(id)
                .map(documentMapper::toDto);
    }
}
```

By using these DTOs consistently throughout the application, we maintain a clean separation between the core domain and external systems, ensuring that the hexagonal architecture remains intact.