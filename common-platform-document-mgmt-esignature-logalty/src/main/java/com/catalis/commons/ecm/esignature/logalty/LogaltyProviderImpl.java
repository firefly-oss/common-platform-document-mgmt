package com.catalis.commons.ecm.esignature.logalty;

import com.catalis.commons.ecm.core.providers.esignature.ESignatureProvider;
import com.catalis.commons.ecm.interfaces.dtos.SignatureRequestDTO;
import com.catalis.commons.ecm.interfaces.dtos.SignatureProofDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Implementation of the ESignatureProvider interface for the Logalty e-signature service.
 */
@Component
public class LogaltyProviderImpl implements ESignatureProvider {

    private static final Logger log = LoggerFactory.getLogger(LogaltyProviderImpl.class);
    private static final String PROVIDER_NAME = "Logalty";

    private final WebClient webClient;
    private final String apiUrl;
    private final String apiKey;
    private final String apiSecret;

    /**
     * Constructor for LogaltyProviderImpl.
     *
     * @param apiUrl the base URL for the Logalty API
     * @param apiKey the API key for authentication
     * @param apiSecret the API secret for authentication
     */
    public LogaltyProviderImpl(
            @Value("${esignature.logalty.api-url}") String apiUrl,
            @Value("${esignature.logalty.api-key}") String apiKey,
            @Value("${esignature.logalty.api-secret}") String apiSecret) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("X-API-Key", apiKey)
                .defaultHeader("X-API-Secret", apiSecret)
                .build();
    }

    @Override
    public Mono<SignatureRequestDTO> initiateSignatureRequest(SignatureRequestDTO signatureRequest) {
        log.info("Initiating signature request with Logalty for document ID: {}", signatureRequest.getDocumentId());
        
        // In a real implementation, this would make an API call to Logalty to initiate a signature request
        // For now, we'll simulate the response
        return Mono.just(signatureRequest)
                .map(request -> {
                    request.setExternalSignatureId("LOGALTY-" + System.currentTimeMillis());
                    return request;
                });
    }

    @Override
    public Mono<SignatureRequestDTO> getSignatureRequestStatus(Long signatureRequestId, String externalSignatureId) {
        log.info("Getting signature request status from Logalty for external ID: {}", externalSignatureId);
        
        // In a real implementation, this would make an API call to Logalty to get the status
        // For now, we'll simulate the response
        return Mono.just(SignatureRequestDTO.builder()
                .id(signatureRequestId)
                .externalSignatureId(externalSignatureId)
                .statusId(1L) // Assuming 1 is "In Progress"
                .build());
    }

    @Override
    public Mono<Void> cancelSignatureRequest(Long signatureRequestId, String externalSignatureId) {
        log.info("Cancelling signature request with Logalty for external ID: {}", externalSignatureId);
        
        // In a real implementation, this would make an API call to Logalty to cancel the request
        // For now, we'll just return a completed Mono
        return Mono.empty();
    }

    @Override
    public Mono<SignatureProofDTO> getSignatureProof(Long signatureRequestId, String externalSignatureId) {
        log.info("Getting signature proof from Logalty for external ID: {}", externalSignatureId);
        
        // In a real implementation, this would make an API call to Logalty to get the proof
        // For now, we'll simulate the response
        return Mono.just(SignatureProofDTO.builder()
                .signatureRequestId(signatureRequestId)
                .proofUrl("https://logalty.com/proofs/" + externalSignatureId)
                .proofTypeId(1L) // Assuming 1 is "Digital Signature"
                .proofDate(LocalDateTime.now())
                .build());
    }

    @Override
    public Mono<Boolean> validateSignatureProof(SignatureProofDTO signatureProof) {
        log.info("Validating signature proof with Logalty for URL: {}", signatureProof.getProofUrl());
        
        // In a real implementation, this would make an API call to Logalty to validate the proof
        // For now, we'll just return true
        return Mono.just(true);
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }
}