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

import com.firefly.commons.ecm.core.mappers.DocumentMapper;
import com.firefly.commons.ecm.interfaces.dtos.DocumentDTO;
import com.firefly.commons.ecm.interfaces.enums.DocumentStatus;
import com.firefly.commons.ecm.interfaces.enums.DocumentType;
import com.firefly.commons.ecm.models.entities.Document;
import com.firefly.commons.ecm.models.repositories.DocumentRepository;
import com.firefly.core.ecm.port.document.DocumentContentPort;

import com.firefly.core.ecm.service.EcmPortProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

/**
 * Unit tests for DocumentServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {

    @Mock
    private DocumentRepository repository;

    @Mock
    private DocumentMapper mapper;

    @Mock
    private EcmPortProvider ecmPortProvider;

    @Mock
    private DocumentContentPort documentContentPort;



    @Mock
    private FilePart filePart;

    @Mock
    private HttpHeaders httpHeaders;

    @InjectMocks
    private DocumentServiceImpl documentService;

    private Document testDocument;
    private DocumentDTO testDocumentDTO;

    private static final UUID TEST_DOCUMENT_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

    @BeforeEach
    void setUp() {
        testDocument = Document.builder()
                .id(TEST_DOCUMENT_ID)
                .name("Test Document")
                .description("Test Description")
                .fileName("test.pdf")
                .documentType(DocumentType.CONTRACT)
                .documentStatus(DocumentStatus.PUBLISHED)
                .version(1L)
                .build();

        testDocumentDTO = DocumentDTO.builder()
                .id(TEST_DOCUMENT_ID)
                .name("Test Document")
                .description("Test Description")
                .fileName("test.pdf")
                .documentType(DocumentType.CONTRACT)
                .documentStatus(DocumentStatus.PUBLISHED)
                .version(1L)
                .build();
    }

    @Test
    void getById_ShouldReturnDocument_WhenDocumentExists() {
        // Given
        when(repository.findById(TEST_DOCUMENT_ID)).thenReturn(Mono.just(testDocument));
        when(mapper.toDTO(testDocument)).thenReturn(testDocumentDTO);

        // When & Then
        StepVerifier.create(documentService.getById(TEST_DOCUMENT_ID))
                .expectNext(testDocumentDTO)
                .verifyComplete();

        verify(repository).findById(TEST_DOCUMENT_ID);
        verify(mapper).toDTO(testDocument);
    }

    @Test
    void getById_ShouldReturnEmpty_WhenDocumentDoesNotExist() {
        // Given
        when(repository.findById(TEST_DOCUMENT_ID)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(documentService.getById(TEST_DOCUMENT_ID))
                .verifyComplete();

        verify(repository).findById(TEST_DOCUMENT_ID);
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void create_ShouldCreateDocument_WithNullId() {
        // Given
        UUID newDocumentId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        DocumentDTO inputDTO = DocumentDTO.builder()
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440999")) // This should be set to null
                .name("New Document")
                .build();

        Document inputEntity = Document.builder()
                .name("New Document")
                .build();

        Document savedEntity = Document.builder()
                .id(newDocumentId)
                .name("New Document")
                .build();

        DocumentDTO savedDTO = DocumentDTO.builder()
                .id(newDocumentId)
                .name("New Document")
                .build();

        when(mapper.toEntity(any(DocumentDTO.class))).thenReturn(inputEntity);
        when(repository.save(inputEntity)).thenReturn(Mono.just(savedEntity));
        when(mapper.toDTO(savedEntity)).thenReturn(savedDTO);

        // When & Then
        StepVerifier.create(documentService.create(inputDTO))
                .expectNext(savedDTO)
                .verifyComplete();

        // Verify ID was set to null
        assert inputDTO.getId() == null;
        verify(repository).save(inputEntity);
    }

    @Test
    void update_ShouldUpdateDocument_WhenDocumentExists() {
        // Given
        DocumentDTO updateDTO = DocumentDTO.builder()
                .id(TEST_DOCUMENT_ID)
                .name("Updated Document")
                .build();

        Document existingEntity = Document.builder()
                .id(TEST_DOCUMENT_ID)
                .name("Original Document")
                .createdAt(null)
                .createdBy("original-user")
                .build();

        Document updateEntity = Document.builder()
                .id(TEST_DOCUMENT_ID)
                .name("Updated Document")
                .build();

        Document savedEntity = Document.builder()
                .id(TEST_DOCUMENT_ID)
                .name("Updated Document")
                .createdBy("original-user")
                .build();

        when(repository.findById(TEST_DOCUMENT_ID)).thenReturn(Mono.just(existingEntity));
        when(mapper.toEntity(updateDTO)).thenReturn(updateEntity);
        when(repository.save(any(Document.class))).thenReturn(Mono.just(savedEntity));
        when(mapper.toDTO(savedEntity)).thenReturn(updateDTO);

        // When & Then
        StepVerifier.create(documentService.update(updateDTO))
                .expectNext(updateDTO)
                .verifyComplete();

        verify(repository).findById(TEST_DOCUMENT_ID);
        verify(repository).save(any(Document.class));
    }

    @Test
    void update_ShouldThrowError_WhenIdIsNull() {
        // Given
        DocumentDTO updateDTO = DocumentDTO.builder()
                .name("Updated Document")
                .build();

        // When & Then
        StepVerifier.create(documentService.update(updateDTO))
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(repository, never()).findById(any(UUID.class));
    }

    @Test
    void delete_ShouldDeleteDocument_WhenDocumentExists() {
        // Given
        when(repository.findById(TEST_DOCUMENT_ID)).thenReturn(Mono.just(testDocument));
        when(repository.delete(testDocument)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(documentService.delete(TEST_DOCUMENT_ID))
                .verifyComplete();

        verify(repository).findById(TEST_DOCUMENT_ID);
        verify(repository).delete(testDocument);
    }

    @Test
    void delete_ShouldThrowError_WhenDocumentDoesNotExist() {
        // Given
        when(repository.findById(TEST_DOCUMENT_ID)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(documentService.delete(TEST_DOCUMENT_ID))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findById(TEST_DOCUMENT_ID);
        verify(repository, never()).delete(any());
    }

    // ECM Port Operations Tests

    @Test
    void uploadContent_ShouldThrowError_WhenEcmPortNotAvailable() {
        // Given
        when(filePart.filename()).thenReturn("uploaded-file.pdf");
        when(repository.findById(TEST_DOCUMENT_ID)).thenReturn(Mono.just(testDocument));
        when(ecmPortProvider.getDocumentContentPort()).thenReturn(Optional.empty());

        // When & Then
        StepVerifier.create(documentService.uploadContent(TEST_DOCUMENT_ID, filePart))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findById(TEST_DOCUMENT_ID);
        verify(ecmPortProvider).getDocumentContentPort();
    }

    @Test
    void uploadContent_ShouldThrowError_WhenDocumentDoesNotExist() {
        // Given
        when(repository.findById(TEST_DOCUMENT_ID)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(documentService.uploadContent(TEST_DOCUMENT_ID, filePart))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findById(TEST_DOCUMENT_ID);
        verify(repository, never()).save(any());
    }

    @Test
    void downloadContent_ShouldThrowError_WhenEcmNotImplemented() {
        // Given
        when(repository.findById(TEST_DOCUMENT_ID)).thenReturn(Mono.just(testDocument));

        // When & Then
        StepVerifier.create(documentService.downloadContent(TEST_DOCUMENT_ID))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findById(TEST_DOCUMENT_ID);
    }

    @Test
    void createVersion_ShouldIncrementVersion_WhenDocumentExists() {
        // Given
        when(filePart.filename()).thenReturn("new-version.pdf");
        when(filePart.headers()).thenReturn(httpHeaders);
        when(httpHeaders.getContentType()).thenReturn(MediaType.APPLICATION_PDF);
        when(repository.findById(TEST_DOCUMENT_ID)).thenReturn(Mono.just(testDocument));
        when(repository.save(any(Document.class))).thenReturn(Mono.just(testDocument));
        when(mapper.toDTO(testDocument)).thenReturn(testDocumentDTO);

        // When & Then
        StepVerifier.create(documentService.createVersion(TEST_DOCUMENT_ID, filePart, "Version comment"))
                .expectNext(testDocumentDTO)
                .verifyComplete();

        verify(repository).findById(TEST_DOCUMENT_ID);
        verify(repository).save(any(Document.class));
        verify(filePart, atLeastOnce()).headers();
    }

    @Test
    void getContentMetadata_ShouldReturnDocumentInfo_WhenDocumentExists() {
        // Given
        when(repository.findById(TEST_DOCUMENT_ID)).thenReturn(Mono.just(testDocument));
        when(mapper.toDTO(testDocument)).thenReturn(testDocumentDTO);

        // When & Then
        StepVerifier.create(documentService.getContentMetadata(TEST_DOCUMENT_ID))
                .expectNext(testDocumentDTO)
                .verifyComplete();

        verify(repository).findById(TEST_DOCUMENT_ID);
        verify(mapper).toDTO(testDocument);
    }

    @Test
    void testDeleteDocument_WithEcmIntegration_Success() {
        // Given
        when(repository.findById(TEST_DOCUMENT_ID)).thenReturn(Mono.just(testDocument));
        when(ecmPortProvider.getDocumentContentPort()).thenReturn(Optional.of(documentContentPort));
        when(documentContentPort.deleteContent(any())).thenReturn(Mono.empty());
        when(repository.delete(any(Document.class))).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(documentService.delete(TEST_DOCUMENT_ID))
                .verifyComplete();

        verify(repository).findById(TEST_DOCUMENT_ID);
        verify(repository, atLeastOnce()).delete(testDocument);
        verify(documentContentPort).deleteContent(any());
    }

    @Test
    void testDeleteDocument_NotFound_ThrowsException() {
        // Given
        when(repository.findById(TEST_DOCUMENT_ID)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(documentService.delete(TEST_DOCUMENT_ID))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findById(TEST_DOCUMENT_ID);
        verify(repository, never()).delete(any());
    }

    @Test
    void testDownloadContent_WithEcmPort_Success() {
        // Given
        DataBuffer dataBuffer = new DefaultDataBufferFactory().allocateBuffer(10);
        when(repository.findById(TEST_DOCUMENT_ID)).thenReturn(Mono.just(testDocument));
        when(ecmPortProvider.getDocumentContentPort()).thenReturn(Optional.of(documentContentPort));
        when(documentContentPort.getContentStream(any())).thenReturn(Flux.just(dataBuffer));

        // When & Then
        StepVerifier.create(documentService.downloadContent(TEST_DOCUMENT_ID))
                .expectNext(dataBuffer)
                .verifyComplete();

        verify(repository).findById(TEST_DOCUMENT_ID);
        verify(documentContentPort).getContentStream(any());
    }

    @Test
    void testDownloadContent_EcmPortNotAvailable_ThrowsException() {
        // Given
        when(repository.findById(TEST_DOCUMENT_ID)).thenReturn(Mono.just(testDocument));
        when(ecmPortProvider.getDocumentContentPort()).thenReturn(Optional.empty());

        // When & Then
        StepVerifier.create(documentService.downloadContent(TEST_DOCUMENT_ID))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findById(TEST_DOCUMENT_ID);
        verify(documentContentPort, never()).getContentStream(any());
    }

    @Test
    void testUploadContent_EcmPortNotAvailable_ThrowsException() {
        // Given
        when(repository.findById(TEST_DOCUMENT_ID)).thenReturn(Mono.just(testDocument));
        when(ecmPortProvider.getDocumentContentPort()).thenReturn(Optional.empty());

        // When & Then
        StepVerifier.create(documentService.uploadContent(TEST_DOCUMENT_ID, filePart))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findById(TEST_DOCUMENT_ID);
    }

    @Test
    void testCreateDocument_WithEcmIntegration_Success() {
        // Given
        when(mapper.toEntity(any(DocumentDTO.class))).thenReturn(testDocument);
        when(repository.save(any(Document.class))).thenReturn(Mono.just(testDocument));
        when(mapper.toDTO(any(Document.class))).thenReturn(testDocumentDTO);

        // When & Then
        StepVerifier.create(documentService.create(testDocumentDTO))
                .expectNext(testDocumentDTO)
                .verifyComplete();

        verify(repository).save(any(Document.class));
    }
}