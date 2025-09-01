# Integration Examples

Practical code examples and common use cases for ECM integration with the Firefly OpenCore Platform Document Management Microservice.

## Document Upload with ECM Integration

### Complete Upload Flow

```java
@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    
    private final DocumentService documentService;
    
    @PostMapping("/{id}/content")
    public Mono<ResponseEntity<DocumentDTO>> uploadContent(
            @PathVariable UUID id,
            @RequestPart("file") Mono<FilePart> filePartMono) {
        
        return filePartMono
            .flatMap(filePart -> documentService.uploadContent(id, filePart))
            .map(ResponseEntity::ok)
            .onErrorResume(DocumentNotFoundException.class, 
                ex -> Mono.just(ResponseEntity.notFound().build()))
            .onErrorResume(RuntimeException.class,
                ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }
}
```

### Service Implementation with ECM

```java
@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final EcmPortProvider ecmPortProvider;

    @Override
    public Mono<DocumentDTO> uploadContent(UUID documentId, FilePart filePart) {
        return documentRepository.findById(documentId)
            .switchIfEmpty(Mono.error(new DocumentNotFoundException(documentId)))
            .flatMap(document -> processContentUpload(document, filePart))
            .map(documentMapper::toDTO);
    }

    private Mono<Document> processContentUpload(Document document, FilePart filePart) {
        return ecmPortProvider.getDocumentContentPort()
            .map(port -> uploadWithEcm(document, filePart, port))
            .orElseThrow(() -> new RuntimeException(
                "Document content upload requires ECM DocumentContentPort to be configured"));
    }

    private Mono<Document> uploadWithEcm(Document document, FilePart filePart, DocumentContentPort port) {
        return DataBufferUtils.join(filePart.content())
            .flatMap(dataBuffer -> {
                byte[] content = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(content);
                DataBufferUtils.release(dataBuffer);

                return port.storeContent(document.getId(), content, filePart.filename())
                    .doOnSuccess(path -> {
                        document.setStoragePath(path);
                        document.setFileName(filePart.filename());
                        document.setMimeType(filePart.headers().getContentType().toString());
                        document.setFileSize((long) content.length);
                        document.setStorageType(StorageType.S3);
                        document.setUpdatedAt(LocalDateTime.now());
                        document.setUpdatedBy(getCurrentUser());
                    })
                    .doOnError(error -> log.error("ECM storage failed: {}", error.getMessage()))
                    .onErrorMap(throwable -> new EcmOperationException("Failed to store content", throwable))
                    .then(documentRepository.save(document));
            });
    }

    @Override
    public Mono<Resource> downloadContent(UUID documentId) {
        return documentRepository.findById(documentId)
            .switchIfEmpty(Mono.error(new DocumentNotFoundException(documentId)))
            .flatMap(document -> {
                if (document.getStoragePath() == null) {
                    return Mono.error(new ContentNotFoundException(documentId));
                }

                return ecmPortProvider.getDocumentContentPort()
                    .map(port -> downloadWithEcm(document, port))
                    .orElseThrow(() -> new RuntimeException(
                        "Document content download requires ECM DocumentContentPort to be configured"));
            });
    }

    private Mono<Resource> downloadWithEcm(Document document, DocumentContentPort port) {
        return port.getContentStream(document.getId())
            .map(inputStream -> new InputStreamResource(inputStream) {
                @Override
                public String getFilename() {
                    return document.getFileName();
                }
            })
            .doOnError(error -> log.error("ECM retrieval failed: {}", error.getMessage()))
            .onErrorMap(throwable -> new EcmOperationException("Failed to retrieve content", throwable));
    }
}
```

## Document Download with Streaming

### Controller with Streaming Response

```java
@GetMapping("/{id}/content")
public Mono<ResponseEntity<Resource>> downloadContent(@PathVariable UUID id) {
    return documentService.downloadContent(id)
        .map(resource -> ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, 
                "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource))
        .onErrorResume(DocumentNotFoundException.class,
            ex -> Mono.just(ResponseEntity.notFound().build()))
        .onErrorResume(RuntimeException.class,
            ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
}
```

