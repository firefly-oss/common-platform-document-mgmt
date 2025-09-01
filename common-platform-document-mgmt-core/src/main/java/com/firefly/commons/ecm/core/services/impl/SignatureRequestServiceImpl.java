package com.firefly.commons.ecm.core.services.impl;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.filters.FilterUtils;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.core.mappers.SignatureRequestMapper;
import com.firefly.commons.ecm.core.services.SignatureRequestService;
import com.firefly.commons.ecm.interfaces.dtos.SignatureRequestDTO;
import com.firefly.commons.ecm.interfaces.enums.SignatureStatus;
import com.firefly.commons.ecm.models.entities.SignatureRequest;
import com.firefly.commons.ecm.models.repositories.SignatureRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;
import java.time.LocalDateTime;

/**
 * Implementation of the SignatureRequestService interface.
 */
@Service
@Transactional
public class SignatureRequestServiceImpl implements SignatureRequestService {

    @Autowired
    private SignatureRequestRepository repository;

    @Autowired
    private SignatureRequestMapper mapper;

    @Override
    public Mono<SignatureRequestDTO> getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<SignatureRequestDTO>> filter(FilterRequest<SignatureRequestDTO> filterRequest) {
        return FilterUtils.createFilter(
                SignatureRequest.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<SignatureRequestDTO> update(SignatureRequestDTO signatureRequest) {
        if (signatureRequest.getId() == null) {
            return Mono.error(new IllegalArgumentException("ID cannot be null for update operation"));
        }

        return repository.findById(signatureRequest.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Signature request not found with ID: " + signatureRequest.getId())))
                .flatMap(existingEntity -> {
                    SignatureRequest entityToUpdate = mapper.toEntity(signatureRequest);
                    // Preserve created info
                    entityToUpdate.setCreatedAt(existingEntity.getCreatedAt());
                    entityToUpdate.setCreatedBy(existingEntity.getCreatedBy());
                    return repository.save(entityToUpdate);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<SignatureRequestDTO> create(SignatureRequestDTO signatureRequest) {
        // Ensure ID is null for create operation
        signatureRequest.setId(null);

        // Set default values if not provided
        if (signatureRequest.getRequestStatus() == null) {
            signatureRequest.setRequestStatus(SignatureStatus.PENDING);
        }
        if (signatureRequest.getNotificationSent() == null) {
            signatureRequest.setNotificationSent(false);
        }
        if (signatureRequest.getReminderSent() == null) {
            signatureRequest.setReminderSent(false);
        }

        SignatureRequest entity = mapper.toEntity(signatureRequest);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature request not found with ID: " + id)))
                .flatMap(entity -> repository.delete(entity));
    }

    @Override
    public Flux<SignatureRequestDTO> getByDocumentSignatureId(UUID documentSignatureId) {
        return repository.findByDocumentSignatureId(documentSignatureId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<SignatureRequestDTO> getByRequestReference(String requestReference) {
        return repository.findByRequestReference(requestReference)
                .map(mapper::toDTO);
    }

    @Override
    public Flux<SignatureRequestDTO> getByRequestStatus(SignatureStatus requestStatus) {
        return repository.findByRequestStatus(requestStatus)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<SignatureRequestDTO> sendNotification(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature request not found with ID: " + id)))
                .flatMap(entity -> {
                    // In a real implementation, this would send an actual notification
                    // For now, we'll just update the entity
                    entity.setNotificationSent(true);
                    entity.setNotificationSentAt(LocalDateTime.now());
                    return repository.save(entity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<SignatureRequestDTO> sendReminder(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature request not found with ID: " + id)))
                .flatMap(entity -> {
                    // In a real implementation, this would send an actual reminder
                    // For now, we'll just update the entity
                    entity.setReminderSent(true);
                    entity.setReminderSentAt(LocalDateTime.now());
                    return repository.save(entity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Flux<SignatureRequestDTO> processExpiredRequests() {
        LocalDateTime now = LocalDateTime.now();

        // Find all pending requests that have expired
        return repository.findByExpirationDateBeforeAndRequestStatus(now, SignatureStatus.PENDING)
                .flatMap(entity -> {
                    // Mark the request as expired
                    entity.setRequestStatus(SignatureStatus.EXPIRED);
                    return repository.save(entity);
                })
                .map(mapper::toDTO);
    }
}
