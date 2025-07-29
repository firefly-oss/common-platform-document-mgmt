# Common Platform Document Management - gRPC Storage Module

This module provides a gRPC service implementation for storage operations, allowing remote clients to interact with the document management system's storage functionality.

## Table of Contents

- [Overview](#overview)
- [Hexagonal Architecture](#hexagonal-architecture)
- [gRPC Service](#grpc-service)
- [Two-Bucket System](#two-bucket-system)
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

- Uploading files directly to private bucket (streaming)
- Uploading files from channel to public bucket (streaming)
- Moving files from public bucket to private bucket
- Downloading files
- Deleting files
- Checking if files exist
- Generating pre-signed URLs

Here's a simplified version of the service definition:

```protobuf
syntax = "proto3";

service StorageService {
  // Upload a file directly to private bucket
  rpc UploadFile (stream FileUploadRequest) returns (FileUploadResponse);

  // Upload a file from channel to public bucket
  rpc UploadFileFromChannel (stream FileUploadRequest) returns (FileUploadResponse);

  // Move a file from public bucket to private bucket
  rpc MoveFileFromPublicToPrivate (MoveFileRequest) returns (MoveFileResponse);

  // Download a file
  rpc DownloadFile (FileDownloadRequest) returns (stream FileDownloadResponse);

  // Delete a file
  rpc DeleteFile (FileDeleteRequest) returns (FileDeleteResponse);

  // Check if a file exists
  rpc FileExists (FileExistsRequest) returns (FileExistsResponse);

  // Generate a pre-signed URL
  rpc GeneratePresignedUrl (PresignedUrlRequest) returns (PresignedUrlResponse);
}

message FileMetadata {
  string file_name = 1;
  string content_type = 2;
  string path = 3;
  string provider_name = 4; // Optional, uses default if not provided
  UploadSource source = 5; // Source of the upload (INTERNAL or CHANNEL)
}

// Source of the upload
enum UploadSource {
  INTERNAL = 0; // Default - upload from internal system
  CHANNEL = 1;  // Upload from channel
}

message MoveFileRequest {
  string public_file_url = 1;
  string provider_name = 2; // Optional, uses default if not provided
}

message MoveFileResponse {
  string private_file_url = 1;
}
```

## Two-Bucket System

The gRPC service supports the two-bucket storage system to optimize file uploads and improve security:

1. **Private Bucket**: The main storage bucket for all documents, not directly accessible from outside the system
2. **Public Bucket**: A temporary storage bucket for files uploaded from external channels

The workflow for file uploads depends on the source:

- **Internal Uploads**: Files uploaded from internal systems using the `UploadFile` method go directly to the private bucket
- **Channel Uploads**: Files uploaded from external channels using the `UploadFileFromChannel` method go to the public bucket first, then are moved to the private bucket after validation using the `MoveFileFromPublicToPrivate` method

This approach provides several benefits:
- **Reduced Network Traffic**: Files uploaded from external channels don't need to pass through multiple network layers
- **Improved Security**: The private bucket is not directly accessible from outside the system
- **Better User Experience**: Direct uploads to the public bucket are faster and more reliable

## Implementation Details

The `GrpcStorageServiceImpl` class implements the generated gRPC service interface:

```java
@GrpcService
@RequiredArgsConstructor
public class GrpcStorageServiceImpl extends StorageServiceGrpc.StorageServiceImplBase {
    private final StorageService storageService;

    @Override
    public StreamObserver<FileUploadRequest> uploadFile(StreamObserver<FileUploadResponse> responseObserver) {
        // Implementation for uploading files directly to private bucket
    }

    @Override
    public StreamObserver<FileUploadRequest> uploadFileFromChannel(StreamObserver<FileUploadResponse> responseObserver) {
        // Implementation for uploading files from channel to public bucket
    }

    @Override
    public void moveFileFromPublicToPrivate(MoveFileRequest request, StreamObserver<MoveFileResponse> responseObserver) {
        // Implementation for moving files from public bucket to private bucket
    }

    @Override
    public void downloadFile(FileDownloadRequest request, StreamObserver<FileDownloadResponse> responseObserver) {
        // Implementation for downloading files
    }

    @Override
    public void deleteFile(FileDeleteRequest request, StreamObserver<FileDeleteResponse> responseObserver) {
        // Implementation for deleting files
    }

    @Override
    public void fileExists(FileExistsRequest request, StreamObserver<FileExistsResponse> responseObserver) {
        // Implementation for checking if files exist
    }

    @Override
    public void generatePresignedUrl(PresignedUrlRequest request, StreamObserver<PresignedUrlResponse> responseObserver) {
        // Implementation for generating pre-signed URLs
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

Remote clients can use the generated gRPC client to interact with the storage service. Here's an example of how a client might upload a file from a channel:

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
        System.out.println("File uploaded to public bucket: " + response.getFileUrl());
        
        // Move the file from public to private bucket
        MoveFileRequest moveRequest = MoveFileRequest.newBuilder()
            .setPublicFileUrl(response.getFileUrl())
            .build();
            
        stub.moveFileFromPublicToPrivate(moveRequest, new StreamObserver<MoveFileResponse>() {
            @Override
            public void onNext(MoveFileResponse moveResponse) {
                System.out.println("File moved to private bucket: " + moveResponse.getPrivateFileUrl());
            }
            
            @Override
            public void onError(Throwable t) {
                System.err.println("Error moving file: " + t.getMessage());
            }
            
            @Override
            public void onCompleted() {
                System.out.println("Move operation completed");
            }
        });
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

// Create a request observer for channel upload
StreamObserver<FileUploadRequest> requestObserver = stub.uploadFileFromChannel(responseObserver);

// Send metadata with CHANNEL source
FileMetadata metadata = FileMetadata.newBuilder()
    .setFileName("example.txt")
    .setContentType("text/plain")
    .setPath("/documents")
    .setSource(UploadSource.CHANNEL)
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