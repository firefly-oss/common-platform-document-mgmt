package com.firefly.commons.ecm.core.services.impl;

import com.firefly.commons.ecm.core.mappers.DocumentVersionMapper;
import com.firefly.commons.ecm.interfaces.dtos.DocumentVersionDTO;
import com.firefly.commons.ecm.interfaces.enums.StorageType;
import com.firefly.commons.ecm.models.entities.DocumentVersion;
import com.firefly.commons.ecm.models.repositories.DocumentVersionRepository;
import com.firefly.core.ecm.service.EcmPortProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.UUID;

/**
 * Unit tests for DocumentVersionServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class DocumentVersionServiceImplTest {

    @Mock
    private DocumentVersionRepository repository;

    @Mock
    private DocumentVersionMapper mapper;

    @Mock
    private EcmPortProvider ecmPortProvider;

    @Mock
    private FilePart filePart;

    @Mock
    private HttpHeaders httpHeaders;

    @InjectMocks
    private DocumentVersionServiceImpl documentVersionService;

    private DocumentVersion testDocumentVersion;
    private DocumentVersionDTO testDocumentVersionDTO;

    private static final UUID TEST_VERSION_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private static final UUID TEST_DOCUMENT_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440100");

    @BeforeEach
    void setUp() {
        testDocumentVersion = DocumentVersion.builder()
                .id(TEST_VERSION_ID)
                .documentId(TEST_DOCUMENT_ID)
                .versionNumber(1)
                .fileName("test-v1.pdf")
                .fileExtension("pdf")
                .mimeType("application/pdf")
                .fileSize(1024L)
                .storageType(StorageType.S3)
                .storagePath("/documents/100/versions/1")
                .isEncrypted(false)
                .changeSummary("Initial version")
                .isMajorVersion(true)
                .build();

        testDocumentVersionDTO = DocumentVersionDTO.builder()
                .id(TEST_VERSION_ID)
                .documentId(TEST_DOCUMENT_ID)
                .versionNumber(1)
                .fileName("test-v1.pdf")
                .fileExtension("pdf")
                .mimeType("application/pdf")
                .fileSize(1024L)
                .storageType(StorageType.S3)
                .storagePath("/documents/100/versions/1")
                .isEncrypted(false)
                .changeSummary("Initial version")
                .isMajorVersion(true)
                .build();
    }

    @Test
    void getById_ShouldReturnDocumentVersion_WhenVersionExists() {
        // Given
        when(repository.findById(TEST_VERSION_ID)).thenReturn(Mono.just(testDocumentVersion));
        when(mapper.toDTO(testDocumentVersion)).thenReturn(testDocumentVersionDTO);

        // When & Then
        StepVerifier.create(documentVersionService.getById(TEST_VERSION_ID))
                .expectNext(testDocumentVersionDTO)
                .verifyComplete();

        verify(repository).findById(TEST_VERSION_ID);
        verify(mapper).toDTO(testDocumentVersion);
    }

    @Test
    void getById_ShouldReturnEmpty_WhenVersionDoesNotExist() {
        // Given
        when(repository.findById(TEST_VERSION_ID)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(documentVersionService.getById(TEST_VERSION_ID))
                .verifyComplete();

        verify(repository).findById(TEST_VERSION_ID);
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void create_ShouldCreateDocumentVersion_WithNullId() {
        // Given
        UUID newVersionId = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
        DocumentVersionDTO inputDTO = DocumentVersionDTO.builder()
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440999")) // This should be set to null
                .documentId(TEST_DOCUMENT_ID)
                .versionNumber(2)
                .fileName("test-v2.pdf")
                .build();

        DocumentVersion inputEntity = DocumentVersion.builder()
                .documentId(TEST_DOCUMENT_ID)
                .versionNumber(2)
                .fileName("test-v2.pdf")
                .build();

        DocumentVersion savedEntity = DocumentVersion.builder()
                .id(newVersionId)
                .documentId(TEST_DOCUMENT_ID)
                .versionNumber(2)
                .fileName("test-v2.pdf")
                .build();

        DocumentVersionDTO savedDTO = DocumentVersionDTO.builder()
                .id(newVersionId)
                .documentId(TEST_DOCUMENT_ID)
                .versionNumber(2)
                .fileName("test-v2.pdf")
                .build();

        when(mapper.toEntity(any(DocumentVersionDTO.class))).thenReturn(inputEntity);
        when(repository.save(inputEntity)).thenReturn(Mono.just(savedEntity));
        when(mapper.toDTO(savedEntity)).thenReturn(savedDTO);

        // When & Then
        StepVerifier.create(documentVersionService.create(inputDTO))
                .expectNext(savedDTO)
                .verifyComplete();

        // Verify ID was set to null
        assert inputDTO.getId() == null;
        verify(repository).save(inputEntity);
    }

    @Test
    void update_ShouldUpdateDocumentVersion_WhenVersionExists() {
        // Given
        DocumentVersionDTO updateDTO = DocumentVersionDTO.builder()
                .id(TEST_VERSION_ID)
                .documentId(TEST_DOCUMENT_ID)
                .changeSummary("Updated version")
                .build();

        DocumentVersion existingEntity = DocumentVersion.builder()
                .id(TEST_VERSION_ID)
                .documentId(TEST_DOCUMENT_ID)
                .changeSummary("Original version")
                .createdAt(null)
                .createdBy("original-user")
                .build();

        DocumentVersion updateEntity = DocumentVersion.builder()
                .id(TEST_VERSION_ID)
                .documentId(TEST_DOCUMENT_ID)
                .changeSummary("Updated version")
                .build();

        DocumentVersion savedEntity = DocumentVersion.builder()
                .id(TEST_VERSION_ID)
                .documentId(TEST_DOCUMENT_ID)
                .changeSummary("Updated version")
                .createdBy("original-user")
                .build();

        when(repository.findById(TEST_VERSION_ID)).thenReturn(Mono.just(existingEntity));
        when(mapper.toEntity(updateDTO)).thenReturn(updateEntity);
        when(repository.save(any(DocumentVersion.class))).thenReturn(Mono.just(savedEntity));
        when(mapper.toDTO(savedEntity)).thenReturn(updateDTO);

        // When & Then
        StepVerifier.create(documentVersionService.update(updateDTO))
                .expectNext(updateDTO)
                .verifyComplete();

        verify(repository).findById(TEST_VERSION_ID);
        verify(repository).save(any(DocumentVersion.class));
    }

    @Test
    void update_ShouldThrowError_WhenIdIsNull() {
        // Given
        DocumentVersionDTO updateDTO = DocumentVersionDTO.builder()
                .documentId(TEST_DOCUMENT_ID)
                .changeSummary("Updated version")
                .build();

        // When & Then
        StepVerifier.create(documentVersionService.update(updateDTO))
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(repository, never()).findById(any(UUID.class));
    }

    @Test
    void delete_ShouldDeleteDocumentVersion_WhenVersionExists() {
        // Given
        when(repository.findById(TEST_VERSION_ID)).thenReturn(Mono.just(testDocumentVersion));
        when(repository.delete(testDocumentVersion)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(documentVersionService.delete(TEST_VERSION_ID))
                .verifyComplete();

        verify(repository).findById(TEST_VERSION_ID);
        verify(repository).delete(testDocumentVersion);
    }

    @Test
    void delete_ShouldThrowError_WhenVersionDoesNotExist() {
        // Given
        when(repository.findById(TEST_VERSION_ID)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(documentVersionService.delete(TEST_VERSION_ID))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findById(TEST_VERSION_ID);
        verify(repository, never()).delete(any());
    }

    // ECM Port Operations Tests

    @Test
    void uploadVersionContent_ShouldUpdateVersion_WithFileInfo() {
        // Given
        when(filePart.filename()).thenReturn("uploaded-version.pdf");
        when(filePart.headers()).thenReturn(httpHeaders);
        when(httpHeaders.getContentType()).thenReturn(MediaType.APPLICATION_PDF);
        when(repository.findById(TEST_VERSION_ID)).thenReturn(Mono.just(testDocumentVersion));
        when(repository.save(any(DocumentVersion.class))).thenReturn(Mono.just(testDocumentVersion));
        when(mapper.toDTO(testDocumentVersion)).thenReturn(testDocumentVersionDTO);

        // When & Then
        StepVerifier.create(documentVersionService.uploadVersionContent(TEST_VERSION_ID, filePart))
                .expectNext(testDocumentVersionDTO)
                .verifyComplete();

        verify(repository).findById(TEST_VERSION_ID);
        verify(repository).save(any(DocumentVersion.class));
        verify(filePart).filename();
        verify(filePart, atLeastOnce()).headers();
    }

    @Test
    void uploadVersionContent_ShouldThrowError_WhenVersionDoesNotExist() {
        // Given
        when(repository.findById(TEST_VERSION_ID)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(documentVersionService.uploadVersionContent(TEST_VERSION_ID, filePart))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findById(TEST_VERSION_ID);
        verify(repository, never()).save(any());
    }

    @Test
    void downloadVersionContent_ShouldThrowError_WhenEcmNotImplemented() {
        // Given
        when(repository.findById(TEST_VERSION_ID)).thenReturn(Mono.just(testDocumentVersion));

        // When & Then
        StepVerifier.create(documentVersionService.downloadVersionContent(TEST_VERSION_ID))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findById(TEST_VERSION_ID);
    }

    @Test
    void getVersionContentMetadata_ShouldReturnVersionInfo_WhenVersionExists() {
        // Given
        when(repository.findById(TEST_VERSION_ID)).thenReturn(Mono.just(testDocumentVersion));
        when(mapper.toDTO(testDocumentVersion)).thenReturn(testDocumentVersionDTO);

        // When & Then
        StepVerifier.create(documentVersionService.getVersionContentMetadata(TEST_VERSION_ID))
                .expectNext(testDocumentVersionDTO)
                .verifyComplete();

        verify(repository).findById(TEST_VERSION_ID);
        verify(mapper).toDTO(testDocumentVersion);
    }

    @Test
    void getVersionsByDocumentId_ShouldReturnVersions_WhenDocumentHasVersions() {
        // Given
        UUID version2Id = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
        DocumentVersion version2 = DocumentVersion.builder()
                .id(version2Id)
                .documentId(TEST_DOCUMENT_ID)
                .versionNumber(2)
                .fileName("test-v2.pdf")
                .build();

        DocumentVersionDTO version2DTO = DocumentVersionDTO.builder()
                .id(version2Id)
                .documentId(TEST_DOCUMENT_ID)
                .versionNumber(2)
                .fileName("test-v2.pdf")
                .build();

        when(repository.findByDocumentId(TEST_DOCUMENT_ID))
                .thenReturn(Flux.just(testDocumentVersion, version2));
        when(mapper.toDTO(testDocumentVersion)).thenReturn(testDocumentVersionDTO);
        when(mapper.toDTO(version2)).thenReturn(version2DTO);

        // When & Then
        StepVerifier.create(documentVersionService.getVersionsByDocumentId(TEST_DOCUMENT_ID))
                .expectNext(testDocumentVersionDTO)
                .expectNext(version2DTO)
                .verifyComplete();

        verify(repository).findByDocumentId(TEST_DOCUMENT_ID);
        verify(mapper).toDTO(testDocumentVersion);
        verify(mapper).toDTO(version2);
    }

    @Test
    void getVersionsByDocumentId_ShouldReturnEmpty_WhenDocumentHasNoVersions() {
        // Given
        when(repository.findByDocumentId(TEST_DOCUMENT_ID)).thenReturn(Flux.empty());

        // When & Then
        StepVerifier.create(documentVersionService.getVersionsByDocumentId(TEST_DOCUMENT_ID))
                .verifyComplete();

        verify(repository).findByDocumentId(TEST_DOCUMENT_ID);
        verify(mapper, never()).toDTO(any());
    }
}