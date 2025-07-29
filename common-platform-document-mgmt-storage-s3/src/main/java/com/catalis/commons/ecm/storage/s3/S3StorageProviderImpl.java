package com.catalis.commons.ecm.storage.s3;

import com.catalis.commons.ecm.core.providers.storage.FileStorageProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Implementation of the FileStorageProvider interface for Amazon S3.
 * This implementation uses the AWS SDK for Java v2 to interact with S3.
 */
@Component
public class S3StorageProviderImpl implements FileStorageProvider {

    private static final Logger log = LoggerFactory.getLogger(S3StorageProviderImpl.class);
    private static final String PROVIDER_NAME = "AmazonS3";
    private static final DefaultDataBufferFactory BUFFER_FACTORY = new DefaultDataBufferFactory();

    private final String privateBucketName;
    private final String publicBucketName;
    private final String basePrivateUrl;
    private final String basePublicUrl;
    private final String endpoint;
    private final String region;
    private final String accessKey;
    private final String secretKey;
    
    private S3AsyncClient s3AsyncClient;
    private S3Presigner s3Presigner;

    /**
     * Constructor for S3StorageProviderImpl.
     *
     * @param endpoint the S3 endpoint URL
     * @param region the AWS region
     * @param accessKey the AWS access key
     * @param secretKey the AWS secret key
     * @param privateBucketName the S3 private bucket name
     * @param publicBucketName the S3 public bucket name
     */
    public S3StorageProviderImpl(
            @Value("${storage.s3.endpoint}") String endpoint,
            @Value("${storage.s3.region}") String region,
            @Value("${storage.s3.access-key}") String accessKey,
            @Value("${storage.s3.secret-key}") String secretKey,
            @Value("${storage.s3.private-bucket-name}") String privateBucketName,
            @Value("${storage.s3.public-bucket-name}") String publicBucketName) {
        
        this.privateBucketName = privateBucketName;
        this.publicBucketName = publicBucketName;
        this.endpoint = endpoint;
        this.basePrivateUrl = endpoint + "/" + privateBucketName + "/";
        this.basePublicUrl = endpoint + "/" + publicBucketName + "/";
        this.region = region;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        
        log.info("Initializing S3 storage provider with private bucket: {} and public bucket: {}", 
                privateBucketName, publicBucketName);
    }
    
    /**
     * Initializes the S3 clients after bean construction.
     */
    @PostConstruct
    public void init() {
        // Create AWS credentials
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        
        // Configure the S3 async client with Netty for non-blocking I/O
        this.s3AsyncClient = S3AsyncClient.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .httpClient(NettyNioAsyncHttpClient.builder()
                        .connectionTimeout(Duration.ofSeconds(30))
                        .maxConcurrency(100)
                        .build())
                .build();
        
        // Configure the S3 presigner for generating pre-signed URLs
        this.s3Presigner = S3Presigner.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
        
        log.info("S3 async client and presigner initialized successfully");
        
        // Ensure buckets exist
        ensureBucketExists(privateBucketName);
        ensureBucketExists(publicBucketName);
    }
    
    /**
     * Ensures that the specified bucket exists, creating it if necessary.
     *
     * @param bucketName the name of the bucket to check/create
     */
    private void ensureBucketExists(String bucketName) {
        s3AsyncClient.headBucket(HeadBucketRequest.builder().bucket(bucketName).build())
                .exceptionally(throwable -> {
                    log.info("Bucket {} does not exist, creating it", bucketName);
                    s3AsyncClient.createBucket(CreateBucketRequest.builder().bucket(bucketName).build())
                            .join();
                    return null;
                });
    }

