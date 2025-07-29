# Common Platform Document Management - Web Module

This module provides the web layer for the document management system, including REST controllers, request/response handling, and application configuration.

## Table of Contents

- [Overview](#overview)
- [Hexagonal Architecture](#hexagonal-architecture)
- [REST Controllers](#rest-controllers)
- [Exception Handling](#exception-handling)
- [Security](#security)
- [Application Configuration](#application-configuration)
- [Running the Application](#running-the-application)

## Overview

The Web module is the entry point for HTTP requests to the document management system. It exposes the functionality of the core domain as a RESTful API, handling request parsing, validation, authentication, authorization, and response formatting.

## Hexagonal Architecture

In the hexagonal architecture pattern, the web module serves as an adapter for the primary (driving) side:

- It translates HTTP requests into calls to the core domain services
- It adapts the responses from the core domain into HTTP responses
- It isolates the core domain from the specifics of HTTP communication

This separation provides several benefits:
- **Isolation**: The core domain doesn't need to know about HTTP, REST, or web-specific concerns
- **Testability**: The web layer can be tested independently of the core domain
- **Flexibility**: The API can evolve without affecting the core business logic

## REST Controllers

The module includes REST controllers for various resources:

- **DocumentController**: Endpoints for document operations
- **DocumentVersionController**: Endpoints for document version operations
- **DocumentTagController**: Endpoints for document tag operations
- **DocumentReferenceController**: Endpoints for document reference operations
- **SignatureRequestController**: Endpoints for signature request operations
- **SignatureProofController**: Endpoints for signature proof operations
- **StorageController**: Endpoints for storage operations, including file uploads and downloads

The StorageController provides endpoints for the two-bucket storage system:
- `/api/v1/storage/upload`: Uploads a file directly to the private bucket (for internal use)
- `/api/v1/storage/upload/public`: Uploads a file to the public bucket (for channel uploads)
- `/api/v1/storage/move-to-private`: Moves a file from the public bucket to the private bucket
- `/api/v1/storage/download`: Downloads a file from either bucket
- `/api/v1/storage/exists`: Checks if a file exists in either bucket
- `/api/v1/storage/presigned-url`: Generates a pre-signed URL for temporary access to a file

Example of a controller:

```java
@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;
    
    @GetMapping
    public Flux<DocumentDTO> getAllDocuments() {
        return documentService.getAllDocuments();
    }
    
    @GetMapping("/{id}")
    public Mono<DocumentDTO> getDocument(@PathVariable Long id) {
        return documentService.getDocument(id);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<DocumentDTO> createDocument(@RequestBody @Valid DocumentDTO documentDTO) {
        return documentService.createDocument(documentDTO);
    }
    
    @PutMapping("/{id}")
    public Mono<DocumentDTO> updateDocument(@PathVariable Long id, @RequestBody @Valid DocumentDTO documentDTO) {
        return documentService.updateDocument(id, documentDTO);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteDocument(@PathVariable Long id) {
        return documentService.deleteDocument(id);
    }
    
    @PostMapping("/filter")
    public Mono<PaginationResponseDocumentDTO> filterDocuments(@RequestBody @Valid FilterRequestDocumentDTO filterRequest) {
        return documentService.filterDocuments(filterRequest);
    }
}
```

The controllers use Spring WebFlux for reactive request handling, providing non-blocking, asynchronous processing of HTTP requests.

## Exception Handling

The module includes a global exception handler that translates exceptions into appropriate HTTP responses:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return Mono.just(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }
    
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleValidationException(ValidationException ex) {
        return Mono.just(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }
    
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ValidationErrorResponse> handleWebExchangeBindException(WebExchangeBindException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        
        return Mono.just(new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation failed", fieldErrors));
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ErrorResponse> handleGenericException(Exception ex) {
        return Mono.just(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred"));
    }
}
```

This ensures that clients receive appropriate error responses with meaningful status codes and messages.

## Security

The module includes security configuration for authentication and authorization:

```java
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .authorizeExchange()
                    .pathMatchers("/api/v1/public/**").permitAll()
                    .pathMatchers("/actuator/**").permitAll()
                    .pathMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .anyExchange().authenticated()
                .and()
                .oauth2ResourceServer()
                    .jwt()
                .and().and()
                .build();
    }
    
    @Bean
    public ReactiveJwtDecoder jwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri) {
        return ReactiveJwtDecoders.fromIssuerLocation(issuerUri);
    }
}
```

The security configuration:
- Secures all endpoints except those explicitly marked as public
- Uses OAuth2/JWT for authentication
- Supports role-based authorization
- Allows access to Swagger UI and API documentation without authentication

## Application Configuration

The module includes the main application configuration:

```java
@SpringBootApplication
@ComponentScan("com.catalis.commons.ecm")
public class DocumentManagementApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(DocumentManagementApplication.class, args);
    }
}
```

It also includes configuration for various application properties in `application.yaml`:

```yaml
spring:
  application:
    name: common-platform-document-mgmt
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/document_mgmt
    username: postgres
    password: postgres
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://auth.example.com/realms/document-mgmt

server:
  port: 8080

storage:
  default-provider: AmazonS3
  s3:
    endpoint: https://s3.amazonaws.com
    region: us-east-1
    access-key: your-access-key
    secret-key: your-secret-key
    private-bucket-name: your-private-bucket-name
    public-bucket-name: your-public-bucket-name

esignature:
  default-provider: Logalty
  logalty:
    api-url: https://api.logalty.com
    api-key: your-api-key
    api-secret: your-api-secret

grpc:
  server:
    port: 9090
    security:
      enabled: false

logging:
  level:
    com.catalis.commons.ecm: INFO
    org.springframework: INFO
```

## Running the Application

To run the application:

1. Ensure you have Java 17 or higher installed
2. Configure the application properties in `application.yaml`
3. Build the application:
   ```bash
   ./mvnw clean package
   ```
4. Run the application:
   ```bash
   java -jar target/common-platform-document-mgmt-web.jar
   ```

The application will start and listen for HTTP requests on the configured port (default: 8080).

You can access the Swagger UI at `http://localhost:8080/swagger-ui.html` to explore and test the API.

For development purposes, you can also run the application directly from your IDE by running the `DocumentManagementApplication` class.