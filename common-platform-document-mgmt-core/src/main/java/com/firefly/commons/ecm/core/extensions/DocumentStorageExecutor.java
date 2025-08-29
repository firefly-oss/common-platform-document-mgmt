package com.firefly.commons.ecm.core.extensions;

import com.firefly.commons.ecm.interfaces.dtos.DocumentDTO;
import com.firefly.commons.ecm.interfaces.extensions.DocumentStorageExtension;
import com.firefly.core.plugin.api.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DocumentStorageExecutor {

    @Autowired
    private PluginManager pluginManager;

    private Mono<DocumentStorageExtension> resolveProvider(String storageType) {
        return pluginManager.getExtensionRegistry()
                .getHighestPriorityExtension("com.firefly.commons.ecm.interfaces.extensions.storage-provider")
                .cast(DocumentStorageExtension.class)
                .filter(p -> p.supportsStorageType(storageType))
                .switchIfEmpty(Mono.error(() ->
                        new IllegalStateException("No plugin supports storage type: " + storageType)));
    }

    public Mono<String> generateUploadUrl(DocumentDTO document) {
        return resolveProvider(document.getStorageType().name())
                .flatMap(provider -> provider.generatePresignedUploadUrl(document));
    }

    public Mono<Void> confirmUpload(DocumentDTO document) {
        return resolveProvider(document.getStorageType().name())
                .flatMap(provider -> provider.confirmUpload(document));
    }

    public Mono<String> generateDownloadUrl(DocumentDTO document) {
        return resolveProvider(document.getStorageType().name())
                .flatMap(provider -> provider.generatePresignedDownloadUrl(document));
    }

    public Mono<Void> deleteDocument(DocumentDTO document) {
        return resolveProvider(document.getStorageType().name())
                .flatMap(provider -> provider.deleteDocument(document));
    }
}