    @Override
    public Mono<String> uploadFile(FilePart filePart, String path) {
        String key = generateKey(path, filePart.filename());
        log.info("Uploading file to S3 private bucket: {}", key);
        
        // Create metadata with content type if available
        Map<String, String> metadata = new HashMap<>();
        if (filePart.headers().getContentType() != null) {
            metadata.put("Content-Type", filePart.headers().getContentType().toString());
        }
        
        // Convert FilePart content to a Flux of ByteBuffer for the AWS SDK
        Flux<ByteBuffer> byteBufferFlux = filePart.content()
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    return ByteBuffer.wrap(bytes);
                });
        
        // Create the PutObjectRequest
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(privateBucketName)
                .key(key)
                .metadata(metadata)
                .build();
        
        // Upload the file using the S3 async client
        return Mono.fromFuture(() -> 
                s3AsyncClient.putObject(putObjectRequest, 
                        AsyncRequestBody.fromPublisher(byteBufferFlux)))
                .map(response -> {
                    log.info("File uploaded successfully to S3 private bucket: {}", key);
                    return basePrivateUrl + key;
                })
                .onErrorResume(e -> {
                    log.error("Error uploading file to S3 private bucket: {}", key, e);
                    return Mono.error(new RuntimeException("Failed to upload file to S3", e));
                });
    }

    @Override
    public Mono<String> uploadFileToPublicBucket(FilePart filePart, String path) {
        String key = generateKey(path, filePart.filename());
        log.info("Uploading file to S3 public bucket: {}", key);
        
        // Create metadata with content type if available
        Map<String, String> metadata = new HashMap<>();
        if (filePart.headers().getContentType() != null) {
            metadata.put("Content-Type", filePart.headers().getContentType().toString());
        }
        
        // Convert FilePart content to a Flux of ByteBuffer for the AWS SDK
        Flux<ByteBuffer> byteBufferFlux = filePart.content()
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    return ByteBuffer.wrap(bytes);
                });
        
        // Create the PutObjectRequest with public-read ACL
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(publicBucketName)
                .key(key)
                .metadata(metadata)
                .acl(ObjectCannedACL.PUBLIC_READ) // Make the object publicly readable
                .build();
        
        // Upload the file using the S3 async client
        return Mono.fromFuture(() -> 
                s3AsyncClient.putObject(putObjectRequest, 
                        AsyncRequestBody.fromPublisher(byteBufferFlux)))
                .map(response -> {
                    log.info("File uploaded successfully to S3 public bucket: {}", key);
                    return basePublicUrl + key;
                })
                .onErrorResume(e -> {
                    log.error("Error uploading file to S3 public bucket: {}", key, e);
                    return Mono.error(new RuntimeException("Failed to upload file to S3 public bucket", e));
                });
    }

    @Override
    public Mono<String> uploadContent(Flux<DataBuffer> content, String fileName, String contentType, String path) {
        String key = generateKey(path, fileName);
        log.info("Uploading content to S3 private bucket: {}", key);
        
        // Create metadata with content type if available
        Map<String, String> metadata = new HashMap<>();
        if (contentType != null) {
            metadata.put("Content-Type", contentType);
        }
        
        // Convert DataBuffer content to a Flux of ByteBuffer for the AWS SDK
        Flux<ByteBuffer> byteBufferFlux = content
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    return ByteBuffer.wrap(bytes);
                });
        
        // Create the PutObjectRequest
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(privateBucketName)
                .key(key)
                .metadata(metadata)
                .build();
        
        // Upload the content using the S3 async client
        return Mono.fromFuture(() -> 
                s3AsyncClient.putObject(putObjectRequest, 
                        AsyncRequestBody.fromPublisher(byteBufferFlux)))
                .map(response -> {
                    log.info("Content uploaded successfully to S3 private bucket: {}", key);
                    return basePrivateUrl + key;
                })
                .onErrorResume(e -> {
                    log.error("Error uploading content to S3 private bucket: {}", key, e);
                    return Mono.error(new RuntimeException("Failed to upload content to S3", e));
                });
    }

    @Override
    public Mono<String> uploadContentToPublicBucket(Flux<DataBuffer> content, String fileName, String contentType, String path) {
        String key = generateKey(path, fileName);
        log.info("Uploading content to S3 public bucket: {}", key);
        
        // Create metadata with content type if available
        Map<String, String> metadata = new HashMap<>();
        if (contentType != null) {
            metadata.put("Content-Type", contentType);
        }
        
        // Convert DataBuffer content to a Flux of ByteBuffer for the AWS SDK
        Flux<ByteBuffer> byteBufferFlux = content
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    return ByteBuffer.wrap(bytes);
                });
        
        // Create the PutObjectRequest with public-read ACL
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(publicBucketName)
                .key(key)
                .metadata(metadata)
                .acl(ObjectCannedACL.PUBLIC_READ) // Make the object publicly readable
                .build();
        
        // Upload the content using the S3 async client
        return Mono.fromFuture(() -> 
                s3AsyncClient.putObject(putObjectRequest, 
                        AsyncRequestBody.fromPublisher(byteBufferFlux)))
                .map(response -> {
                    log.info("Content uploaded successfully to S3 public bucket: {}", key);
                    return basePublicUrl + key;
                })
                .onErrorResume(e -> {
                    log.error("Error uploading content to S3 public bucket: {}", key, e);
                    return Mono.error(new RuntimeException("Failed to upload content to S3 public bucket", e));
                });
    }

    @Override
    public Mono<String> moveFileFromPublicToPrivate(String publicFileUrl) {
        String key = extractKeyFromUrl(publicFileUrl, true);
        String newKey = key; // Use the same key in the private bucket
        log.info("Moving file from public to private bucket: {}", key);
        
        // Create the copy request
        CopyObjectRequest copyRequest = CopyObjectRequest.builder()
                .sourceBucket(publicBucketName)
                .sourceKey(key)
                .destinationBucket(privateBucketName)
                .destinationKey(newKey)
                .build();
        
        // Copy the object from public to private bucket, then delete from public bucket
        return Mono.fromFuture(() -> s3AsyncClient.copyObject(copyRequest))
                .flatMap(copyResponse -> {
                    log.info("File copied from public to private bucket: {}", key);
                    
                    // Delete the file from the public bucket
                    DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                            .bucket(publicBucketName)
                            .key(key)
                            .build();
                    
                    return Mono.fromFuture(() -> s3AsyncClient.deleteObject(deleteRequest))
                            .map(deleteResponse -> {
                                log.info("File deleted from public bucket: {}", key);
                                return basePrivateUrl + newKey;
                            });
                })
                .onErrorResume(e -> {
                    log.error("Error moving file from public to private bucket: {}", key, e);
                    return Mono.error(new RuntimeException("Failed to move file from public to private bucket", e));
                });
    }

    @Override
    public Flux<DataBuffer> downloadFile(String fileUrl) {
        boolean isPublic = isPublicUrl(fileUrl);
        String key = extractKeyFromUrl(fileUrl, isPublic);
        String bucketName = isPublic ? publicBucketName : privateBucketName;
        log.info("Downloading file from S3 {} bucket: {}", isPublic ? "public" : "private", key);
        
        // Create the GetObjectRequest
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        
        // Download the file using the S3 async client
        return Flux.create(sink -> {
            s3AsyncClient.getObject(getObjectRequest, AsyncResponseTransformer.toBytes())
                    .whenComplete((response, error) -> {
                        if (error != null) {
                            log.error("Error downloading file from S3 {} bucket: {}", 
                                    isPublic ? "public" : "private", key, error);
                            sink.error(new RuntimeException("Failed to download file from S3", error));
                            return;
                        }
                        
                        log.info("File downloaded successfully from S3 {} bucket: {}", 
                                isPublic ? "public" : "private", key);
                        
                        // Convert the response bytes to DataBuffer
                        DataBuffer dataBuffer = BUFFER_FACTORY.wrap(response.asByteArray());
                        sink.next(dataBuffer);
                        sink.complete();
                    });
        });
    }

    @Override
    public Mono<Void> deleteFile(String fileUrl) {
        boolean isPublic = isPublicUrl(fileUrl);
        String key = extractKeyFromUrl(fileUrl, isPublic);
        String bucketName = isPublic ? publicBucketName : privateBucketName;
        log.info("Deleting file from S3 {} bucket: {}", isPublic ? "public" : "private", key);
        
        // Create the DeleteObjectRequest
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        
        // Delete the file using the S3 async client
        return Mono.fromFuture(() -> s3AsyncClient.deleteObject(deleteRequest))
                .doOnSuccess(response -> {
                    log.info("File deleted successfully from S3 {} bucket: {}", 
                            isPublic ? "public" : "private", key);
                })
                .then()
                .onErrorResume(e -> {
                    log.error("Error deleting file from S3 {} bucket: {}", 
                            isPublic ? "public" : "private", key, e);
                    return Mono.error(new RuntimeException("Failed to delete file from S3", e));
                });
    }

    @Override
    public Mono<Boolean> fileExists(String fileUrl) {
        boolean isPublic = isPublicUrl(fileUrl);
        String key = extractKeyFromUrl(fileUrl, isPublic);
        String bucketName = isPublic ? publicBucketName : privateBucketName;
        log.info("Checking if file exists in S3 {} bucket: {}", isPublic ? "public" : "private", key);
        
        // Create the HeadObjectRequest
        HeadObjectRequest headRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        
        // Check if the file exists using the S3 async client
        return Mono.fromFuture(() -> s3AsyncClient.headObject(headRequest))
                .map(response -> {
                    log.info("File exists in S3 {} bucket: {}", isPublic ? "public" : "private", key);
                    return true;
                })
                .onErrorResume(e -> {
                    if (e instanceof NoSuchKeyException) {
                        log.info("File does not exist in S3 {} bucket: {}", isPublic ? "public" : "private", key);
                        return Mono.just(false);
                    }
                    log.error("Error checking if file exists in S3 {} bucket: {}", 
                            isPublic ? "public" : "private", key, e);
                    return Mono.error(new RuntimeException("Failed to check if file exists in S3", e));
                });
    }

    @Override
    public Mono<String> generatePresignedUrl(String fileUrl, long expirationInSeconds) {
        boolean isPublic = isPublicUrl(fileUrl);
        String key = extractKeyFromUrl(fileUrl, isPublic);
        String bucketName = isPublic ? publicBucketName : privateBucketName;
        log.info("Generating pre-signed URL for S3 {} bucket file: {} with expiration: {} seconds", 
                isPublic ? "public" : "private", key, expirationInSeconds);
        
        // Create the GetObjectRequest
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        
        // Create the GetObjectPresignRequest with expiration
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(expirationInSeconds))
                .getObjectRequest(getObjectRequest)
                .build();
        
        try {
            // Generate the pre-signed URL
            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
            String presignedUrl = presignedRequest.url().toString();
            
            log.info("Pre-signed URL generated successfully for S3 {} bucket file: {}", 
                    isPublic ? "public" : "private", key);
            return Mono.just(presignedUrl);
        } catch (Exception e) {
            log.error("Error generating pre-signed URL for S3 {} bucket file: {}", 
                    isPublic ? "public" : "private", key, e);
            return Mono.error(new RuntimeException("Failed to generate pre-signed URL", e));
        }
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }
    
    /**
     * Generates a unique key for the file in S3.
     *
     * @param path the path where the file should be stored
     * @param fileName the name of the file
     * @return the generated key
     */
    private String generateKey(String path, String fileName) {
        String sanitizedPath = path.startsWith("/") ? path.substring(1) : path;
        sanitizedPath = sanitizedPath.endsWith("/") ? sanitizedPath : sanitizedPath + "/";
        return sanitizedPath + UUID.randomUUID() + "-" + fileName;
    }
    
    /**
     * Checks if a URL is for the public bucket.
     *
     * @param fileUrl the URL of the file
     * @return true if the URL is for the public bucket, false otherwise
     */
    private boolean isPublicUrl(String fileUrl) {
        return fileUrl.contains("/" + publicBucketName + "/");
    }
    
    /**
     * Extracts the key from a file URL.
     *
     * @param fileUrl the URL of the file
     * @param isPublic whether the URL is for the public bucket
     * @return the extracted key
     */
    private String extractKeyFromUrl(String fileUrl, boolean isPublic) {
        return isPublic ? fileUrl.replace(basePublicUrl, "") : fileUrl.replace(basePrivateUrl, "");
    }
}