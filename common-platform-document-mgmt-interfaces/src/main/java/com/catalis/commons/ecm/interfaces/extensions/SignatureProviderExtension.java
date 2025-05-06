package com.catalis.commons.ecm.interfaces.extensions;

import com.catalis.commons.ecm.interfaces.dtos.*;
import com.catalis.core.plugin.annotation.ExtensionPoint;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Extension point to be implemented by plugins that integrate with Qualified Trust Service Providers (QTSP)
 * for handling the full lifecycle of digital signatures.
 */
@ExtensionPoint(
        id = "com.catalis.commons.ecm.interfaces.extensions.signature-provider",
        description = "Extension point for integrating QTSPs like DocuSign, ValidatedID, etc.",
        allowMultiple = false
)
public interface SignatureProviderExtension {

    /**
     * Sends a signature request to the provider for the given document and signer.
     *
     * @param requestDTO the signature request data
     * @param signatureDTO the document signature data
     * @param documentDTO the document being signed
     * @return a Mono signaling completion or error
     */
    Mono<Void> sendSignatureRequest(SignatureRequestDTO requestDTO, DocumentSignatureDTO signatureDTO,
                                    DocumentDTO documentDTO);

    /**
     * Cancels a previously submitted signature request.
     *
     * @param providerRequestId the external provider request ID
     * @return a Mono signaling completion or error
     */
    Mono<Void> cancelSignatureRequest(String providerRequestId);

    /**
     * Retrieves the current status of a signature request from the provider.
     *
     * @param providerRequestId the external provider request ID
     * @return a Mono with the updated SignatureRequestDTO
     */
    Mono<SignatureRequestDTO> fetchSignatureRequestStatus(String providerRequestId);

    /**
     * Verifies the final status and integrity of a completed signature.
     *
     * @param documentSignatureId the internal ID of the signature to verify
     * @return a Mono containing the signature verification result
     */
    Mono<SignatureVerificationDTO> verifySignature(Long documentSignatureId);

    /**
     * Returns a list of signers associated with a given request.
     *
     * @param providerRequestId the external provider request ID
     * @return a Flux of DocumentSignatureDTO representing each signer
     */
    Flux<DocumentSignatureDTO> fetchSigners(String providerRequestId);

    /**
     * Gets the metadata for this signature provider (e.g. name, code, version).
     *
     * @return the provider descriptor
     */
    Mono<SignatureProviderDTO> getProviderMetadata();

    /**
     * Returns a unique code identifying this provider (e.g. "docusign").
     *
     * @return the provider code
     */
    String getProviderCode();

    /**
     * Validates whether this provider supports the given signature request.
     *
     * @param requestDTO the signature request
     * @return true if this provider can handle the request
     */
    boolean supports(SignatureRequestDTO requestDTO);
}