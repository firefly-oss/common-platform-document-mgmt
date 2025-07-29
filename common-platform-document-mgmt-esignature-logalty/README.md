# Common Platform Document Management - Logalty E-Signature Module

This module provides an implementation of the `ESignatureProvider` interface for the Logalty e-signature service.

## Table of Contents

- [Overview](#overview)
- [Hexagonal Architecture](#hexagonal-architecture)
- [Implementation Details](#implementation-details)
- [Configuration](#configuration)
- [Usage](#usage)
- [Error Handling](#error-handling)
- [Signature Workflow](#signature-workflow)

## Overview

The Logalty E-Signature module is an adapter in the hexagonal architecture that implements the `ESignatureProvider` interface to integrate with the Logalty e-signature service. This module allows the document management system to use Logalty for digital signatures without modifying the core business logic.

## Hexagonal Architecture

In the hexagonal architecture pattern:
- The `ESignatureProvider` interface in the core module defines the port for e-signature operations
- The `LogaltyProviderImpl` class in this module is an adapter that implements the port for the Logalty service
- The adapter is registered with the `ESignatureProviderRegistry` in the core module, allowing the application to use it dynamically

This separation of concerns allows the core business logic to remain independent of the specific e-signature implementation, making it easier to add or replace e-signature providers without affecting the rest of the application.

## Implementation Details

The `LogaltyProviderImpl` class implements all methods defined in the `ESignatureProvider` interface:

```java
@Component
public class LogaltyProviderImpl implements ESignatureProvider {
    @Override
    public Mono<SignatureRequestDTO> initiateSignatureRequest(SignatureRequestDTO signatureRequest);
    
    @Override
    public Mono<SignatureRequestDTO> getSignatureRequestStatus(Long signatureRequestId, String externalSignatureId);
    
    @Override
    public Mono<Void> cancelSignatureRequest(Long signatureRequestId, String externalSignatureId);
    
    @Override
    public Mono<SignatureProofDTO> getSignatureProof(Long signatureRequestId, String externalSignatureId);
    
    @Override
    public Mono<Boolean> validateSignatureProof(SignatureProofDTO signatureProof);
    
    @Override
    public String getProviderName();
}
```

The implementation uses the WebClient from Spring WebFlux to make HTTP requests to the Logalty API, providing the following functionality:

- **Initiate Signature Request**: Sends a request to Logalty to initiate a signature process for a document
- **Get Signature Request Status**: Retrieves the current status of a signature request from Logalty
- **Cancel Signature Request**: Cancels an ongoing signature request in Logalty
- **Get Signature Proof**: Retrieves the proof of signature from Logalty
- **Validate Signature Proof**: Validates the authenticity of a signature proof with Logalty

The implementation is reactive, using Project Reactor to provide non-blocking, asynchronous operations.

## Configuration

The Logalty e-signature provider is configured using the following properties in the application configuration:

```yaml
esignature:
  logalty:
    api-url: https://api.logalty.com
    api-key: your-api-key
    api-secret: your-api-secret
```

These properties are injected into the `LogaltyProviderImpl` constructor:

```java
public LogaltyProviderImpl(
        @Value("${esignature.logalty.api-url}") String apiUrl,
        @Value("${esignature.logalty.api-key}") String apiKey,
        @Value("${esignature.logalty.api-secret}") String apiSecret) {
    // ...
}
```

## Usage

The Logalty e-signature provider is automatically registered with the `ESignatureProviderRegistry` when the application starts. It can be used by injecting the registry and requesting the provider by name:

```java
@Service
public class MyService {
    private final ESignatureProviderRegistry providerRegistry;
    
    public MyService(ESignatureProviderRegistry providerRegistry) {
        this.providerRegistry = providerRegistry;
    }
    
    public Mono<SignatureRequestDTO> initiateSignature(SignatureRequestDTO request) {
        ESignatureProvider logaltyProvider = providerRegistry.getProvider("Logalty");
        return logaltyProvider.initiateSignatureRequest(request);
    }
}
```

Alternatively, the default provider can be used if Logalty is configured as the default:

```java
ESignatureProvider defaultProvider = providerRegistry.getDefaultProvider();
return defaultProvider.initiateSignatureRequest(request);
```

## Error Handling

The Logalty e-signature provider handles various error scenarios:

- **Authentication Errors**: If the API credentials are invalid, the provider will throw an exception
- **Service Unavailable**: If the Logalty service is unavailable, the provider will throw an exception
- **Invalid Request**: If the signature request is invalid, the provider will throw an exception
- **Not Found**: If a requested signature or proof does not exist, the provider will throw an exception
- **Network Errors**: If there are network issues when communicating with Logalty, the provider will throw an exception

All errors are logged and propagated as reactive errors, allowing the calling code to handle them using standard reactive error handling mechanisms.

## Signature Workflow

The typical workflow for using the Logalty e-signature provider is as follows:

1. **Initiate Signature Request**:
   ```java
   SignatureRequestDTO request = SignatureRequestDTO.builder()
       .documentId(123L)
       .requestDate(LocalDateTime.now())
       .signatoryOrder(1L)
       .interveningParties("Party A, Party B")
       .build();
   
   Mono<SignatureRequestDTO> result = logaltyProvider.initiateSignatureRequest(request);
   ```

2. **Check Signature Status**:
   ```java
   Mono<SignatureRequestDTO> status = logaltyProvider.getSignatureRequestStatus(
       request.getId(),
       request.getExternalSignatureId()
   );
   ```

3. **Get Signature Proof** (once the signature is complete):
   ```java
   Mono<SignatureProofDTO> proof = logaltyProvider.getSignatureProof(
       request.getId(),
       request.getExternalSignatureId()
   );
   ```

4. **Validate Signature Proof**:
   ```java
   Mono<Boolean> isValid = logaltyProvider.validateSignatureProof(proof);
   ```

The Logalty provider handles the communication with the Logalty service for each of these steps, allowing the application to focus on the business logic rather than the details of the Logalty API.