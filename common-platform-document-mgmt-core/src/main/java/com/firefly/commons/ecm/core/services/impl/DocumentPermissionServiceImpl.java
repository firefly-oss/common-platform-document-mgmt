/*
 * Copyright 2025 Firefly Software Solutions Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.firefly.commons.ecm.core.services.impl;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.filters.FilterUtils;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.core.mappers.DocumentPermissionMapper;
import com.firefly.commons.ecm.core.services.DocumentPermissionService;
import com.firefly.commons.ecm.interfaces.dtos.DocumentPermissionDTO;
import com.firefly.commons.ecm.interfaces.enums.PermissionType;
import com.firefly.commons.ecm.models.entities.DocumentPermission;
import com.firefly.commons.ecm.models.repositories.DocumentPermissionRepository;
import com.firefly.core.ecm.service.EcmPortProvider;
import com.firefly.core.ecm.port.security.PermissionPort;
import com.firefly.core.ecm.domain.enums.security.ResourceType;
import com.firefly.core.ecm.domain.enums.security.PrincipalType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import java.time.ZoneOffset;
import java.util.UUID;
/**
 * Implementation of the DocumentPermissionService interface.
 */
@Service
@Transactional
public class DocumentPermissionServiceImpl implements DocumentPermissionService {

    @Autowired
    private DocumentPermissionRepository repository;

    @Autowired
    private DocumentPermissionMapper mapper;

    @Autowired
    private EcmPortProvider ecmPortProvider;

    @Override
    public Mono<DocumentPermissionDTO> getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<DocumentPermissionDTO>> filter(FilterRequest<DocumentPermissionDTO> filterRequest) {
        return FilterUtils.createFilter(
                DocumentPermission.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<DocumentPermissionDTO> update(DocumentPermissionDTO documentPermission) {
        if (documentPermission.getId() == null) {
            return Mono.error(new IllegalArgumentException("ID cannot be null for update operation"));
        }

        return repository.findById(documentPermission.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Document permission not found with ID: " + documentPermission.getId())))
                .flatMap(existingEntity -> {
                    // Update via ECM port if available
                    Mono<Void> portUpdate = ecmPortProvider.getPermissionPort()
                            .map(port -> {
                                com.firefly.core.ecm.domain.model.security.Permission permission =
                                        com.firefly.core.ecm.domain.model.security.Permission.builder()
                                                .id(documentPermission.getId())
                                                .resourceId(documentPermission.getDocumentId())
                                                .resourceType(ResourceType.DOCUMENT)
                                                .principalId(documentPermission.getPartyId())
                                                .principalType(PrincipalType.USER)
                                                .permissionType(com.firefly.core.ecm.domain.enums.security.PermissionType.valueOf(documentPermission.getPermissionType().name()))
                                                .granted(Boolean.TRUE.equals(documentPermission.getIsGranted()))
                                                .expiresAt(documentPermission.getExpirationDate() != null ? documentPermission.getExpirationDate().toInstant(ZoneOffset.UTC) : null)
                                                .build();
                                return port.updatePermission(permission).then();
                            })
                            .orElse(Mono.empty());

                    DocumentPermission entityToUpdate = mapper.toEntity(documentPermission);
                    // Preserve created info
                    entityToUpdate.setCreatedAt(existingEntity.getCreatedAt());
                    entityToUpdate.setCreatedBy(existingEntity.getCreatedBy());

                    return portUpdate.onErrorResume(err -> Mono.empty())
                            .then(repository.save(entityToUpdate));
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<DocumentPermissionDTO> create(DocumentPermissionDTO documentPermission) {
        // Ensure ID is null for create operation
        documentPermission.setId(null);

        // Grant permission via ECM port if available
        return ecmPortProvider.getPermissionPort()
                .map(port -> grantViaPort(port, documentPermission)
                        .onErrorResume(err -> Mono.empty()) // continue even if ECM fails
                        .then(saveLocal(documentPermission)))
                .orElseGet(() -> saveLocal(documentPermission));
    }

    private Mono<DocumentPermissionDTO> saveLocal(DocumentPermissionDTO dto) {
        DocumentPermission entity = mapper.toEntity(dto);
        return repository.save(entity).map(mapper::toDTO);
    }

    private Mono<Void> grantViaPort(PermissionPort port, DocumentPermissionDTO dto) {
        com.firefly.core.ecm.domain.model.security.Permission permission =
                com.firefly.core.ecm.domain.model.security.Permission.builder()
                        .id(UUID.randomUUID())
                        .resourceId(dto.getDocumentId())
                        .resourceType(ResourceType.DOCUMENT)
                        .principalId(dto.getPartyId())
                        .principalType(PrincipalType.USER)
                        .permissionType(com.firefly.core.ecm.domain.enums.security.PermissionType.valueOf(dto.getPermissionType().name()))
                        .granted(Boolean.TRUE.equals(dto.getIsGranted()))
                        .grantedAt(java.time.Instant.now())
                        .expiresAt(dto.getExpirationDate() != null ? dto.getExpirationDate().toInstant(ZoneOffset.UTC) : null)
                        .inherited(false)
                        .build();
        return port.grantPermission(permission).then();
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Document permission not found with ID: " + id)))
                .flatMap(entity -> {
                    Mono<Void> portDelete = ecmPortProvider.getPermissionPort()
                            .map(port -> port.revokePermission(id)
                                    .onErrorResume(err -> Mono.empty()))
                            .orElse(Mono.empty());
                    return portDelete.then(repository.delete(entity));
                });
    }

    @Override
    public Mono<Boolean> hasPermission(UUID documentId, UUID principalId, com.firefly.commons.ecm.interfaces.enums.PermissionType permissionType) {
        return ecmPortProvider.getPermissionPort()
                .map(port -> port.hasPermission(
                        documentId,
                        ResourceType.DOCUMENT,
                        principalId,
                        PrincipalType.USER,
                        com.firefly.core.ecm.domain.enums.security.PermissionType.valueOf(permissionType.name())
                ))
                .orElse(Mono.error(new RuntimeException("ECM PermissionPort is not configured")));
    }
}