### Service Implementation with Streaming

```java
@Override
public Mono<Resource> downloadContent(UUID documentId) {
    return repository.findById(documentId)
        .switchIfEmpty(Mono.error(new DocumentNotFoundException(documentId)))
        .flatMap(document -> {
            if (document.getContentPath() == null) {
                return Mono.error(new ContentNotFoundException(documentId));
            }
            
            return ecmPortProvider.getDocumentContentPort()
                .map(port -> downloadWithEcm(document, port))
                .orElseThrow(() -> new RuntimeException(
                    "Document content download requires ECM DocumentContentPort to be configured"));
        });
}

private Mono<Resource> downloadWithEcm(Document document, DocumentContentPort port) {
    return port.getContentStream(document.getId())
        .map(inputStream -> new InputStreamResource(inputStream) {
            @Override
            public String getFilename() {
                return document.getFileName();
            }
        })
        .doOnError(error -> log.error("ECM retrieval failed: {}", error.getMessage()))
        .onErrorMap(throwable -> new EcmOperationException("Failed to retrieve content", throwable));
}
```

## Digital Signature Workflow

### Create Signature Request

```java
@PostMapping("/signature-requests")
public Mono<ResponseEntity<SignatureRequestDTO>> createSignatureRequest(
        @RequestBody @Valid SignatureRequestDTO request) {
    
    return signatureService.createSignatureRequest(request)
        .map(result -> ResponseEntity.status(HttpStatus.CREATED).body(result))
        .onErrorResume(DocumentNotFoundException.class,
            ex -> Mono.just(ResponseEntity.notFound().build()));
}
```

### Service Implementation with ECM Integration

```java
@Override
public Mono<SignatureRequestDTO> createSignatureRequest(SignatureRequestDTO request) {
    return documentRepository.findById(request.getDocumentId())
        .switchIfEmpty(Mono.error(new DocumentNotFoundException(request.getDocumentId())))
        .flatMap(document -> createSignatureRequestEntity(request, document))
        .flatMap(this::processSignatureRequest)
        .map(mapper::toDTO);
}

private Mono<SignatureRequest> createSignatureRequestEntity(SignatureRequestDTO request, Document document) {
    SignatureRequest signatureRequest = mapper.toEntity(request);
    signatureRequest.setId(UUID.randomUUID());
    signatureRequest.setCreatedAt(LocalDateTime.now());
    
    // Apply business logic defaults
    if (request.getCustomMessage() == null) {
        signatureRequest.setCustomMessage(properties.getSignature().getCustomMessage());
    }
    
    return Mono.just(signatureRequest);
}

private Mono<SignatureRequest> processSignatureRequest(SignatureRequest signatureRequest) {
    return ecmPortProvider.getSignatureRequestPort()
        .map(port -> processWithEcm(signatureRequest, port))
        .orElseGet(() -> processWithoutEcm(signatureRequest));
}

private Mono<SignatureRequest> processWithEcm(SignatureRequest request, SignatureRequestPort port) {
    return port.createSignatureRequest(
            request.getId(),
            request.getDocumentId(),
            request.getSignerEmail(),
            request.getSignatureType()
        )
        .doOnSuccess(externalId -> {
            request.setExternalRequestId(externalId);
            request.setSignatureStatus(SignatureStatus.SENT);
            log.info("Signature request sent via ECM: {}", externalId);
        })
        .doOnError(error -> {
            log.error("ECM signature request failed: {}", error.getMessage());
            request.setSignatureStatus(SignatureStatus.FAILED);
        })
        .onErrorReturn(request.getExternalRequestId())
        .then(signatureRequestRepository.save(request));
}

private Mono<SignatureRequest> processWithoutEcm(SignatureRequest request) {
    request.setSignatureStatus(SignatureStatus.PENDING);
    log.warn("SignatureRequestPort not available, storing request locally");
    return signatureRequestRepository.save(request);
}
```

## Document Versioning

### Create New Version

