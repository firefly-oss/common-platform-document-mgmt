# Common Platform Document Management - S3 Storage Module

This module provides an implementation of the `FileStorageProvider` interface for Amazon S3 storage.

## Table of Contents

- [Overview](#overview)
- [Hexagonal Architecture](#hexagonal-architecture)
- [Implementation Details](#implementation-details)
- [Configuration](#configuration)
- [Usage](#usage)
- [Error Handling](#error-handling)

## Overview

The S3 Storage module is an adapter in the hexagonal architecture that implements the `FileStorageProvider` interface to store and retrieve files using Amazon S3. This module allows the document management system to use Amazon S3 as a storage backend without modifying the core business logic.

## Hexagonal Architecture

In the hexagonal architecture pattern:
- The `FileStorageProvider` interface in the core module defines the port for file storage operations
- The `S3StorageProviderImpl` class in this module is an adapter that implements the port for Amazon S3
- The adapter is registered with the `FileStorageProviderRegistry` in the core module, allowing the application to use it dynamically

This separation of concerns allows the core business logic to remain independent of the specific storage implementation, making it easier to add or replace storage providers without affecting the rest of the application.

## Implementation Details

The `S3StorageProviderImpl` class implements all methods defined in the `FileStorageProvider` interface:

```java
@Component
public class S3StorageProviderImpl implements FileStorageProvider {
    @Override
    public Mono<String> uploadFile(FilePart filePart, String path);
    
    @Override
    public Mono<String> uploadContent(Flux<DataBuffer> content, String fileName, String contentType, String path);
    
    @Override
    public Flux<DataBuffer> downloadFile(String fileUrl);
    
    @Override
    public Mono<Void> deleteFile(String fileUrl);
    
    @Override
    public Mono<Boolean> fileExists(String fileUrl);
    
    @Override
    public Mono<String> generatePresignedUrl(String fileUrl, long expirationInSeconds);
    
    @Override
    public String getProviderName();
}
```

The implementation uses the AWS SDK for Java to interact with Amazon S3, providing the following functionality:

- **File Upload**: Uploads files to S3 buckets, generating unique keys based on the path and filename
- **Content Upload**: Uploads content streams to S3 buckets
- **File Download**: Downloads files from S3 buckets as reactive streams
- **File Deletion**: Deletes files from S3 buckets
- **File Existence Check**: Checks if files exist in S3 buckets
- **Pre-signed URL Generation**: Generates pre-signed URLs for temporary access to files

The implementation is reactive, using Project Reactor to provide non-blocking, asynchronous operations.

## Configuration

The S3 storage provider is configured using the following properties in the application configuration:

```yaml
storage:
  s3:
    endpoint: https://s3.amazonaws.com
    region: us-east-1
    access-key: your-access-key
    secret-key: your-secret-key
    bucket-name: your-bucket-name
```

These properties are injected into the `S3StorageProviderImpl` constructor:

```java
public S3StorageProviderImpl(
        @Value("${storage.s3.endpoint}") String endpoint,
        @Value("${storage.s3.region}") String region,
        @Value("${storage.s3.access-key}") String accessKey,
        @Value("${storage.s3.secret-key}") String secretKey,
        @Value("${storage.s3.bucket-name}") String bucketName) {
    // ...
}
```

## Usage

The S3 storage provider is automatically registered with the `FileStorageProviderRegistry` when the application starts. It can be used by injecting the registry and requesting the provider by name:

```java
@Service
public class MyService {
    private final FileStorageProviderRegistry providerRegistry;
    
    public MyService(FileStorageProviderRegistry providerRegistry) {
        this.providerRegistry = providerRegistry;
    }
    
    public Mono<String> uploadFile(FilePart filePart, String path) {
        FileStorageProvider s3Provider = providerRegistry.getProvider("AmazonS3");
        return s3Provider.uploadFile(filePart, path);
    }
}
```

Alternatively, the default provider can be used if S3 is configured as the default:

```java
FileStorageProvider defaultProvider = providerRegistry.getDefaultProvider();
return defaultProvider.uploadFile(filePart, path);
```

## Error Handling

The S3 storage provider handles various error scenarios:

- **Authentication Errors**: If the AWS credentials are invalid, the provider will throw an exception
- **Bucket Not Found**: If the configured bucket does not exist, the provider will throw an exception
- **File Not Found**: If a requested file does not exist, the provider will return an empty Flux or Mono
- **Permission Errors**: If the AWS credentials do not have permission to perform an operation, the provider will throw an exception
- **Network Errors**: If there are network issues when communicating with S3, the provider will throw an exception

All errors are logged and propagated as reactive errors, allowing the calling code to handle them using standard reactive error handling mechanisms.