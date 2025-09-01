package com.firefly.commons.ecm.core.services.impl;

import com.firefly.commons.ecm.core.mappers.DocumentSignatureMapper;
import com.firefly.commons.ecm.core.mappers.EcmDomainMapper;
import com.firefly.commons.ecm.core.validation.EcmParameterValidator;
import com.firefly.commons.ecm.core.services.SignatureRequestService;
import com.firefly.commons.ecm.interfaces.dtos.DocumentSignatureDTO;
import com.firefly.commons.ecm.interfaces.enums.SignatureStatus;
import com.firefly.commons.ecm.interfaces.enums.SignatureType;
import com.firefly.commons.ecm.interfaces.enums.SignatureFormat;
import com.firefly.commons.ecm.models.entities.DocumentSignature;
import com.firefly.commons.ecm.models.repositories.DocumentSignatureRepository;
import com.firefly.core.ecm.service.EcmPortProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.UUID;

/**
 * Unit tests for DocumentSignatureServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class DocumentSignatureServiceImplTest {

    @Mock
    private DocumentSignatureRepository repository;

    @Mock
    private DocumentSignatureMapper mapper;

    @Mock
    private EcmPortProvider ecmPortProvider;

    @Mock
    private SignatureRequestService signatureRequestService;

    @Mock
    private EcmDomainMapper ecmDomainMapper;

    @Mock
    private EcmParameterValidator ecmParameterValidator;

    @InjectMocks
    private DocumentSignatureServiceImpl documentSignatureService;

    private DocumentSignature testDocumentSignature;
    private DocumentSignatureDTO testDocumentSignatureDTO;

    private static final UUID TEST_SIGNATURE_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private static final UUID TEST_DOCUMENT_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440100");
    private static final UUID TEST_VERSION_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
    private static final UUID TEST_PROVIDER_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
    private static final UUID TEST_SIGNER_PARTY_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440200");

    @BeforeEach
    void setUp() {
        testDocumentSignature = DocumentSignature.builder()
                .id(TEST_SIGNATURE_ID)
                .documentId(TEST_DOCUMENT_ID)
                .documentVersionId(TEST_VERSION_ID)
                .signatureProviderId(TEST_PROVIDER_ID)
                .signerPartyId(TEST_SIGNER_PARTY_ID)
                .signerName("John Doe")
                .signerEmail("john.doe@example.com")
                .signatureType(SignatureType.DIGITAL)
                .signatureFormat(SignatureFormat.PADES)
                .signatureStatus(SignatureStatus.PENDING)
                .signatureReason("Contract approval")
                .signatureLocation("New York, NY")
                .signatureContactInfo("john.doe@example.com")
                .build();

        testDocumentSignatureDTO = DocumentSignatureDTO.builder()
                .id(TEST_SIGNATURE_ID)
                .documentId(TEST_DOCUMENT_ID)
                .documentVersionId(TEST_VERSION_ID)
                .signatureProviderId(TEST_PROVIDER_ID)
                .signerPartyId(TEST_SIGNER_PARTY_ID)
                .signerName("John Doe")
                .signerEmail("john.doe@example.com")
                .signatureType(SignatureType.DIGITAL)
                .signatureFormat(SignatureFormat.PADES)
                .signatureStatus(SignatureStatus.PENDING)
                .signatureReason("Contract approval")
                .signatureLocation("New York, NY")
                .signatureContactInfo("john.doe@example.com")
                .build();
    }

    @Test
    void getById_ShouldReturnDocumentSignature_WhenSignatureExists() {
        // Given
        when(repository.findById(TEST_SIGNATURE_ID)).thenReturn(Mono.just(testDocumentSignature));
        when(mapper.toDTO(testDocumentSignature)).thenReturn(testDocumentSignatureDTO);

        // When & Then
        StepVerifier.create(documentSignatureService.getById(TEST_SIGNATURE_ID))
                .expectNext(testDocumentSignatureDTO)
                .verifyComplete();

        verify(repository).findById(TEST_SIGNATURE_ID);
        verify(mapper).toDTO(testDocumentSignature);
    }

    @Test
    void getById_ShouldReturnEmpty_WhenSignatureDoesNotExist() {
        // Given
        when(repository.findById(TEST_SIGNATURE_ID)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(documentSignatureService.getById(TEST_SIGNATURE_ID))
                .verifyComplete();

        verify(repository).findById(TEST_SIGNATURE_ID);
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void create_ShouldCreateDocumentSignature_WithNullId() {
        // Given
        UUID newSignatureId = UUID.fromString("550e8400-e29b-41d4-a716-446655440999");
        DocumentSignatureDTO inputDTO = DocumentSignatureDTO.builder()
                .id(newSignatureId) // This should be set to null
                .documentId(TEST_DOCUMENT_ID)
                .signerName("Jane Smith")
                .signerEmail("jane.smith@example.com")
                .build();

        DocumentSignature inputEntity = DocumentSignature.builder()
                .documentId(TEST_DOCUMENT_ID)
                .signerName("Jane Smith")
                .signerEmail("jane.smith@example.com")
                .build();

        DocumentSignature savedEntity = DocumentSignature.builder()
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440003"))
                .documentId(TEST_DOCUMENT_ID)
                .signerName("Jane Smith")
                .signerEmail("jane.smith@example.com")
                .build();

        DocumentSignatureDTO savedDTO = DocumentSignatureDTO.builder()
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440003"))
                .documentId(TEST_DOCUMENT_ID)
                .signerName("Jane Smith")
                .signerEmail("jane.smith@example.com")
                .build();

        when(mapper.toEntity(any(DocumentSignatureDTO.class))).thenReturn(inputEntity);
        when(repository.save(inputEntity)).thenReturn(Mono.just(savedEntity));
        when(mapper.toDTO(savedEntity)).thenReturn(savedDTO);

        // When & Then
        StepVerifier.create(documentSignatureService.create(inputDTO))
                .expectNext(savedDTO)
                .verifyComplete();

        // Verify ID was set to null
        assert inputDTO.getId() == null;
        verify(repository).save(inputEntity);
    }

    @Test
    void update_ShouldUpdateDocumentSignature_WhenSignatureExists() {
        // Given
        DocumentSignatureDTO updateDTO = DocumentSignatureDTO.builder()
                .id(TEST_SIGNATURE_ID)
                .documentId(TEST_DOCUMENT_ID)
                .signatureStatus(SignatureStatus.SIGNED)
                .build();

        DocumentSignature existingEntity = DocumentSignature.builder()
                .id(TEST_SIGNATURE_ID)
                .documentId(TEST_DOCUMENT_ID)
                .signatureStatus(SignatureStatus.PENDING)
                .createdAt(null)
                .createdBy("original-user")
                .build();

        DocumentSignature updateEntity = DocumentSignature.builder()
                .id(TEST_SIGNATURE_ID)
                .documentId(TEST_DOCUMENT_ID)
                .signatureStatus(SignatureStatus.SIGNED)
                .build();

        DocumentSignature savedEntity = DocumentSignature.builder()
                .id(TEST_SIGNATURE_ID)
                .documentId(TEST_DOCUMENT_ID)
                .signatureStatus(SignatureStatus.SIGNED)
                .createdBy("original-user")
                .build();

        when(repository.findById(TEST_SIGNATURE_ID)).thenReturn(Mono.just(existingEntity));
        when(mapper.toEntity(updateDTO)).thenReturn(updateEntity);
        when(repository.save(any(DocumentSignature.class))).thenReturn(Mono.just(savedEntity));
        when(mapper.toDTO(savedEntity)).thenReturn(updateDTO);

        // When & Then
        StepVerifier.create(documentSignatureService.update(updateDTO))
                .expectNext(updateDTO)
                .verifyComplete();

        verify(repository).findById(TEST_SIGNATURE_ID);
        verify(repository).save(any(DocumentSignature.class));
    }

    @Test
    void update_ShouldThrowError_WhenIdIsNull() {
        // Given
        DocumentSignatureDTO updateDTO = DocumentSignatureDTO.builder()
                .documentId(TEST_DOCUMENT_ID)
                .signatureStatus(SignatureStatus.SIGNED)
                .build();

        // When & Then
        StepVerifier.create(documentSignatureService.update(updateDTO))
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(repository, never()).findById(any(UUID.class));
    }

    @Test
    void delete_ShouldDeleteDocumentSignature_WhenSignatureExists() {
        // Given
        when(repository.findById(TEST_SIGNATURE_ID)).thenReturn(Mono.just(testDocumentSignature));
        when(repository.delete(testDocumentSignature)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(documentSignatureService.delete(TEST_SIGNATURE_ID))
                .verifyComplete();

        verify(repository).findById(TEST_SIGNATURE_ID);
        verify(repository).delete(testDocumentSignature);
    }

    @Test
    void delete_ShouldThrowError_WhenSignatureDoesNotExist() {
        // Given
        when(repository.findById(TEST_SIGNATURE_ID)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(documentSignatureService.delete(TEST_SIGNATURE_ID))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findById(TEST_SIGNATURE_ID);
        verify(repository, never()).delete(any());
    }

    // Business Logic Tests

    @Test
    void getByDocumentId_ShouldReturnSignatures_WhenDocumentHasSignatures() {
        // Given
        DocumentSignature signature2 = DocumentSignature.builder()
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440003"))
                .documentId(TEST_DOCUMENT_ID)
                .signerName("Jane Smith")
                .signatureStatus(SignatureStatus.SIGNED)
                .build();

        DocumentSignatureDTO signature2DTO = DocumentSignatureDTO.builder()
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440003"))
                .documentId(TEST_DOCUMENT_ID)
                .signerName("Jane Smith")
                .signatureStatus(SignatureStatus.SIGNED)
                .build();

        when(repository.findByDocumentId(TEST_DOCUMENT_ID))
                .thenReturn(Flux.just(testDocumentSignature, signature2));
        when(mapper.toDTO(testDocumentSignature)).thenReturn(testDocumentSignatureDTO);
        when(mapper.toDTO(signature2)).thenReturn(signature2DTO);

        // When & Then
        StepVerifier.create(documentSignatureService.getByDocumentId(TEST_DOCUMENT_ID))
                .expectNext(testDocumentSignatureDTO)
                .expectNext(signature2DTO)
                .verifyComplete();

        verify(repository).findByDocumentId(TEST_DOCUMENT_ID);
        verify(mapper).toDTO(testDocumentSignature);
        verify(mapper).toDTO(signature2);
    }

    @Test
    void getBySignatureStatus_ShouldReturnSignatures_WhenSignaturesExistWithStatus() {
        // Given
        when(repository.findBySignatureStatus(SignatureStatus.PENDING))
                .thenReturn(Flux.just(testDocumentSignature));
        when(mapper.toDTO(testDocumentSignature)).thenReturn(testDocumentSignatureDTO);

        // When & Then
        StepVerifier.create(documentSignatureService.getBySignatureStatus(SignatureStatus.PENDING))
                .expectNext(testDocumentSignatureDTO)
                .verifyComplete();

        verify(repository).findBySignatureStatus(SignatureStatus.PENDING);
        verify(mapper).toDTO(testDocumentSignature);
    }

    @Test
    void initiateSigningProcess_ShouldSetPendingStatus_AndCreateSignature() {
        // Given
        DocumentSignatureDTO inputDTO = DocumentSignatureDTO.builder()
                .documentId(TEST_DOCUMENT_ID)
                .signerName("Bob Wilson")
                .signerEmail("bob.wilson@example.com")
                .build();

        UUID newSignatureId = UUID.fromString("550e8400-e29b-41d4-a716-446655440003");
        DocumentSignature savedEntity = DocumentSignature.builder()
                .id(newSignatureId)
                .documentId(TEST_DOCUMENT_ID)
                .signerName("Bob Wilson")
                .signerEmail("bob.wilson@example.com")
                .signatureStatus(SignatureStatus.PENDING)
                .build();

        DocumentSignatureDTO savedDTO = DocumentSignatureDTO.builder()
                .id(newSignatureId)
                .documentId(TEST_DOCUMENT_ID)
                .signerName("Bob Wilson")
                .signerEmail("bob.wilson@example.com")
                .signatureStatus(SignatureStatus.PENDING)
                .build();

        when(mapper.toEntity(any(DocumentSignatureDTO.class))).thenReturn(savedEntity);
        when(repository.save(any(DocumentSignature.class))).thenReturn(Mono.just(savedEntity));
        when(mapper.toDTO(savedEntity)).thenReturn(savedDTO);

        // When & Then
        StepVerifier.create(documentSignatureService.initiateSigningProcess(inputDTO))
                .expectNext(savedDTO)
                .verifyComplete();

        // Verify status was set to PENDING
        assert inputDTO.getSignatureStatus() == SignatureStatus.PENDING;
        verify(repository).save(any(DocumentSignature.class));
    }

    @Test
    void cancelSignature_ShouldSetCanceledStatus_WhenSignatureIsPending() {
        // Given
        DocumentSignature pendingSignature = DocumentSignature.builder()
                .id(TEST_SIGNATURE_ID)
                .signatureStatus(SignatureStatus.PENDING)
                .build();

        DocumentSignature canceledSignature = DocumentSignature.builder()
                .id(TEST_SIGNATURE_ID)
                .signatureStatus(SignatureStatus.CANCELED)
                .build();

        DocumentSignatureDTO canceledDTO = DocumentSignatureDTO.builder()
                .id(TEST_SIGNATURE_ID)
                .signatureStatus(SignatureStatus.CANCELED)
                .build();

        when(repository.findById(TEST_SIGNATURE_ID)).thenReturn(Mono.just(pendingSignature));
        when(repository.save(any(DocumentSignature.class))).thenReturn(Mono.just(canceledSignature));
        when(mapper.toDTO(canceledSignature)).thenReturn(canceledDTO);

        // When & Then
        StepVerifier.create(documentSignatureService.cancelSignature(TEST_SIGNATURE_ID))
                .expectNext(canceledDTO)
                .verifyComplete();

        verify(repository).findById(TEST_SIGNATURE_ID);
        verify(repository).save(any(DocumentSignature.class));
    }

    @Test
    void cancelSignature_ShouldThrowError_WhenSignatureIsAlreadySigned() {
        // Given
        DocumentSignature signedSignature = DocumentSignature.builder()
                .id(TEST_SIGNATURE_ID)
                .signatureStatus(SignatureStatus.SIGNED)
                .build();

        when(repository.findById(TEST_SIGNATURE_ID)).thenReturn(Mono.just(signedSignature));

        // When & Then
        StepVerifier.create(documentSignatureService.cancelSignature(TEST_SIGNATURE_ID))
                .expectError(IllegalStateException.class)
                .verify();

        verify(repository).findById(TEST_SIGNATURE_ID);
        verify(repository, never()).save(any());
    }

    @Test
    void isDocumentFullySigned_ShouldReturnTrue_WhenNoPendingAndHasSigned() {
        // Given
        when(repository.findByDocumentIdAndSignatureStatus(TEST_DOCUMENT_ID, SignatureStatus.PENDING))
                .thenReturn(Flux.empty()); // No pending signatures
        when(repository.findByDocumentIdAndSignatureStatus(TEST_DOCUMENT_ID, SignatureStatus.SIGNED))
                .thenReturn(Flux.just(testDocumentSignature)); // Has signed signatures

        // When & Then
        StepVerifier.create(documentSignatureService.isDocumentFullySigned(TEST_DOCUMENT_ID))
                .expectNext(true)
                .verifyComplete();

        verify(repository).findByDocumentIdAndSignatureStatus(TEST_DOCUMENT_ID, SignatureStatus.PENDING);
        verify(repository).findByDocumentIdAndSignatureStatus(TEST_DOCUMENT_ID, SignatureStatus.SIGNED);
    }

    @Test
    void isDocumentFullySigned_ShouldReturnFalse_WhenHasPendingSignatures() {
        // Given
        when(repository.findByDocumentIdAndSignatureStatus(TEST_DOCUMENT_ID, SignatureStatus.PENDING))
                .thenReturn(Flux.just(testDocumentSignature)); // Has pending signatures

        // When & Then
        StepVerifier.create(documentSignatureService.isDocumentFullySigned(TEST_DOCUMENT_ID))
                .expectNext(false)
                .verifyComplete();

        verify(repository).findByDocumentIdAndSignatureStatus(TEST_DOCUMENT_ID, SignatureStatus.PENDING);
        verify(repository, never()).findByDocumentIdAndSignatureStatus(TEST_DOCUMENT_ID, SignatureStatus.SIGNED);
    }
}