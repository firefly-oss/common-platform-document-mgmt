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

import com.firefly.commons.ecm.core.mappers.DocumentMapper;
import com.firefly.commons.ecm.core.mappers.EcmDomainMapper;
import com.firefly.commons.ecm.interfaces.dtos.DocumentDTO;
import com.firefly.commons.ecm.models.entities.Document;
import com.firefly.commons.ecm.models.repositories.DocumentRepository;
import com.firefly.core.ecm.port.document.DocumentSearchPort;
import com.firefly.core.ecm.service.EcmPortProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {

    @Mock
    private DocumentRepository repository;

    @Mock
    private DocumentMapper mapper;

    @Mock
    private EcmPortProvider ecmPortProvider;

    @Mock
    private EcmDomainMapper ecmDomainMapper;

    @Mock
    private FilePart filePart;

    @Mock
    private DocumentSearchPort searchPort;

    @InjectMocks
    private DocumentServiceImpl service;

    private UUID docId;
    private Document entity;

    @BeforeEach
    void setup() {
        docId = UUID.fromString("11111111-2222-3333-4444-555555555555");
        entity = Document.builder()
                .id(docId)
                .name("contract.pdf")
                .fileName("contract.pdf")
                .mimeType("application/pdf")
                .version(0L)
                .build();
    }

    @Test
    void createVersion_FallbackWithoutEcm_IncrementsVersionAndIndexes() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        when(filePart.filename()).thenReturn("contract-v2.pdf");
        when(filePart.headers()).thenReturn(headers);

        when(repository.findById(docId)).thenReturn(Mono.just(entity));
        when(repository.save(any(Document.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
        when(ecmPortProvider.getDocumentVersionPort()).thenReturn(Optional.empty());
        when(ecmPortProvider.getDocumentSearchPort()).thenReturn(Optional.of(searchPort));
        when(searchPort.indexDocument(any())).thenReturn(Mono.empty());
        when(mapper.toDTO(any(Document.class))).thenAnswer(inv -> {
            Document d = inv.getArgument(0);
            return DocumentDTO.builder()
                    .id(d.getId())
                    .fileName(d.getFileName())
                    .mimeType(d.getMimeType())
                    .version(d.getVersion())
                    .build();
        });

        // When
        Mono<DocumentDTO> result = service.createVersion(docId, filePart, "v2");

        // Then
        StepVerifier.create(result)
                .assertNext(dto -> {
                    assert dto.getVersion() == 1L;
                    assert "contract-v2.pdf".equals(dto.getFileName());
                    assert "application/pdf".equals(dto.getMimeType());
                })
                .verifyComplete();

        verify(searchPort).indexDocument(any());
    }
}
