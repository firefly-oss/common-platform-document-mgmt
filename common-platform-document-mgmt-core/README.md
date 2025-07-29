# Common Platform Document Management - Core Module

This module contains the core business logic, services, and provider interfaces (ports) for the document management system.

## Table of Contents

- [Overview](#overview)
- [Hexagonal Architecture](#hexagonal-architecture)
- [Provider Interfaces](#provider-interfaces)
  - [FileStorageProvider](#filestorageprovider)
  - [ESignatureProvider](#esignatureprovider)
- [Provider Registries](#provider-registries)
  - [FileStorageProviderRegistry](#filestorageproviderregistry)
  - [ESignatureProviderRegistry](#esignatureproviderregistry)
- [Core Services](#core-services)

## Overview

The core module is the heart of the document management system, containing the business logic and defining the interfaces (ports) for interacting with external systems. It follows the hexagonal architecture pattern, which separates the core business logic from external dependencies.

## Hexagonal Architecture

In the hexagonal architecture pattern, the core module defines the interfaces (ports) that external systems must implement to interact with the core business logic. These interfaces are then implemented by adapters in other modules.

The core module contains:
- **Ports**: Interfaces that define how the core domain interacts with the outside world
- **Services**: Core business logic that uses the ports to interact with external systems
- **Domain Models**: Business entities and value objects

This architecture provides several benefits:
- **Modularity**: Each component has a clear responsibility and can be developed and tested independently
- **Flexibility**: Different implementations of the same port can be swapped without affecting the core business logic
- **Testability**: The core business logic can be tested without depending on external systems

## Provider Interfaces

The core module defines two main provider interfaces:

### FileStorageProvider

The `FileStorageProvider` interface defines the contract for file storage operations:

```java
public interface FileStorageProvider {
    Mono<String> uploadFile(FilePart filePart, String path);
    Mono<String> uploadContent(Flux<DataBuffer> content, String fileName, String contentType, String path);
    Flux<DataBuffer> downloadFile(String fileUrl);
    Mono<Void> deleteFile(String fileUrl);
    Mono<Boolean> fileExists(String fileUrl);
    Mono<String> generatePresignedUrl(String fileUrl, long expirationInSeconds);
    String getProviderName();
}
```

This interface is implemented by storage adapters like the S3 storage provider.

### ESignatureProvider

The `ESignatureProvider` interface defines the contract for e-signature operations:

```java
public interface ESignatureProvider {
    Mono<SignatureRequestDTO> initiateSignatureRequest(SignatureRequestDTO signatureRequest);
    Mono<SignatureRequestDTO> getSignatureRequestStatus(Long signatureRequestId, String externalSignatureId);
    Mono<Void> cancelSignatureRequest(Long signatureRequestId, String externalSignatureId);
    Mono<SignatureProofDTO> getSignatureProof(Long signatureRequestId, String externalSignatureId);
    Mono<Boolean> validateSignatureProof(SignatureProofDTO signatureProof);
    String getProviderName();
}
```

This interface is implemented by e-signature adapters like the Logalty provider.

## Provider Registries

The core module also includes registries for managing provider implementations:

### FileStorageProviderRegistry

The `FileStorageProviderRegistry` manages the available file storage providers and provides methods to retrieve them:

```java
@Component
public class FileStorageProviderRegistry {
    public FileStorageProvider getDefaultProvider();
    public FileStorageProvider getProvider(String providerName);
    public boolean hasProvider(String providerName);
    public List<String> getProviderNames();
}
```

This registry allows the application to dynamically select which storage provider to use at runtime.

### ESignatureProviderRegistry

The `ESignatureProviderRegistry` manages the available e-signature providers and provides methods to retrieve them:

```java
@Component
public class ESignatureProviderRegistry {
    public ESignatureProvider getDefaultProvider();
    public ESignatureProvider getProvider(String providerName);
    public boolean hasProvider(String providerName);
    public List<String> getProviderNames();
}
```

This registry allows the application to dynamically select which e-signature provider to use at runtime.

## Core Services

The core module includes several services that implement the business logic of the document management system:

- **DocumentService**: Manages document metadata and operations
- **DocumentVersionService**: Manages document versions
- **DocumentTagService**: Manages document tags
- **DocumentReferenceService**: Manages document references
- **SignatureRequestService**: Manages signature requests
- **SignatureProofService**: Manages signature proofs
- **StorageService**: Manages file storage operations using the FileStorageProvider
- **NotificationService**: Manages notifications for document events

These services use the provider interfaces to interact with external systems, allowing the business logic to remain independent of the specific implementations.