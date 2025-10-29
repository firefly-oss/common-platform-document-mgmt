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

import com.firefly.commons.ecm.core.mappers.EcmDomainMapper;
import com.firefly.commons.ecm.core.services.impl.DocumentSearchServiceImpl;
import com.firefly.commons.ecm.interfaces.dtos.DocumentDTO;
import com.firefly.core.ecm.domain.model.document.Document;
import com.firefly.core.ecm.port.document.DocumentSearchPort;
import com.firefly.core.ecm.service.EcmPortProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentSearchServiceImplTest {

    @Mock
    private EcmPortProvider ecmPortProvider;

    @Mock
    private EcmDomainMapper ecmDomainMapper;

    @Mock
    private DocumentSearchPort documentSearchPort;

    @InjectMocks
    private DocumentSearchServiceImpl service;

    @Test
    void fullTextSearch_ReturnsMappedDto() {
        Document ecmDoc = Document.builder()
                .id(UUID.randomUUID())
                .name("contract.pdf")
                .build();
        DocumentDTO dto = DocumentDTO.builder().id(ecmDoc.getId()).name(ecmDoc.getName()).build();

        when(ecmPortProvider.getDocumentSearchPort()).thenReturn(Optional.of(documentSearchPort));
        when(documentSearchPort.fullTextSearch("contract", 10)).thenReturn(Flux.just(ecmDoc));
        when(ecmDomainMapper.fromEcmDocument(ecmDoc)).thenReturn(dto);

        StepVerifier.create(service.fullTextSearch("contract", 10))
                .expectNext(dto)
                .verifyComplete();

        verify(documentSearchPort).fullTextSearch("contract", 10);
    }
}