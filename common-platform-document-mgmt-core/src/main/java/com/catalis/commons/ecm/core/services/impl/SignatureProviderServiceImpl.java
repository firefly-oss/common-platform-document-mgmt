package com.catalis.commons.ecm.core.services.impl;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.SignatureProviderMapper;
import com.catalis.commons.ecm.core.services.SignatureProviderService;
import com.catalis.commons.ecm.interfaces.dtos.SignatureProviderDTO;
import com.catalis.commons.ecm.models.entities.SignatureProvider;
import com.catalis.commons.ecm.models.repositories.SignatureProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the SignatureProviderService interface.
 */
@Service
@Transactional
public class SignatureProviderServiceImpl implements SignatureProviderService {

    @Autowired
    private SignatureProviderRepository repository;

    @Autowired
    private SignatureProviderMapper mapper;

    @Override
    public Mono<SignatureProviderDTO> getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<SignatureProviderDTO>> filter(FilterRequest<SignatureProviderDTO> filterRequest) {
        return FilterUtils.createFilter(
                SignatureProvider.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<SignatureProviderDTO> update(SignatureProviderDTO signatureProvider) {
        if (signatureProvider.getId() == null) {
            return Mono.error(new IllegalArgumentException("ID cannot be null for update operation"));
        }

        return repository.findById(signatureProvider.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Signature provider not found with ID: " + signatureProvider.getId())))
                .flatMap(existingEntity -> {
                    SignatureProvider entityToUpdate = mapper.toEntity(signatureProvider);
                    // Preserve created info
                    entityToUpdate.setCreatedAt(existingEntity.getCreatedAt());
                    entityToUpdate.setCreatedBy(existingEntity.getCreatedBy());
                    return repository.save(entityToUpdate);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<SignatureProviderDTO> create(SignatureProviderDTO signatureProvider) {
        // Ensure ID is null for create operation
        signatureProvider.setId(null);

        SignatureProvider entity = mapper.toEntity(signatureProvider);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature provider not found with ID: " + id)))
                .flatMap(entity -> repository.delete(entity));
    }

    @Override
    public Mono<SignatureProviderDTO> getDefaultProvider(String tenantId) {
        return repository.findByIsDefaultTrueAndTenantId(tenantId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<SignatureProviderDTO> setAsDefault(Long id, String tenantId) {
        // First, find the provider to set as default
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Signature provider not found with ID: " + id)))
                .flatMap(providerToSetAsDefault -> {
                    // Find the current default provider (if any)
                    return repository.findByIsDefaultTrueAndTenantId(tenantId)
                            .flatMap(currentDefault -> {
                                // If there is a current default and it's not the same as the one we're setting
                                if (!currentDefault.getId().equals(id)) {
                                    // Unset the current default
                                    currentDefault.setIsDefault(false);
                                    return repository.save(currentDefault);
                                }
                                return Mono.just(currentDefault);
                            })
                            .then(Mono.defer(() -> {
                                // Set the new provider as default
                                providerToSetAsDefault.setIsDefault(true);
                                return repository.save(providerToSetAsDefault);
                            }))
                            .switchIfEmpty(Mono.defer(() -> {
                                // No current default, just set the new one
                                providerToSetAsDefault.setIsDefault(true);
                                return repository.save(providerToSetAsDefault);
                            }))
                            .map(mapper::toDTO);
                });
    }
}
