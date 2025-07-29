package com.catalis.commons.ecm.core.providers.esignature;

import com.catalis.commons.ecm.interfaces.dtos.SignatureRequestDTO;
import com.catalis.commons.ecm.interfaces.dtos.SignatureProofDTO;
import reactor.core.publisher.Mono;

/**
 * Provider interface for e-signature services.
 * This interface defines the contract that all e-signature provider implementations must follow.
 */
public interface ESignatureProvider {
    
    /**
     * Initiates a signature request with the e-signature service.
     *
     * @param signatureRequest the signature request details
     * @return a Mono emitting the updated signature request with provider-specific information
     */
    Mono<SignatureRequestDTO> initiateSignatureRequest(SignatureRequestDTO signatureRequest);
    
    /**
     * Retrieves the current status of a signature request from the e-signature service.
     *
     * @param signatureRequestId the internal ID of the signature request
     * @param externalSignatureId the ID of the signature request in the external e-signature service
     * @return a Mono emitting the updated signature request with the current status
     */
    Mono<SignatureRequestDTO> getSignatureRequestStatus(Long signatureRequestId, String externalSignatureId);
    
    /**
     * Cancels an existing signature request in the e-signature service.
     *
     * @param signatureRequestId the internal ID of the signature request
     * @param externalSignatureId the ID of the signature request in the external e-signature service
     * @return a Mono that completes when the signature request is successfully canceled
     */
    Mono<Void> cancelSignatureRequest(Long signatureRequestId, String externalSignatureId);
    
    /**
     * Retrieves the proof of signature from the e-signature service.
     *
     * @param signatureRequestId the internal ID of the signature request
     * @param externalSignatureId the ID of the signature request in the external e-signature service
     * @return a Mono emitting the signature proof
     */
    Mono<SignatureProofDTO> getSignatureProof(Long signatureRequestId, String externalSignatureId);
    
    /**
     * Validates the signature proof with the e-signature service.
     *
     * @param signatureProof the signature proof to validate
     * @return a Mono emitting a boolean indicating whether the signature proof is valid
     */
    Mono<Boolean> validateSignatureProof(SignatureProofDTO signatureProof);
    
    /**
     * Gets the name of the provider.
     *
     * @return the name of the e-signature provider
     */
    String getProviderName();
}