package com.catalis.commons.ecm.core.services;

import com.catalis.commons.ecm.interfaces.dtos.SignatureRequestDTO;
import com.catalis.commons.ecm.interfaces.dtos.SignatureProofDTO;
import reactor.core.publisher.Mono;

/**
 * Service for e-signature operations.
 * This service uses the ESignatureProvider to trigger e-signature processes.
 */
public interface ESignatureService {
    
    /**
     * Initiates a signature request with the e-signature provider.
     *
     * @param signatureRequestDTO the signature request details
     * @param providerName the name of the e-signature provider to use (optional, uses default if not provided)
     * @return a Mono emitting the updated signature request with provider-specific information
     */
    Mono<SignatureRequestDTO> initiateSignature(SignatureRequestDTO signatureRequestDTO, String providerName);
    
    /**
     * Initiates a signature request with the default e-signature provider.
     *
     * @param signatureRequestDTO the signature request details
     * @return a Mono emitting the updated signature request with provider-specific information
     */
    Mono<SignatureRequestDTO> initiateSignature(SignatureRequestDTO signatureRequestDTO);
    
    /**
     * Gets the status of a signature request from the e-signature provider.
     *
     * @param signatureRequestId the ID of the signature request
     * @param providerName the name of the e-signature provider to use (optional, uses default if not provided)
     * @return a Mono emitting the updated signature request with the current status
     */
    Mono<SignatureRequestDTO> getSignatureStatus(Long signatureRequestId, String providerName);
    
    /**
     * Gets the status of a signature request from the default e-signature provider.
     *
     * @param signatureRequestId the ID of the signature request
     * @return a Mono emitting the updated signature request with the current status
     */
    Mono<SignatureRequestDTO> getSignatureStatus(Long signatureRequestId);
    
    /**
     * Cancels a signature request with the e-signature provider.
     *
     * @param signatureRequestId the ID of the signature request
     * @param providerName the name of the e-signature provider to use (optional, uses default if not provided)
     * @return a Mono that completes when the signature request is successfully canceled
     */
    Mono<Void> cancelSignature(Long signatureRequestId, String providerName);
    
    /**
     * Cancels a signature request with the default e-signature provider.
     *
     * @param signatureRequestId the ID of the signature request
     * @return a Mono that completes when the signature request is successfully canceled
     */
    Mono<Void> cancelSignature(Long signatureRequestId);
    
    /**
     * Gets the proof of signature from the e-signature provider.
     *
     * @param signatureRequestId the ID of the signature request
     * @param providerName the name of the e-signature provider to use (optional, uses default if not provided)
     * @return a Mono emitting the signature proof
     */
    Mono<SignatureProofDTO> getSignatureProof(Long signatureRequestId, String providerName);
    
    /**
     * Gets the proof of signature from the default e-signature provider.
     *
     * @param signatureRequestId the ID of the signature request
     * @return a Mono emitting the signature proof
     */
    Mono<SignatureProofDTO> getSignatureProof(Long signatureRequestId);
    
    /**
     * Validates a signature proof with the e-signature provider.
     *
     * @param signatureProofDTO the signature proof to validate
     * @param providerName the name of the e-signature provider to use (optional, uses default if not provided)
     * @return a Mono emitting a boolean indicating whether the signature proof is valid
     */
    Mono<Boolean> validateSignatureProof(SignatureProofDTO signatureProofDTO, String providerName);
    
    /**
     * Validates a signature proof with the default e-signature provider.
     *
     * @param signatureProofDTO the signature proof to validate
     * @return a Mono emitting a boolean indicating whether the signature proof is valid
     */
    Mono<Boolean> validateSignatureProof(SignatureProofDTO signatureProofDTO);
}