```java
@PostMapping("/{id}/versions")
public Mono<ResponseEntity<DocumentDTO>> createVersion(
        @PathVariable UUID id,
        @RequestPart("file") Mono<FilePart> filePartMono,
        @RequestParam(required = false) String comment) {
    
    return filePartMono
        .flatMap(filePart -> documentService.createVersion(id, filePart, comment))
        .map(ResponseEntity::ok)
        .onErrorResume(DocumentNotFoundException.class,
            ex -> Mono.just(ResponseEntity.notFound().build()));
}
```

### Service Implementation with Versioning

```java
@Override
public Mono<DocumentDTO> createVersion(UUID documentId, FilePart filePart, String comment) {
    return repository.findById(documentId)
        .switchIfEmpty(Mono.error(new DocumentNotFoundException(documentId)))
        .flatMap(document -> createNewVersion(document, filePart, comment))
        .map(mapper::toDTO);
}

private Mono<Document> createNewVersion(Document document, FilePart filePart, String comment) {
    return ecmPortProvider.getDocumentVersionPort()
        .map(port -> createVersionWithEcm(document, filePart, comment, port))
        .orElseGet(() -> createVersionWithoutEcm(document, filePart));
}

private Mono<Document> createVersionWithEcm(Document document, FilePart filePart, 
                                          String comment, DocumentVersionPort port) {
    return DataBufferUtils.join(filePart.content())
        .flatMap(dataBuffer -> {
            byte[] content = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(content);
            DataBufferUtils.release(dataBuffer);
            
            DocumentVersion version = new DocumentVersion();
            version.setDocumentId(document.getId());
            version.setVersionNumber(document.getVersion() + 1);
            version.setComment(comment);
            version.setCreatedAt(LocalDateTime.now());
            
            return port.createVersion(version, content)
                .doOnSuccess(createdVersion -> {
                    document.setVersion(createdVersion.getVersionNumber());
                    document.setFileName(filePart.filename());
                    document.setMimeType(filePart.headers().getContentType().toString());
                    document.setUpdatedAt(LocalDateTime.now());
                })
                .then(repository.save(document));
        });
}
```

## Health Check Integration

### Custom Health Indicator

```java
@Component
public class EcmHealthIndicator implements HealthIndicator {
    
    private final EcmPortProvider ecmPortProvider;
    
    @Override
    public Health health() {
        Map<String, Object> details = new HashMap<>();
        
        boolean hasContentPort = ecmPortProvider.getDocumentContentPort().isPresent();
        boolean hasVersionPort = ecmPortProvider.getDocumentVersionPort().isPresent();
        boolean hasSignaturePort = ecmPortProvider.getSignatureRequestPort().isPresent();
        boolean hasEnvelopePort = ecmPortProvider.getSignatureEnvelopePort().isPresent();
        
        details.put("documentContentPort", hasContentPort ? "available" : "not available");
        details.put("documentVersionPort", hasVersionPort ? "available" : "not available");
        details.put("signatureRequestPort", hasSignaturePort ? "available" : "not available");
        details.put("signatureEnvelopePort", hasEnvelopePort ? "available" : "not available");
        
        int availablePorts = (hasContentPort ? 1 : 0) + (hasVersionPort ? 1 : 0) + 
                           (hasSignaturePort ? 1 : 0) + (hasEnvelopePort ? 1 : 0);
        
        details.put("availablePortsCount", availablePorts);
        details.put("totalPortsCount", 4);
        
        if (availablePorts > 0) {
            return Health.up().withDetails(details).build();
        } else {
            return Health.down()
                .withDetail("reason", "No ECM ports available")
                .withDetails(details)
                .build();
        }
    }
}
```

## Configuration Examples

### Development Configuration

```yaml
# application-dev.yml
spring:
  profiles:
    active: dev

firefly:
  ecm:
    # Local file storage for development
    document-content:
      provider: local
      local:
        base-path: ./dev-documents
        create-directories: true
    
    # Business logic configuration
    integration:
      document:
        security-level: "INTERNAL"
        max-file-size: "10MB"
        allowed-mime-types:
          - "application/pdf"
          - "image/jpeg"
          - "image/png"
      signature:
        custom-message: "Development: Please sign this document"
        expiration-days: 7
      error-handling:
        fail-fast: false
        log-failures: true

logging:
  level:
    com.firefly.commons.ecm: DEBUG
    com.firefly.core.ecm: DEBUG
```

