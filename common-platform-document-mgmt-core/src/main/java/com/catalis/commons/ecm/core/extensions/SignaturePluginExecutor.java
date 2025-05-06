package com.catalis.commons.ecm.core.extensions;

import com.catalis.commons.ecm.interfaces.dtos.DocumentDTO;
import com.catalis.commons.ecm.interfaces.dtos.DocumentSignatureDTO;
import com.catalis.commons.ecm.interfaces.dtos.SignatureRequestDTO;
import com.catalis.commons.ecm.interfaces.dtos.SignatureVerificationDTO;
import com.catalis.commons.ecm.interfaces.extensions.SignatureProviderExtension;
import com.catalis.core.plugin.api.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SignaturePluginExecutor {

    @Autowired
    private PluginManager pluginManager;

    public Mono<SignatureProviderExtension> resolveProvider(SignatureRequestDTO request) {
        return pluginManager.getExtensionRegistry()
                .getHighestPriorityExtension("com.catalis.commons.ecm.interfaces.extensions.signature-provider")
                .cast(SignatureProviderExtension.class)
                .filter(provider -> provider.supports(request))
                .switchIfEmpty(Mono.error(new IllegalStateException("No compatible signature provider found")));
    }

    public Mono<Void> send(SignatureRequestDTO request, DocumentSignatureDTO signature, DocumentDTO document) {
        return resolveProvider(request)
                .flatMap(provider -> provider.sendSignatureRequest(request, signature, document));
    }

    public Mono<SignatureRequestDTO> checkStatus(String providerRequestId) {
        return pluginManager.getExtensionRegistry()
                .getHighestPriorityExtension("com.catalis.commons.ecm.interfaces.extensions.signature-provider")
                .cast(SignatureProviderExtension.class)
                .flatMap(provider -> provider.fetchSignatureRequestStatus(providerRequestId));
    }

    public Mono<SignatureVerificationDTO> verify(Long signatureId) {
        return pluginManager.getExtensionRegistry()
                .getHighestPriorityExtension("com.catalis.commons.ecm.interfaces.extensions.signature-provider")
                .cast(SignatureProviderExtension.class)
                .flatMap(provider -> provider.verifySignature(signatureId));
    }

    public Mono<Void> cancel(String providerRequestId) {
        return pluginManager.getExtensionRegistry()
                .getHighestPriorityExtension("com.catalis.commons.ecm.interfaces.extensions.signature-provider")
                .cast(SignatureProviderExtension.class)
                .flatMap(provider -> provider.cancelSignatureRequest(providerRequestId));
    }

    public Flux<DocumentSignatureDTO> fetchSigners(String providerRequestId) {
        return pluginManager.getExtensionRegistry()
                .getHighestPriorityExtension("com.catalis.commons.ecm.interfaces.extensions.signature-provider")
                .cast(SignatureProviderExtension.class)
                .flatMapMany(provider -> provider.fetchSigners(providerRequestId));
    }
}