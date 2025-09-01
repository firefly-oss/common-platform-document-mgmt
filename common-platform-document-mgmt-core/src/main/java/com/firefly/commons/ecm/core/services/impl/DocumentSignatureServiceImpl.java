package com.firefly.commons.ecm.core.services.impl;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.filters.FilterUtils;
import com.firefly.common.core.queries.PaginationResponse;

import com.firefly.commons.ecm.core.mappers.DocumentSignatureMapper;
import com.firefly.commons.ecm.core.mappers.EcmDomainMapper;
import com.firefly.commons.ecm.core.services.DocumentSignatureService;
import com.firefly.commons.ecm.core.validation.EcmParameterValidator;
import com.firefly.commons.ecm.core.services.SignatureRequestService;
import com.firefly.commons.ecm.interfaces.dtos.DocumentSignatureDTO;
import com.firefly.commons.ecm.interfaces.dtos.SignatureRequestDTO;
import com.firefly.commons.ecm.interfaces.enums.SignatureStatus;
import com.firefly.commons.ecm.models.entities.DocumentSignature;
import com.firefly.commons.ecm.models.repositories.DocumentSignatureRepository;
import com.firefly.core.ecm.service.EcmPortProvider;
import com.firefly.core.ecm.port.esignature.SignatureRequestPort;
import com.firefly.core.ecm.port.esignature.SignatureEnvelopePort;
import com.firefly.core.ecm.domain.model.esignature.SignatureRequest;
import com.firefly.core.ecm.domain.enums.esignature.SignatureRequestStatus;
import com.firefly.core.ecm.domain.enums.esignature.SignatureRequestType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
/**
 * Implementation of the DocumentSignatureService interface.
 * Provides comprehensive document signature management with ECM port integration.
 */
@Service
@Transactional
@Slf4j
public class DocumentSignatureServiceImpl implements DocumentSignatureService {

    @Autowired
    private DocumentSignatureRepository repository;

    @Autowired
    private DocumentSignatureMapper mapper;

    @Autowired
    private EcmPortProvider ecmPortProvider;

    @Autowired
    private SignatureRequestService signatureRequestService;

    @Autowired
    private EcmDomainMapper ecmDomainMapper;

    @Autowired
    private EcmParameterValidator ecmParameterValidator;

