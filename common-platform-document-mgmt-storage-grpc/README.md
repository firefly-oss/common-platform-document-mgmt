# Common Platform Document Management - gRPC Storage Module

This module provides a gRPC service implementation for storage operations, allowing remote clients to interact with the document management system's storage functionality.

## Table of Contents

- [Overview](#overview)
- [Hexagonal Architecture](#hexagonal-architecture)
- [gRPC Service](#grpc-service)
- [Implementation Details](#implementation-details)
- [Configuration](#configuration)
- [Client Usage](#client-usage)
- [Security](#security)

## Overview

The gRPC Storage module exposes the document management system's storage functionality as a gRPC service, allowing remote clients to perform storage operations like uploading, downloading, and deleting files without having to implement the `FileStorageProvider` interface directly.

## Hexagonal Architecture

In the hexagonal architecture pattern, this module serves a different role than the S3 storage module:

- The S3 storage module is an adapter that implements the `FileStorageProvider` port to connect the core domain to Amazon S3
- The gRPC storage module is an adapter that exposes the core domain's storage functionality to external clients via gRPC

Rather than implementing the `FileStorageProvider` interface, the gRPC service uses the core `StorageService`, which in turn uses the registered `FileStorageProvider` implementations. This allows the gRPC service to leverage the same storage provider infrastructure as the rest of the application.

This approach has several benefits:
- Remote clients don't need to implement the `FileStorageProvider` interface
- The gRPC service can use any registered storage provider, including the S3 provider
- Storage provider selection can be centralized in the core domain

## gRPC Service

The gRPC service is defined in a Protocol Buffers (protobuf) file, which generates the service interface and message classes. The service definition includes operations for:

- Uploading files (streaming)
- Downloading files
- Deleting files
- Checking if files exist
- Generating pre-signed URLs

Here's a simplified version of the service definition:

```protobuf
syntax = "proto3";

service StorageService {
  rpc uploadFile(stream FileUploadRequest) returns (FileUploadResponse);
  rpc downloadFile(FileDownloadRequest) returns (stream FileDownloadResponse);
  rpc deleteFile(FileDeleteRequest) returns (FileDeleteResponse);
  rpc fileExists(FileExistsRequest) returns (FileExistsResponse);
  rpc generatePresignedUrl(PresignedUrlRequest) returns (PresignedUrlResponse);
}
```

## Implementation Details

The `GrpcStorageServiceImpl` class implements the generated gRPC service interface:

```java
@GrpcService
@RequiredArgsConstructor
public class GrpcStorageServiceImpl extends StorageServiceGrpc.StorageServiceImplBase {
    private final StorageService storageService;

    @Override
    public StreamObserver<FileUploadRequest> uploadFile(StreamObserver<FileUploadResponse> responseObserver) {
        // Implementation
    }

    @Override
    public void downloadFile(FileDownloadRequest request, StreamObserver<FileDownloadResponse> responseObserver) {
        // Implementation
    }

    @Override
    public void deleteFile(FileDeleteRequest request, StreamObserver<FileDeleteResponse> responseObserver) {
        // Implementation
    }

    @Override
    public void fileExists(FileExistsRequest request, StreamObserver<FileExistsResponse> responseObserver) {
        // Implementation
    }

    @Override
    public void generatePresignedUrl(PresignedUrlRequest request, StreamObserver<PresignedUrlResponse> responseObserver) {
        // Implementation
    }
}
```

The implementation delegates to the core `StorageService`, which uses the registered `FileStorageProvider` implementations to perform the actual storage operations. This allows the gRPC service to leverage the same storage provider infrastructure as the rest of the application.

## Configuration

The gRPC service is configured using the `GrpcConfig` class:

```java
@Configuration
public class GrpcConfig {
    @Value("${grpc.server.security.enabled:false}")
    private boolean securityEnabled;

    @Bean
    public GrpcServerConfigurer grpcServerConfigurer() {
        return serverBuilder -> {
            serverBuilder.maxInboundMessageSize(10 * 1024 * 1024); // 10MB
        };
    }

    @Bean
    public GrpcAuthenticationReader grpcAuthenticationReader() {
        if (securityEnabled) {
            return new BasicGrpcAuthenticationReader();
        }
        return null;
    }
}
```

This configuration sets up the gRPC server with:
- A maximum inbound message size of 10MB
- Optional basic authentication if security is enabled

Additional configuration can be provided in the application properties:

```yaml
grpc:
  server:
    port: 9090
    security:
      enabled: true
```

## Client Usage

Remote clients can use the generated gRPC client to interact with the storage service. Here's an example of how a client might upload a file:

```java
// Create a gRPC channel
ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
    .usePlaintext()
    .build();

// Create a stub
StorageServiceGrpc.StorageServiceStub stub = StorageServiceGrpc.newStub(channel);

// Create a response observer
StreamObserver<FileUploadResponse> responseObserver = new StreamObserver<FileUploadResponse>() {
    @Override
    public void onNext(FileUploadResponse response) {
        System.out.println("File uploaded: " + response.getFileUrl());
    }

    @Override
    public void onError(Throwable t) {
        System.err.println("Error uploading file: " + t.getMessage());
    }

    @Override
    public void onCompleted() {
        System.out.println("Upload completed");
    }
};

// Create a request observer
StreamObserver<FileUploadRequest> requestObserver = stub.uploadFile(responseObserver);

// Send metadata
FileMetadata metadata = FileMetadata.newBuilder()
    .setFileName("example.txt")
    .setContentType("text/plain")
    .setPath("/documents")
    .build();

requestObserver.onNext(FileUploadRequest.newBuilder()
    .setMetadata(metadata)
    .build());

// Send file chunks
byte[] data = "Hello, World!".getBytes();
requestObserver.onNext(FileUploadRequest.newBuilder()
    .setChunk(ByteString.copyFrom(data))
    .build());

// Complete the request
requestObserver.onCompleted();
```

## Security

The gRPC service can be secured using basic authentication if enabled in the configuration. This requires clients to provide credentials in the gRPC metadata:

```java
// Create credentials
String credentials = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);

// Create a gRPC channel with credentials
ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
    .usePlaintext()
    .build();

// Create a stub with credentials
StorageServiceGrpc.StorageServiceStub stub = StorageServiceGrpc.newStub(channel)
    .withCallCredentials(new CallCredentials() {
        @Override
        public void applyRequestMetadata(RequestInfo requestInfo, Executor appExecutor, MetadataApplier applier) {
            Metadata metadata = new Metadata();
            metadata.put(credentials, "Basic " + Base64.getEncoder().encodeToString("username:password".getBytes()));
            applier.apply(metadata);
        }

        @Override
        public void thisUsesUnstableApi() {
            // Required by interface
        }
    });
```

For production environments, it is recommended to use TLS/SSL encryption and more robust authentication mechanisms, such as OAuth2 or JWT tokens.