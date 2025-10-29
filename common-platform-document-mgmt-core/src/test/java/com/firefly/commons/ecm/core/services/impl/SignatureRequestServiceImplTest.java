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

import com.firefly.commons.ecm.core.mappers.SignatureRequestMapper;
import com.firefly.commons.ecm.interfaces.dtos.SignatureRequestDTO;
import com.firefly.commons.ecm.interfaces.enums.SignatureStatus;
import com.firefly.commons.ecm.models.entities.SignatureRequest;
import com.firefly.commons.ecm.models.repositories.SignatureRequestRepository;
import com.firefly.core.ecm.port.esignature.SignatureRequestPort;
import com.firefly.core.ecm.service.EcmPortProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignatureRequestServiceImplTest {

    @Mock
    private SignatureRequestRepository repository;

    @Mock
    private SignatureRequestMapper mapper;

    @Mock
    private EcmPortProvider ecmPortProvider;

    @Mock
    private SignatureRequestPort signatureRequestPort;

    @InjectMocks
    private SignatureRequestServiceImpl service;

    @Test
    void sendNotification_WithEcmPort_UpdatesLocally() {
        UUID id = UUID.randomUUID();
        SignatureRequest entity = SignatureRequest.builder()
                .id(id)
                .requestStatus(SignatureStatus.PENDING)
                .notificationSent(false)
                .build();
        SignatureRequest saved = SignatureRequest.builder()
                .id(id)
                .requestStatus(SignatureStatus.PENDING)
                .notificationSent(true)
                .notificationSentAt(LocalDateTime.now())
                .build();
        SignatureRequestDTO dto = SignatureRequestDTO.builder().id(id).build();

        when(repository.findById(id)).thenReturn(Mono.just(entity));
        when(ecmPortProvider.getSignatureRequestPort()).thenReturn(Optional.of(signatureRequestPort));
        when(signatureRequestPort.resendNotification(id)).thenReturn(Mono.empty());
        when(repository.save(any(SignatureRequest.class))).thenReturn(Mono.just(saved));
        when(mapper.toDTO(saved)).thenReturn(dto);

        StepVerifier.create(service.sendNotification(id))
                .expectNext(dto)
                .verifyComplete();

        verify(signatureRequestPort).resendNotification(id);
    }
}