    @Override
    public Mono<DocumentSignatureDTO> getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<DocumentSignatureDTO>> filter(FilterRequest<DocumentSignatureDTO> filterRequest) {
        return FilterUtils.createFilter(
                DocumentSignature.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<DocumentSignatureDTO> update(DocumentSignatureDTO documentSignature) {
        if (documentSignature.getId() == null) {
            return Mono.error(new IllegalArgumentException("ID cannot be null for update operation"));
        }

        return repository.findById(documentSignature.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Document signature not found with ID: " + documentSignature.getId())))
                .flatMap(existingEntity -> {
                    DocumentSignature entityToUpdate = mapper.toEntity(documentSignature);
                    // Preserve created info
                    entityToUpdate.setCreatedAt(existingEntity.getCreatedAt());
                    entityToUpdate.setCreatedBy(existingEntity.getCreatedBy());
                    return repository.save(entityToUpdate);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<DocumentSignatureDTO> create(DocumentSignatureDTO documentSignature) {
        // Ensure ID is null for create operation
        documentSignature.setId(null);

        DocumentSignature entity = mapper.toEntity(documentSignature);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Document signature not found with ID: " + id)))
                .flatMap(entity -> repository.delete(entity));
    }

    @Override
    public Flux<DocumentSignatureDTO> getByDocumentId(UUID documentId) {
        return repository.findByDocumentId(documentId)
                .map(mapper::toDTO);
    }

    @Override
    public Flux<DocumentSignatureDTO> getByDocumentVersionId(UUID documentVersionId) {
        return repository.findByDocumentVersionId(documentVersionId)
                .map(mapper::toDTO);
    }

    @Override
    public Flux<DocumentSignatureDTO> getBySignerPartyId(UUID signerPartyId) {
        return repository.findBySignerPartyId(signerPartyId)
                .map(mapper::toDTO);
    }

    @Override
    public Flux<DocumentSignatureDTO> getBySignatureStatus(SignatureStatus signatureStatus) {
        return repository.findBySignatureStatus(signatureStatus)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<DocumentSignatureDTO> initiateSigningProcess(DocumentSignatureDTO documentSignature) {
        // Validate ECM parameters before processing
        List<String> validationErrors = ecmParameterValidator.validateDocumentSignatureEcmParameters(documentSignature);
        if (!validationErrors.isEmpty()) {
            String errorMessage = "ECM parameter validation failed: " + String.join(", ", validationErrors);
            log.error("Validation failed for signature request: {}", errorMessage);
            return Mono.error(new IllegalArgumentException(errorMessage));
        }

        // Set initial status for a new signature
        documentSignature.setSignatureStatus(SignatureStatus.PENDING);

        // Create the signature in database first
        return create(documentSignature)
                .flatMap(createdSignature -> {
                    // Initiate signature request using ECM port if available
                    java.util.UUID signatureUuid = java.util.UUID.fromString(createdSignature.getId().toString());
                    java.util.UUID documentUuid = java.util.UUID.fromString(createdSignature.getDocumentId().toString());

                    // Create ECM signature request if port is available
                    return ecmPortProvider.getSignatureRequestPort()
                            .map(port -> {
                                log.info("Using ECM SignatureRequestPort to create signature request for signature: {}", createdSignature.getId());

                                // Create ECM SignatureRequest domain object using the mapper
                                // This uses values from the DTO when available, falls back to configuration defaults
                                SignatureRequest ecmSignatureRequest = ecmDomainMapper.toEcmSignatureRequest(
                                    createdSignature, documentUuid);

                                // Create signature request through ECM port
                                return port.createSignatureRequest(ecmSignatureRequest)
                                        .flatMap(externalSignatureId -> {
                                            log.info("ECM signature request created with external ID: {} for signature: {}",
                                                    externalSignatureId, createdSignature.getId());

                                            // Update the signature DTO with ECM response data
                                            DocumentSignatureDTO updatedSignature = ecmDomainMapper.updateWithEcmResponse(
                                                createdSignature, externalSignatureId.toString(), null);

                                            // Create local SignatureRequest to track the ECM request
                                            SignatureRequestDTO signatureRequestDTO = SignatureRequestDTO.builder()
                                                    .documentSignatureId(createdSignature.getId())
                                                    .requestReference(externalSignatureId.toString())
                                                    .requestStatus(SignatureStatus.PENDING)
                                                    .requestMessage("Signature request created via ECM")
                                                    .expirationDate(createdSignature.getExpirationDate())
                                                    .tenantId(createdSignature.getTenantId())
                                                    .build();

                                            // Save the signature request to track ECM integration
                                            return signatureRequestService.create(signatureRequestDTO)
                                                    .then(Mono.just(updatedSignature));
                                        })
                                        .doOnSuccess(signature -> log.info("ECM signature request initiated successfully for ID: {}", signature.getId()))
                                        .doOnError(error -> {
                                            log.error("Failed to create ECM signature request for signature ID {}: {}",
                                                    createdSignature.getId(), error.getMessage(), error);
                                        });
                            })
                            .orElse(
                                // Fallback: signature created in database only
                                Mono.just(createdSignature)
                                        .doOnNext(signature -> {
                                            log.warn("Signature request created locally (ECM SignatureRequestPort not available) for ID: {}",
                                                    signature.getId());
                                        })
                            );
                });
    }

    @Override
    public Mono<DocumentSignatureDTO> cancelSignature(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Document signature not found with ID: " + id)))
                .flatMap(entity -> {
                    // Only allow cancellation of signatures that are not signed
                    if (entity.getSignatureStatus() == SignatureStatus.SIGNED) {
                        return Mono.error(new IllegalStateException("Cannot cancel a signed signature"));
                    }

                    // Update status to canceled in database first
                    entity.setSignatureStatus(SignatureStatus.CANCELED);
                    return repository.save(entity)
                            .flatMap(savedEntity -> {
                                // Cancel signature request using ECM port if available
                                java.util.UUID signatureUuid = java.util.UUID.fromString(savedEntity.getId().toString());

                                // Check if ECM SignatureRequestPort is available for cancellation
                                return ecmPortProvider.getSignatureRequestPort()
                                        .map(port -> {
                                            log.debug("ECM SignatureRequestPort available for cancellation of signature: {}", savedEntity.getId());

                                            // Attempt ECM cancellation using the signature ID
                                            return port.deleteSignatureRequest(signatureUuid)
                                                    .doOnSuccess(result -> log.info("ECM signature request canceled successfully for signature ID: {}", savedEntity.getId()))
                                                    .doOnError(error -> log.warn("Failed to cancel ECM signature request for signature ID {}: {}",
                                                            savedEntity.getId(), error.getMessage()))
                                                    .onErrorComplete() // Continue even if ECM cancellation fails
                                                    .then(Mono.just(savedEntity));
                                        })
                                        .orElse(
                                            // Fallback: signature canceled in database only
                                            Mono.just(savedEntity)
                                                    .doOnNext(signature -> {
                                                        log.warn("Signature request canceled locally (ECM SignatureRequestPort not available) for ID: {}",
                                                                signature.getId());
                                                    })
                                        );
                            });
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Boolean> isDocumentFullySigned(UUID documentId) {
        // Check if there are any pending signatures for the document
        return repository.findByDocumentIdAndSignatureStatus(documentId, SignatureStatus.PENDING)
                .count()
                .map(count -> count == 0)
                .flatMap(noMorePendingSignatures -> {
                    if (noMorePendingSignatures) {
                        // If there are no pending signatures, check if there are any signed signatures
                        return repository.findByDocumentIdAndSignatureStatus(documentId, SignatureStatus.SIGNED)
                                .count()
                                .map(count -> count > 0);
                    } else {
                        // If there are pending signatures, the document is not fully signed
                        return Mono.just(false);
                    }
                });
    }
}
