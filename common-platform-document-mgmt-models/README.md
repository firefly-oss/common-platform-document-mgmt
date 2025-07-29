# Common Platform Document Management - Models Module

This module contains the domain entities, repositories, and database-related components for the document management system.

## Table of Contents

- [Overview](#overview)
- [Hexagonal Architecture](#hexagonal-architecture)
- [Domain Entities](#domain-entities)
- [Repositories](#repositories)
- [Database Configuration](#database-configuration)

## Overview

The Models module defines the core domain entities and data access layer for the document management system. It contains the JPA entities that represent the domain objects in the database, as well as the Spring Data repositories that provide data access operations.

## Hexagonal Architecture

In the hexagonal architecture pattern, the models module represents the innermost layer of the application:

- It defines the domain entities that encapsulate the core business concepts
- It provides repositories that act as ports for data persistence
- It isolates the domain model from the specific database technology

This separation provides several benefits:
- **Domain Focus**: The domain entities can focus on business rules without being contaminated by persistence concerns
- **Testability**: The domain model can be tested independently of the database
- **Flexibility**: The database technology can be changed without affecting the core business logic

## Domain Entities

The module includes entities for various domain concepts:

- **Document**: Represents a document in the system
- **DocumentVersion**: Represents a version of a document
- **DocumentStatus**: Represents the status of a document
- **DocumentType**: Represents the type of a document
- **DocumentTag**: Represents a tag associated with a document
- **Tag**: Represents a tag that can be applied to documents
- **DocumentReference**: Represents a reference between documents
- **ReferenceType**: Represents the type of reference between documents
- **SignatureRequest**: Represents a request for document signatures
- **SignatureRequestStatus**: Represents the status of a signature request
- **SignatureSigner**: Represents a signer in a signature request
- **SignatureProof**: Represents a proof of signature
- **ProofType**: Represents the type of signature proof
- **SignatureStatus**: Represents the status of a signature
- **NotificationType**: Represents the type of notification
- **NotificationLog**: Represents a log of notifications sent
- **File**: Represents a file in the system

Example of an entity:

```java
@Entity
@Table(name = "documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    private String description;
    
    @Column(name = "owner_department")
    private String ownerDepartment;
    
    @ManyToOne
    @JoinColumn(name = "status_id")
    private DocumentStatus status;
    
    @ManyToOne
    @JoinColumn(name = "document_type_id")
    private DocumentType documentType;
    
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    
    @Column(name = "date_created")
    private LocalDateTime dateCreated;
    
    @Column(name = "date_updated")
    private LocalDateTime dateUpdated;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "updated_by")
    private String updatedBy;
    
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentVersion> versions = new ArrayList<>();
    
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentTag> tags = new ArrayList<>();
    
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentReference> references = new ArrayList<>();
    
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SignatureRequest> signatureRequests = new ArrayList<>();
}
```

## Repositories

The module includes Spring Data repositories for each entity, providing data access operations:

- **DocumentRepository**: Operations for documents
- **DocumentVersionRepository**: Operations for document versions
- **DocumentStatusRepository**: Operations for document statuses
- **DocumentTypeRepository**: Operations for document types
- **DocumentTagRepository**: Operations for document tags
- **TagRepository**: Operations for tags
- **DocumentReferenceRepository**: Operations for document references
- **ReferenceTypeRepository**: Operations for reference types
- **SignatureRequestRepository**: Operations for signature requests
- **SignatureRequestStatusRepository**: Operations for signature request statuses
- **SignatureSignerRepository**: Operations for signature signers
- **SignatureProofRepository**: Operations for signature proofs
- **ProofTypeRepository**: Operations for proof types
- **SignatureStatusRepository**: Operations for signature statuses
- **NotificationTypeRepository**: Operations for notification types
- **NotificationLogRepository**: Operations for notification logs
- **FileRepository**: Operations for files

Example of a repository:

```java
@Repository
public interface DocumentRepository extends ReactiveCrudRepository<Document, Long> {
    Flux<Document> findByDocumentTypeId(Long documentTypeId);
    Flux<Document> findByStatusId(Long statusId);
    Flux<Document> findByOwnerDepartment(String ownerDepartment);
    Mono<Long> countByDocumentTypeId(Long documentTypeId);
}
```

The repositories use Spring Data's reactive support (R2DBC) to provide non-blocking, asynchronous data access operations.

## Database Configuration

The module includes configuration for the database connection and schema management:

- **R2dbcConfiguration**: Configures the R2DBC connection factory and database client
- **DatabaseInitializer**: Initializes the database schema and loads initial data
- **AuditingConfiguration**: Configures auditing for entity creation and modification timestamps

Example of database configuration:

```java
@Configuration
@EnableR2dbcAuditing
public class R2dbcConfiguration {
    @Bean
    public ConnectionFactory connectionFactory(
            @Value("${spring.r2dbc.url}") String url,
            @Value("${spring.r2dbc.username}") String username,
            @Value("${spring.r2dbc.password}") String password) {
        return ConnectionFactories.get(
                ConnectionFactoryOptions.builder()
                        .option(DRIVER, "postgresql")
                        .option(HOST, extractHost(url))
                        .option(PORT, extractPort(url))
                        .option(DATABASE, extractDatabase(url))
                        .option(USER, username)
                        .option(PASSWORD, password)
                        .build());
    }
    
    @Bean
    public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionFactory connectionFactory) {
        return new R2dbcEntityTemplate(connectionFactory);
    }
    
    // Helper methods for extracting connection details
}
```

The database configuration is designed to be flexible, allowing the application to work with different database technologies by changing the configuration.