package com.catalis.commons.ecm.core.providers.storage;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Provider interface for file storage services.
 * This interface defines the contract that all file storage provider implementations must follow.
 */
public interface FileStorageProvider {
    
    /**
     * Uploads a file to the storage service.
     *
     * @param filePart the file part containing the file data
     * @param path the path where the file should be stored (can include subdirectories)
     * @return a Mono emitting the URL where the file can be accessed
     */
    Mono<String> uploadFile(FilePart filePart, String path);
    
    /**
     * Uploads file content to the storage service.
     *
     * @param content the file content as a Flux of DataBuffer
     * @param fileName the name of the file
     * @param contentType the content type of the file
     * @param path the path where the file should be stored (can include subdirectories)
     * @return a Mono emitting the URL where the file can be accessed
     */
    Mono<String> uploadContent(Flux<DataBuffer> content, String fileName, String contentType, String path);
    
    /**
     * Downloads a file from the storage service.
     *
     * @param fileUrl the URL of the file to download
     * @return a Flux emitting the file content as DataBuffer
     */
    Flux<DataBuffer> downloadFile(String fileUrl);
    
    /**
     * Deletes a file from the storage service.
     *
     * @param fileUrl the URL of the file to delete
     * @return a Mono that completes when the file is successfully deleted
     */
    Mono<Void> deleteFile(String fileUrl);
    
    /**
     * Checks if a file exists in the storage service.
     *
     * @param fileUrl the URL of the file to check
     * @return a Mono emitting a boolean indicating whether the file exists
     */
    Mono<Boolean> fileExists(String fileUrl);
    
    /**
     * Generates a pre-signed URL for temporary access to a file.
     *
     * @param fileUrl the URL of the file
     * @param expirationInSeconds the expiration time in seconds
     * @return a Mono emitting the pre-signed URL
     */
    Mono<String> generatePresignedUrl(String fileUrl, long expirationInSeconds);
    
    /**
     * Gets the name of the provider.
     *
     * @return the name of the file storage provider
     */
    String getProviderName();
}