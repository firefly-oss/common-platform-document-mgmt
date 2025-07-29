package com.catalis.commons.ecm.core.services;

import com.catalis.commons.ecm.core.providers.esignature.ESignatureProviderRegistry;
import com.catalis.commons.ecm.interfaces.dtos.SignatureRequestDTO;
import com.catalis.commons.ecm.interfaces.dtos.SignatureProofDTO;
import com.catalis.commons.ecm.models.repositories.SignatureRequestRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the ESignatureService interface.
 * This service uses the ESignatureProviderRegistry to get the appropriate ESignatureProvider
 * and trigger e-signature processes.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ESignatureServiceImpl implements ESignatureService {

    private static final Logger log = LoggerFactory.getLogger(ESignatureServiceImpl.class);
    
    private final ESignatureProviderRegistry providerRegistry;
    private final SignatureRequestRepository signatureRequestRepository;
    private final SignatureRequestService signatureRequestService;

    @Override
    public Mono<SignatureRequestDTO> initiateSignature(SignatureRequestDTO signatureRequestDTO, String providerName) {
        log.info("Initiating signature request with provider: {}", providerName);
        
        return Mono.just(signatureRequestDTO)
                .flatMap(signatureRequestService::createSignatureRequest)
                .flatMap(createdRequest -> {
                    // Get the provider and initiate the signature request
                    return providerRegistry.getProvider(providerName)
                            .initiateSignatureRequest(createdRequest)
                            .flatMap(updatedRequest -> {
                                // Update the signature request with the external signature ID
                                return signatureRequestService.updateSignatureRequest(
                                        updatedRequest.getId(), updatedRequest);
                            });
                });
    }

    @Override
    public Mono<SignatureRequestDTO> initiateSignature(SignatureRequestDTO signatureRequestDTO) {
        log.info("Initiating signature request with default provider");
        
        return Mono.just(signatureRequestDTO)
                .flatMap(signatureRequestService::createSignatureRequest)
                .flatMap(createdRequest -> {
                    // Get the default provider and initiate the signature request
                    return providerRegistry.getDefaultProvider()
                            .initiateSignatureRequest(createdRequest)
                            .flatMap(updatedRequest -> {
                                // Update the signature request with the external signature ID
                                return signatureRequestService.updateSignatureRequest(
                                        updatedRequest.getId(), updatedRequest);
                            });
                });
    }

    @Override
    public Mono<SignatureRequestDTO> getSignatureStatus(Long signatureRequestId, String providerName) {
        log.info("Getting signature status for request ID: {} with provider: {}", signatureRequestId, providerName);
        
        return signatureRequestRepository.findById(signatureRequestId)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature request not found with ID: " + signatureRequestId)))
                .flatMap(signatureRequest -> {
                    // Get the provider and check the signature status
                    return providerRegistry.getProvider(providerName)
                            .getSignatureRequestStatus(signatureRequestId, signatureRequest.getExternalSignatureId())
                            .flatMap(updatedRequest -> {
                                // Update the signature request with the current status
                                return signatureRequestService.updateSignatureRequest(
                                        updatedRequest.getId(), updatedRequest);
                            });
                });
    }

    @Override
    public Mono<SignatureRequestDTO> getSignatureStatus(Long signatureRequestId) {
        log.info("Getting signature status for request ID: {} with default provider", signatureRequestId);
        
        return signatureRequestRepository.findById(signatureRequestId)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature request not found with ID: " + signatureRequestId)))
                .flatMap(signatureRequest -> {
                    // Get the default provider and check the signature status
                    return providerRegistry.getDefaultProvider()
                            .getSignatureRequestStatus(signatureRequestId, signatureRequest.getExternalSignatureId())
                            .flatMap(updatedRequest -> {
                                // Update the signature request with the current status
                                return signatureRequestService.updateSignatureRequest(
                                        updatedRequest.getId(), updatedRequest);
                            });
                });
    }

    @Override
    public Mono<Void> cancelSignature(Long signatureRequestId, String providerName) {
        log.info("Cancelling signature request ID: {} with provider: {}", signatureRequestId, providerName);
        
        return signatureRequestRepository.findById(signatureRequestId)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature request not found with ID: " + signatureRequestId)))
                .flatMap(signatureRequest -> {
                    // Get the provider and cancel the signature request
                    return providerRegistry.getProvider(providerName)
                            .cancelSignatureRequest(signatureRequestId, signatureRequest.getExternalSignatureId());
                });
    }

    @Override
    public Mono<Void> cancelSignature(Long signatureRequestId) {
        log.info("Cancelling signature request ID: {} with default provider", signatureRequestId);
        
        return signatureRequestRepository.findById(signatureRequestId)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature request not found with ID: " + signatureRequestId)))
                .flatMap(signatureRequest -> {
                    // Get the default provider and cancel the signature request
                    return providerRegistry.getDefaultProvider()
                            .cancelSignatureRequest(signatureRequestId, signatureRequest.getExternalSignatureId());
                });
    }

    @Override
    public Mono<SignatureProofDTO> getSignatureProof(Long signatureRequestId, String providerName) {
        log.info("Getting signature proof for request ID: {} with provider: {}", signatureRequestId, providerName);
        
        return signatureRequestRepository.findById(signatureRequestId)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature request not found with ID: " + signatureRequestId)))
                .flatMap(signatureRequest -> {
                    // Get the provider and retrieve the signature proof
                    return providerRegistry.getProvider(providerName)
                            .getSignatureProof(signatureRequestId, signatureRequest.getExternalSignatureId());
                });
    }

    @Override
    public Mono<SignatureProofDTO> getSignatureProof(Long signatureRequestId) {
        log.info("Getting signature proof for request ID: {} with default provider", signatureRequestId);
        
        return signatureRequestRepository.findById(signatureRequestId)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature request not found with ID: " + signatureRequestId)))
                .flatMap(signatureRequest -> {
                    // Get the default provider and retrieve the signature proof
                    return providerRegistry.getDefaultProvider()
                            .getSignatureProof(signatureRequestId, signatureRequest.getExternalSignatureId());
                });
    }

    @Override
    public Mono<Boolean> validateSignatureProof(SignatureProofDTO signatureProofDTO, String providerName) {
        log.info("Validating signature proof with provider: {}", providerName);
        
        // Get the provider and validate the signature proof
        return providerRegistry.getProvider(providerName)
                .validateSignatureProof(signatureProofDTO);
    }

    @Override
    public Mono<Boolean> validateSignatureProof(SignatureProofDTO signatureProofDTO) {
        log.info("Validating signature proof with default provider");
        
        // Get the default provider and validate the signature proof
        return providerRegistry.getDefaultProvider()
                .validateSignatureProof(signatureProofDTO);
    }
}