package com.catalis.commons.ecm.storage.grpc;

import com.catalis.commons.ecm.core.services.StorageService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Implementation of the gRPC StorageService.
 * This service uses the StorageService to perform file storage operations.
 */
@GrpcService
@RequiredArgsConstructor
public class GrpcStorageServiceImpl extends StorageServiceGrpc.StorageServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(GrpcStorageServiceImpl.class);
    private static final DataBufferFactory BUFFER_FACTORY = new DefaultDataBufferFactory();

    private final StorageService storageService;

    @Override
    public StreamObserver<FileUploadRequest> uploadFile(StreamObserver<FileUploadResponse> responseObserver) {
        log.info("gRPC uploadFile request received");
        
        // Create a sink for the file content
        Sinks.Many<DataBuffer> contentSink = Sinks.many().unicast().onBackpressureBuffer();
        
        // Reference to store metadata
        AtomicReference<FileMetadata> metadataRef = new AtomicReference<>();
        
        return new StreamObserver<FileUploadRequest>() {
            @Override
            public void onNext(FileUploadRequest request) {
                if (request.hasMetadata()) {
                    // Store metadata
                    FileMetadata metadata = request.getMetadata();
                    metadataRef.set(metadata);
                    log.info("Received file metadata: {}", metadata.getFileName());
                } else if (request.hasChunk()) {
                    // Add chunk to content sink
                    ByteBuffer byteBuffer = ByteBuffer.wrap(request.getChunk().toByteArray());
                    DataBuffer dataBuffer = BUFFER_FACTORY.wrap(byteBuffer);
                    contentSink.tryEmitNext(dataBuffer);
                }
            }

            @Override
            public void onError(Throwable t) {
                log.error("Error in uploadFile stream", t);
                contentSink.tryEmitError(t);
                responseObserver.onError(Status.INTERNAL
                        .withDescription("Error processing upload: " + t.getMessage())
                        .asException());
            }

            @Override
            public void onCompleted() {
                log.info("Upload stream completed");
                contentSink.tryEmitComplete();
                
                // Get metadata
                FileMetadata metadata = metadataRef.get();
                if (metadata == null) {
                    responseObserver.onError(Status.INVALID_ARGUMENT
                            .withDescription("No metadata received")
                            .asException());
                    return;
                }
                
                // Upload content
                Flux<DataBuffer> content = contentSink.asFlux();
                Mono<String> result;
                
                if (metadata.getProviderName().isEmpty()) {
                    result = storageService.uploadContent(
                            content,
                            metadata.getFileName(),
                            metadata.getContentType(),
                            metadata.getPath());
                } else {
                    result = storageService.uploadContent(
                            content,
                            metadata.getFileName(),
                            metadata.getContentType(),
                            metadata.getPath(),
                            metadata.getProviderName());
                }
                
                // Process result
                result.subscribe(
                        fileUrl -> {
                            FileUploadResponse response = FileUploadResponse.newBuilder()
                                    .setFileUrl(fileUrl)
                                    .build();
                            responseObserver.onNext(response);
                            responseObserver.onCompleted();
                        },
                        error -> {
                            log.error("Error uploading file", error);
                            responseObserver.onError(Status.INTERNAL
                                    .withDescription("Error uploading file: " + error.getMessage())
                                    .asException());
                        }
                );
            }
        };
    }

    @Override
    public void downloadFile(FileDownloadRequest request, StreamObserver<FileDownloadResponse> responseObserver) {
        log.info("gRPC downloadFile request received for URL: {}", request.getFileUrl());
        
        // Get file content
        Flux<DataBuffer> content;
        if (request.getProviderName().isEmpty()) {
            content = storageService.downloadFile(request.getFileUrl());
        } else {
            content = storageService.downloadFile(request.getFileUrl(), request.getProviderName());
        }
        
        // Send metadata first
        FileMetadata metadata = FileMetadata.newBuilder()
                .setFileName(extractFileName(request.getFileUrl()))
                .setContentType("application/octet-stream") // Default content type
                .build();
        
        FileDownloadResponse metadataResponse = FileDownloadResponse.newBuilder()
                .setMetadata(metadata)
                .build();
        
        responseObserver.onNext(metadataResponse);
        
        // Subscribe to content and send chunks
        content.subscribe(
                dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    
                    FileDownloadResponse chunkResponse = FileDownloadResponse.newBuilder()
                            .setChunk(com.google.protobuf.ByteString.copyFrom(bytes))
                            .build();
                    
                    responseObserver.onNext(chunkResponse);
                },
                error -> {
                    log.error("Error downloading file", error);
                    responseObserver.onError(Status.INTERNAL
                            .withDescription("Error downloading file: " + error.getMessage())
                            .asException());
                },
                () -> {
                    log.info("Download completed");
                    responseObserver.onCompleted();
                }
        );
    }

    @Override
    public void deleteFile(FileDeleteRequest request, StreamObserver<FileDeleteResponse> responseObserver) {
        log.info("gRPC deleteFile request received for URL: {}", request.getFileUrl());
        
        // Delete file
        Mono<Void> result;
        if (request.getProviderName().isEmpty()) {
            result = storageService.deleteFile(request.getFileUrl());
        } else {
            result = storageService.deleteFile(request.getFileUrl(), request.getProviderName());
        }
        
        // Process result
        result.subscribe(
                unused -> {
                    FileDeleteResponse response = FileDeleteResponse.newBuilder()
                            .setSuccess(true)
                            .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                },
                error -> {
                    log.error("Error deleting file", error);
                    responseObserver.onError(Status.INTERNAL
                            .withDescription("Error deleting file: " + error.getMessage())
                            .asException());
                }
        );
    }

    @Override
    public void fileExists(FileExistsRequest request, StreamObserver<FileExistsResponse> responseObserver) {
        log.info("gRPC fileExists request received for URL: {}", request.getFileUrl());
        
        // Check if file exists
        Mono<Boolean> result;
        if (request.getProviderName().isEmpty()) {
            result = storageService.fileExists(request.getFileUrl());
        } else {
            result = storageService.fileExists(request.getFileUrl(), request.getProviderName());
        }
        
        // Process result
        result.subscribe(
                exists -> {
                    FileExistsResponse response = FileExistsResponse.newBuilder()
                            .setExists(exists)
                            .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                },
                error -> {
                    log.error("Error checking if file exists", error);
                    responseObserver.onError(Status.INTERNAL
                            .withDescription("Error checking if file exists: " + error.getMessage())
                            .asException());
                }
        );
    }

    @Override
    public void generatePresignedUrl(PresignedUrlRequest request, StreamObserver<PresignedUrlResponse> responseObserver) {
        log.info("gRPC generatePresignedUrl request received for URL: {}", request.getFileUrl());
        
        // Generate pre-signed URL
        Mono<String> result;
        if (request.getProviderName().isEmpty()) {
            result = storageService.generatePresignedUrl(request.getFileUrl(), request.getExpirationInSeconds());
        } else {
            result = storageService.generatePresignedUrl(request.getFileUrl(), request.getExpirationInSeconds(), request.getProviderName());
        }
        
        // Process result
        result.subscribe(
                presignedUrl -> {
                    PresignedUrlResponse response = PresignedUrlResponse.newBuilder()
                            .setPresignedUrl(presignedUrl)
                            .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                },
                error -> {
                    log.error("Error generating pre-signed URL", error);
                    responseObserver.onError(Status.INTERNAL
                            .withDescription("Error generating pre-signed URL: " + error.getMessage())
                            .asException());
                }
        );
    }
    
    /**
     * Extracts the file name from a URL.
     *
     * @param fileUrl the URL of the file
     * @return the extracted file name
     */
    private String extractFileName(String fileUrl) {
        int lastSlashIndex = fileUrl.lastIndexOf('/');
        if (lastSlashIndex >= 0 && lastSlashIndex < fileUrl.length() - 1) {
            return fileUrl.substring(lastSlashIndex + 1);
        }
        return "file";
    }
}