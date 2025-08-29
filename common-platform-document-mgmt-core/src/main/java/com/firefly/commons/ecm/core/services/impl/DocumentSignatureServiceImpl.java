package com.firefly.commons.ecm.core.services.impl;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.filters.FilterUtils;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.core.mappers.DocumentSignatureMapper;
import com.firefly.commons.ecm.core.services.DocumentSignatureService;
import com.firefly.commons.ecm.interfaces.dtos.DocumentSignatureDTO;
import com.firefly.commons.ecm.interfaces.enums.SignatureStatus;
import com.firefly.commons.ecm.models.entities.DocumentSignature;
import com.firefly.commons.ecm.models.repositories.DocumentSignatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of the DocumentSignatureService interface.
 */
@Service
@Transactional
public class DocumentSignatureServiceImpl implements DocumentSignatureService {

    @Autowired
    private DocumentSignatureRepository repository;

    @Autowired
    private DocumentSignatureMapper mapper;

    @Override
    public Mono<DocumentSignatureDTO> getById(Long id) {
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
    public Mono<Void> delete(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Document signature not found with ID: " + id)))
                .flatMap(entity -> repository.delete(entity));
    }

    @Override
    public Flux<DocumentSignatureDTO> getByDocumentId(Long documentId) {
        return repository.findByDocumentId(documentId)
                .map(mapper::toDTO);
    }

    @Override
    public Flux<DocumentSignatureDTO> getByDocumentVersionId(Long documentVersionId) {
        return repository.findByDocumentVersionId(documentVersionId)
                .map(mapper::toDTO);
    }

    @Override
    public Flux<DocumentSignatureDTO> getBySignerPartyId(Long signerPartyId) {
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
        // Set initial status for a new signature
        documentSignature.setSignatureStatus(SignatureStatus.PENDING);

        // Create the signature
        return create(documentSignature);
    }

    @Override
    public Mono<DocumentSignatureDTO> cancelSignature(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Document signature not found with ID: " + id)))
                .flatMap(entity -> {
                    // Only allow cancellation of signatures that are not signed
                    if (entity.getSignatureStatus() == SignatureStatus.SIGNED) {
                        return Mono.error(new IllegalStateException("Cannot cancel a signed signature"));
                    }

                    // Update status to canceled
                    entity.setSignatureStatus(SignatureStatus.CANCELED);
                    return repository.save(entity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Boolean> isDocumentFullySigned(Long documentId) {
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
