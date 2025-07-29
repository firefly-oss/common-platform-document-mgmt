package com.catalis.commons.ecm.core.services;

import com.catalis.commons.ecm.core.providers.storage.FileStorageProviderRegistry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of the StorageService interface.
 * This service uses the FileStorageProviderRegistry to get the appropriate FileStorageProvider
 * and perform file storage operations.
 */
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private static final Logger log = LoggerFactory.getLogger(StorageServiceImpl.class);
    
    private final FileStorageProviderRegistry providerRegistry;

    @Override
    public Mono<String> uploadFile(FilePart filePart, String path, String providerName) {
        log.info("Uploading file {} to path {} with provider: {}", filePart.filename(), path, providerName);
        
        return providerRegistry.getProvider(providerName)
                .uploadFile(filePart, path);
    }

    @Override
    public Mono<String> uploadFile(FilePart filePart, String path) {
        log.info("Uploading file {} to path {} with default provider", filePart.filename(), path);
        
        return providerRegistry.getDefaultProvider()
                .uploadFile(filePart, path);
    }

    @Override
    public Mono<String> uploadContent(Flux<DataBuffer> content, String fileName, String contentType, String path, String providerName) {
        log.info("Uploading content as {} to path {} with provider: {}", fileName, path, providerName);
        
        return providerRegistry.getProvider(providerName)
                .uploadContent(content, fileName, contentType, path);
    }

    @Override
    public Mono<String> uploadContent(Flux<DataBuffer> content, String fileName, String contentType, String path) {
        log.info("Uploading content as {} to path {} with default provider", fileName, path);
        
        return providerRegistry.getDefaultProvider()
                .uploadContent(content, fileName, contentType, path);
    }

    @Override
    public Flux<DataBuffer> downloadFile(String fileUrl, String providerName) {
        log.info("Downloading file from URL: {} with provider: {}", fileUrl, providerName);
        
        return providerRegistry.getProvider(providerName)
                .downloadFile(fileUrl);
    }

    @Override
    public Flux<DataBuffer> downloadFile(String fileUrl) {
        log.info("Downloading file from URL: {} with default provider", fileUrl);
        
        return providerRegistry.getDefaultProvider()
                .downloadFile(fileUrl);
    }

    @Override
    public Mono<Void> deleteFile(String fileUrl, String providerName) {
        log.info("Deleting file from URL: {} with provider: {}", fileUrl, providerName);
        
        return providerRegistry.getProvider(providerName)
                .deleteFile(fileUrl);
    }

    @Override
    public Mono<Void> deleteFile(String fileUrl) {
        log.info("Deleting file from URL: {} with default provider", fileUrl);
        
        return providerRegistry.getDefaultProvider()
                .deleteFile(fileUrl);
    }

    @Override
    public Mono<Boolean> fileExists(String fileUrl, String providerName) {
        log.info("Checking if file exists at URL: {} with provider: {}", fileUrl, providerName);
        
        return providerRegistry.getProvider(providerName)
                .fileExists(fileUrl);
    }

    @Override
    public Mono<Boolean> fileExists(String fileUrl) {
        log.info("Checking if file exists at URL: {} with default provider", fileUrl);
        
        return providerRegistry.getDefaultProvider()
                .fileExists(fileUrl);
    }

    @Override
    public Mono<String> generatePresignedUrl(String fileUrl, long expirationInSeconds, String providerName) {
        log.info("Generating pre-signed URL for file at URL: {} with provider: {} (expiration: {} seconds)", 
                fileUrl, providerName, expirationInSeconds);
        
        return providerRegistry.getProvider(providerName)
                .generatePresignedUrl(fileUrl, expirationInSeconds);
    }

    @Override
    public Mono<String> generatePresignedUrl(String fileUrl, long expirationInSeconds) {
        log.info("Generating pre-signed URL for file at URL: {} with default provider (expiration: {} seconds)", 
                fileUrl, expirationInSeconds);
        
        return providerRegistry.getDefaultProvider()
                .generatePresignedUrl(fileUrl, expirationInSeconds);
    }
}