package com.firefly.commons.ecm.core.services.impl;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.filters.FilterUtils;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.core.mappers.SignatureVerificationMapper;
import com.firefly.commons.ecm.core.services.DocumentSignatureService;
import com.firefly.commons.ecm.core.services.SignatureVerificationService;
import com.firefly.commons.ecm.interfaces.dtos.DocumentSignatureDTO;
import com.firefly.commons.ecm.interfaces.dtos.SignatureVerificationDTO;
import com.firefly.commons.ecm.interfaces.enums.VerificationStatus;
import com.firefly.commons.ecm.models.entities.SignatureVerification;
import com.firefly.commons.ecm.models.repositories.SignatureVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;
import java.time.LocalDateTime;

/**
 * Implementation of the SignatureVerificationService interface.
 */
@Service
@Transactional
public class SignatureVerificationServiceImpl implements SignatureVerificationService {

    @Autowired
    private SignatureVerificationRepository repository;

    @Autowired
    private SignatureVerificationMapper mapper;

    @Autowired
    private DocumentSignatureService documentSignatureService;

    @Override
    public Mono<SignatureVerificationDTO> getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<SignatureVerificationDTO>> filter(FilterRequest<SignatureVerificationDTO> filterRequest) {
        return FilterUtils.createFilter(
                SignatureVerification.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<SignatureVerificationDTO> update(SignatureVerificationDTO signatureVerification) {
        if (signatureVerification.getId() == null) {
            return Mono.error(new IllegalArgumentException("ID cannot be null for update operation"));
        }

        return repository.findById(signatureVerification.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Signature verification not found with ID: " + signatureVerification.getId())))
                .flatMap(existingEntity -> {
                    SignatureVerification entityToUpdate = mapper.toEntity(signatureVerification);
                    // Preserve created info
                    entityToUpdate.setCreatedAt(existingEntity.getCreatedAt());
                    entityToUpdate.setCreatedBy(existingEntity.getCreatedBy());
                    return repository.save(entityToUpdate);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<SignatureVerificationDTO> create(SignatureVerificationDTO signatureVerification) {
        // Ensure ID is null for create operation
        signatureVerification.setId(null);

        // Set default values if not provided
        if (signatureVerification.getVerificationTimestamp() == null) {
            signatureVerification.setVerificationTimestamp(LocalDateTime.now());
        }
        if (signatureVerification.getVerificationStatus() == null) {
            signatureVerification.setVerificationStatus(VerificationStatus.NOT_VERIFIED);
        }

        SignatureVerification entity = mapper.toEntity(signatureVerification);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature verification not found with ID: " + id)))
                .flatMap(entity -> repository.delete(entity));
    }

    @Override
    public Flux<SignatureVerificationDTO> getByDocumentSignatureId(UUID documentSignatureId) {
        return repository.findByDocumentSignatureId(documentSignatureId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<SignatureVerificationDTO> getLatestVerification(UUID documentSignatureId) {
        return repository.findFirstByDocumentSignatureIdOrderByVerificationTimestampDesc(documentSignatureId)
                .map(mapper::toDTO);
    }

    @Override
    public Flux<SignatureVerificationDTO> getByVerificationStatus(VerificationStatus verificationStatus) {
        return repository.findByVerificationStatus(verificationStatus)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<SignatureVerificationDTO> verifySignature(UUID documentSignatureId) {
        // In a real implementation, this would call a signature verification service
        // For now, we'll just create a verification record with a random status
        return documentSignatureService.getById(documentSignatureId)
                .switchIfEmpty(Mono.error(new RuntimeException("Document signature not found with ID: " + documentSignatureId)))
                .flatMap(signature -> {
                    SignatureVerificationDTO verification = SignatureVerificationDTO.builder()
                            .documentSignatureId(documentSignatureId)
                            .verificationStatus(VerificationStatus.VALID) // In a real implementation, this would be determined by the verification process
                            .verificationDetails("Signature verified successfully")
                            .verificationProvider("Mock Provider")
                            .verificationTimestamp(LocalDateTime.now())
                            .certificateValid(true)
                            .certificateDetails("Mock certificate details")
                            .certificateIssuer("Mock Certificate Authority")
                            .certificateSubject("CN=Mock Signer, O=Mock Organization, C=US")
                            .certificateValidFrom(LocalDateTime.now().minusYears(1))
                            .certificateValidUntil(LocalDateTime.now().plusYears(1))
                            .documentIntegrityValid(true)
                            .tenantId(signature.getTenantId())
                            .build();

                    return create(verification);
                });
    }

    @Override
    public Flux<SignatureVerificationDTO> verifyAllSignaturesForDocument(UUID documentId) {
        // Get all signatures for the document and verify each one
        return documentSignatureService.getByDocumentId(documentId)
                .flatMap(signature -> verifySignature(signature.getId()));
    }
}
