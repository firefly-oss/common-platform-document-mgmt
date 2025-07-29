package com.catalis.commons.ecm.core.services;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service for file storage operations.
 * This service uses the FileStorageProvider to perform file storage operations.
 */
public interface StorageService {
    
    /**
     * Uploads a file to the storage provider.
     *
     * @param filePart the file part containing the file data
     * @param path the path where the file should be stored (can include subdirectories)
     * @param providerName the name of the file storage provider to use (optional, uses default if not provided)
     * @return a Mono emitting the URL where the file can be accessed
     */
    Mono<String> uploadFile(FilePart filePart, String path, String providerName);
    
    /**
     * Uploads a file to the default storage provider.
     *
     * @param filePart the file part containing the file data
     * @param path the path where the file should be stored (can include subdirectories)
     * @return a Mono emitting the URL where the file can be accessed
     */
    Mono<String> uploadFile(FilePart filePart, String path);
    
    /**
     * Uploads a file to the public bucket of the storage provider.
     * Files in the public bucket are intended for temporary storage before being moved to the private bucket.
     *
     * @param filePart the file part containing the file data
     * @param path the path where the file should be stored (can include subdirectories)
     * @param providerName the name of the file storage provider to use (optional, uses default if not provided)
     * @return a Mono emitting the URL where the file can be accessed
     */
    Mono<String> uploadFileToPublicBucket(FilePart filePart, String path, String providerName);
    
    /**
     * Uploads a file to the public bucket of the default storage provider.
     * Files in the public bucket are intended for temporary storage before being moved to the private bucket.
     *
     * @param filePart the file part containing the file data
     * @param path the path where the file should be stored (can include subdirectories)
     * @return a Mono emitting the URL where the file can be accessed
     */
    Mono<String> uploadFileToPublicBucket(FilePart filePart, String path);
    
    /**
     * Uploads file content to the storage provider.
     *
     * @param content the file content as a Flux of DataBuffer
     * @param fileName the name of the file
     * @param contentType the content type of the file
     * @param path the path where the file should be stored (can include subdirectories)
     * @param providerName the name of the file storage provider to use (optional, uses default if not provided)
     * @return a Mono emitting the URL where the file can be accessed
     */
    Mono<String> uploadContent(Flux<DataBuffer> content, String fileName, String contentType, String path, String providerName);
    
    /**
     * Uploads file content to the default storage provider.
     *
     * @param content the file content as a Flux of DataBuffer
     * @param fileName the name of the file
     * @param contentType the content type of the file
     * @param path the path where the file should be stored (can include subdirectories)
     * @return a Mono emitting the URL where the file can be accessed
     */
    Mono<String> uploadContent(Flux<DataBuffer> content, String fileName, String contentType, String path);
    
    /**
     * Uploads file content to the public bucket of the storage provider.
     * Files in the public bucket are intended for temporary storage before being moved to the private bucket.
     *
     * @param content the file content as a Flux of DataBuffer
     * @param fileName the name of the file
     * @param contentType the content type of the file
     * @param path the path where the file should be stored (can include subdirectories)
     * @param providerName the name of the file storage provider to use (optional, uses default if not provided)
     * @return a Mono emitting the URL where the file can be accessed
     */
    Mono<String> uploadContentToPublicBucket(Flux<DataBuffer> content, String fileName, String contentType, String path, String providerName);
    
    /**
     * Uploads file content to the public bucket of the default storage provider.
     * Files in the public bucket are intended for temporary storage before being moved to the private bucket.
     *
     * @param content the file content as a Flux of DataBuffer
     * @param fileName the name of the file
     * @param contentType the content type of the file
     * @param path the path where the file should be stored (can include subdirectories)
     * @return a Mono emitting the URL where the file can be accessed
     */
    Mono<String> uploadContentToPublicBucket(Flux<DataBuffer> content, String fileName, String contentType, String path);
    
    /**
     * Moves a file from the public bucket to the private bucket.
     *
     * @param publicFileUrl the URL of the file in the public bucket
     * @param providerName the name of the file storage provider to use (optional, uses default if not provided)
     * @return a Mono emitting the URL of the file in the private bucket
     */
    Mono<String> moveFileFromPublicToPrivate(String publicFileUrl, String providerName);
    
    /**
     * Moves a file from the public bucket to the private bucket using the default storage provider.
     *
     * @param publicFileUrl the URL of the file in the public bucket
     * @return a Mono emitting the URL of the file in the private bucket
     */
    Mono<String> moveFileFromPublicToPrivate(String publicFileUrl);
    
    /**
     * Downloads a file from the storage provider.
     *
     * @param fileUrl the URL of the file to download
     * @param providerName the name of the file storage provider to use (optional, uses default if not provided)
     * @return a Flux emitting the file content as DataBuffer
     */
    Flux<DataBuffer> downloadFile(String fileUrl, String providerName);
    
    /**
     * Downloads a file from the default storage provider.
     *
     * @param fileUrl the URL of the file to download
     * @return a Flux emitting the file content as DataBuffer
     */
    Flux<DataBuffer> downloadFile(String fileUrl);
    
    /**
     * Deletes a file from the storage provider.
     *
     * @param fileUrl the URL of the file to delete
     * @param providerName the name of the file storage provider to use (optional, uses default if not provided)
     * @return a Mono that completes when the file is successfully deleted
     */
    Mono<Void> deleteFile(String fileUrl, String providerName);
    
    /**
     * Deletes a file from the default storage provider.
     *
     * @param fileUrl the URL of the file to delete
     * @return a Mono that completes when the file is successfully deleted
     */
    Mono<Void> deleteFile(String fileUrl);
    
    /**
     * Checks if a file exists in the storage provider.
     *
     * @param fileUrl the URL of the file to check
     * @param providerName the name of the file storage provider to use (optional, uses default if not provided)
     * @return a Mono emitting a boolean indicating whether the file exists
     */
    Mono<Boolean> fileExists(String fileUrl, String providerName);
    
    /**
     * Checks if a file exists in the default storage provider.
     *
     * @param fileUrl the URL of the file to check
     * @return a Mono emitting a boolean indicating whether the file exists
     */
    Mono<Boolean> fileExists(String fileUrl);
    
    /**
     * Generates a pre-signed URL for temporary access to a file in the storage provider.
     *
     * @param fileUrl the URL of the file
     * @param expirationInSeconds the expiration time in seconds
     * @param providerName the name of the file storage provider to use (optional, uses default if not provided)
     * @return a Mono emitting the pre-signed URL
     */
    Mono<String> generatePresignedUrl(String fileUrl, long expirationInSeconds, String providerName);
    
    /**
     * Generates a pre-signed URL for temporary access to a file in the default storage provider.
     *
     * @param fileUrl the URL of the file
     * @param expirationInSeconds the expiration time in seconds
     * @return a Mono emitting the pre-signed URL
     */
    Mono<String> generatePresignedUrl(String fileUrl, long expirationInSeconds);
}