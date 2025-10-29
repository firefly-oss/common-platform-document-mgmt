/*
 * Copyright 2025 Firefly Software Solutions Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
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

import com.firefly.commons.ecm.core.mappers.DocumentPermissionMapper;
import com.firefly.commons.ecm.interfaces.dtos.DocumentPermissionDTO;
import com.firefly.commons.ecm.interfaces.enums.PermissionType;
import com.firefly.commons.ecm.models.entities.DocumentPermission;
import com.firefly.commons.ecm.models.repositories.DocumentPermissionRepository;
import com.firefly.core.ecm.domain.model.security.Permission;
import com.firefly.core.ecm.port.security.PermissionPort;
import com.firefly.core.ecm.service.EcmPortProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentPermissionServiceImplTest {

    @Mock
    private DocumentPermissionRepository repository;

    @Mock
    private DocumentPermissionMapper mapper;

    @Mock
    private EcmPortProvider ecmPortProvider;

    @Mock
    private PermissionPort permissionPort;

    @InjectMocks
    private DocumentPermissionServiceImpl service;

    @Test
    void create_WithEcmPermissionPort_GrantsAndSaves() {
        UUID docId = UUID.randomUUID();
        UUID partyId = UUID.randomUUID();
        DocumentPermissionDTO dto = DocumentPermissionDTO.builder()
                .documentId(docId)
                .partyId(partyId)
                .permissionType(PermissionType.READ)
                .isGranted(true)
                .build();
        DocumentPermission entity = DocumentPermission.builder().build();
        DocumentPermission saved = DocumentPermission.builder().id(UUID.randomUUID()).build();
        DocumentPermissionDTO savedDto = DocumentPermissionDTO.builder().id(saved.getId()).build();

        when(ecmPortProvider.getPermissionPort()).thenReturn(Optional.of(permissionPort));
        when(permissionPort.grantPermission(any(Permission.class))).thenReturn(Mono.just(mock(Permission.class)));
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(saved));
        when(mapper.toDTO(saved)).thenReturn(savedDto);

        StepVerifier.create(service.create(dto))
                .expectNext(savedDto)
                .verifyComplete();

        verify(permissionPort).grantPermission(any(Permission.class));
        verify(repository).save(entity);
    }
}