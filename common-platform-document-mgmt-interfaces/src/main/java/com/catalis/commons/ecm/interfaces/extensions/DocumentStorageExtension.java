package com.catalis.commons.ecm.interfaces.extensions;

import com.catalis.commons.ecm.interfaces.dtos.DocumentDTO;
import com.catalis.core.plugin.annotation.ExtensionPoint;
import reactor.core.publisher.Mono;

/**
 * Extension point for abstracting document storage providers like S3, Azure Blob, etc.
 */
@ExtensionPoint(
        id = "com.catalis.commons.ecm.interfaces.extensions.storage-provider",
        description = "Provides storage operations like upload/download for documents using pre-signed URLs",
        allowMultiple = false
)
public interface DocumentStorageExtension {

    /**
     * Generates a pre-signed PUT URL to allow the client to upload a document version.
     *
     * @param document the document to be uploaded
     * @return the signed URL string
     */
    Mono<String> generatePresignedUploadUrl(DocumentDTO document);

    /**
     * Confirms that the client has uploaded the document to the given path.
     *
     * @param document the document to be confirmed
     * @return confirmation result (could persist metadata or mark it as uploaded)
     */
    Mono<Void> confirmUpload(DocumentDTO document);

    /**
     * Generates a pre-signed GET URL to allow downloading the document.
     *
     * @param document the document to download
     * @return the signed URL string
     */
    Mono<String> generatePresignedDownloadUrl(DocumentDTO document);

    /**
     * Deletes a document from storage.
     *
     * @param document the document to delete
     * @return Mono signaling completion
     */
    Mono<Void> deleteDocument(DocumentDTO document);

    /**
     * Returns the code that identifies the provider (e.g., "s3", "azure", "minio").
     */
    String getProviderCode();

    /**
     * Returns true if this plugin supports the given storage type.
     */
    boolean supportsStorageType(String storageType);
}