### Production Configuration

```yaml
# application-prod.yml
spring:
  profiles:
    active: prod

firefly:
  ecm:
    # S3 storage for production
    document-content:
      provider: s3
      s3:
        bucket: ${S3_BUCKET_NAME}
        region: ${AWS_REGION}
        access-key: ${AWS_ACCESS_KEY}
        secret-key: ${AWS_SECRET_KEY}
    
    # DocuSign for production signatures
    signature:
      provider: docusign
      docusign:
        integration-key: ${DOCUSIGN_INTEGRATION_KEY}
        user-id: ${DOCUSIGN_USER_ID}
        account-id: ${DOCUSIGN_ACCOUNT_ID}
        base-path: https://www.docusign.net/restapi
        private-key: ${DOCUSIGN_PRIVATE_KEY}
    
    # Business logic configuration
    integration:
      document:
        security-level: "CONFIDENTIAL"
        max-file-size: "50MB"
        retention-days: 2555
      signature:
        custom-message: "Please review and electronically sign this document"
        expiration-days: 30
        reminder-days: 7
      error-handling:
        fail-fast: false
        log-failures: true
        retry-attempts: 3
```

## Error Handling Examples

### Custom Exception Handling

```java
@RestControllerAdvice
public class EcmExceptionHandler {
    
    @ExceptionHandler(EcmOperationException.class)
    public ResponseEntity<ErrorResponse> handleEcmOperationException(EcmOperationException ex) {
        ErrorResponse error = new ErrorResponse(
            "ECM_OPERATION_FAILED",
            ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleEcmUnavailable(RuntimeException ex) {
        if (ex.getMessage().contains("requires ECM")) {
            ErrorResponse error = new ErrorResponse(
                "ECM_PORT_UNAVAILABLE",
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        throw ex; // Re-throw if not ECM-related
    }
}
```

### Retry Logic for ECM Operations

```java
@Component
public class EcmRetryTemplate {
    
    private final EcmIntegrationProperties properties;
    
    public <T> Mono<T> executeWithRetry(Supplier<Mono<T>> operation) {
        return operation.get()
            .retryWhen(Retry.backoff(
                properties.getErrorHandling().getRetryAttempts(),
                Duration.ofSeconds(1)
            ).filter(throwable -> !(throwable instanceof DocumentNotFoundException)))
            .doOnError(error -> log.error("ECM operation failed after retries: {}", error.getMessage()));
    }
}
```

## Testing Examples

### Integration Test with ECM Mocking

```java
@SpringBootTest
@TestPropertySource(properties = {
    "firefly.ecm.integration.error-handling.fail-fast=false"
})
class DocumentServiceIntegrationTest {
    
    @Autowired
    private DocumentService documentService;
    
    @MockBean
    private EcmPortProvider ecmPortProvider;
    
    @Test
    void uploadContent_WithEcmAvailable_ShouldStoreContent() {
        // Given
        DocumentContentPort mockPort = mock(DocumentContentPort.class);
        when(ecmPortProvider.getDocumentContentPort()).thenReturn(Optional.of(mockPort));
        when(mockPort.storeContent(any(), any(), any())).thenReturn(Mono.just("/path/to/content"));
        
        // When & Then
        StepVerifier.create(documentService.uploadContent(documentId, filePart))
            .expectNextMatches(dto -> dto.getContentPath() != null)
            .verifyComplete();
    }
    
    @Test
    void uploadContent_WithEcmUnavailable_ShouldThrowException() {
        // Given
        when(ecmPortProvider.getDocumentContentPort()).thenReturn(Optional.empty());
        
        // When & Then
        StepVerifier.create(documentService.uploadContent(documentId, filePart))
            .expectErrorMatches(throwable -> 
                throwable instanceof RuntimeException && 
                throwable.getMessage().contains("requires ECM DocumentContentPort"))
            .verify();
    }
}
```
