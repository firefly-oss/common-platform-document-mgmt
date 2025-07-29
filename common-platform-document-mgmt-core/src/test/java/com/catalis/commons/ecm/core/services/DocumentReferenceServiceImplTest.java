package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.DocumentReferenceMapper;
import com.catalis.commons.ecm.interfaces.dtos.DocumentReferenceDTO;
import com.catalis.commons.ecm.models.entities.DocumentReference;
import com.catalis.commons.ecm.models.repositories.DocumentReferenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentReferenceServiceImplTest {

    @Mock
    private DocumentReferenceRepository repository;

    @Mock
    private DocumentReferenceMapper mapper;

    @InjectMocks
    private DocumentReferenceServiceImpl service;

    private DocumentReference documentReference;
    private DocumentReferenceDTO documentReferenceDTO;
    private FilterRequest<DocumentReferenceDTO> filterRequest;
    private PaginationResponse<DocumentReferenceDTO> paginationResponse;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        // Initialize test data
        now = LocalDateTime.now();
        
        documentReference = new DocumentReference();
        documentReference.setId(1L);
        documentReference.setDocumentId(100L);
        documentReference.setRelatedDocumentId(200L);
        documentReference.setReferenceTypeId(10L);
        documentReference.setNote("Test Note");
        documentReference.setDateCreated(now);
        documentReference.setDateUpdated(now);
        documentReference.setCreatedBy("test-user");
        documentReference.setUpdatedBy("test-user");

        documentReferenceDTO = new DocumentReferenceDTO();
        documentReferenceDTO.setId(1L);
        documentReferenceDTO.setDocumentId(100L);
        documentReferenceDTO.setRelatedDocumentId(200L);
        documentReferenceDTO.setReferenceTypeId(10L);
        documentReferenceDTO.setNote("Test Note");
        documentReferenceDTO.setDateCreated(now);
        documentReferenceDTO.setDateUpdated(now);
        documentReferenceDTO.setCreatedBy("test-user");
        documentReferenceDTO.setUpdatedBy("test-user");

        filterRequest = new FilterRequest<>();
        
        paginationResponse = mock(PaginationResponse.class);
    }

    @Test
    void filterDocumentReferences_ShouldReturnFilteredResults() {
        // This test is simplified because FilterUtils is in an external library
        // We're using a mocked static to simulate its behavior
        try (MockedStatic<FilterUtils> filterUtilsMockedStatic = mockStatic(FilterUtils.class)) {
            // Set up the mock to return a Mono with our pagination response
            filterUtilsMockedStatic.when(() -> FilterUtils.createFilter(any(), any()))
                    .thenAnswer((Answer<Object>) invocation -> {
                        // Return an object that will handle the filter method call
                        return new Object() {
                            public Mono<PaginationResponse<DocumentReferenceDTO>> filter(FilterRequest<DocumentReferenceDTO> request) {
                                return Mono.just(paginationResponse);
                            }
                        };
                    });
            
            // Act & Assert
            StepVerifier.create(service.filterDocumentReferences(filterRequest))
                    .expectNext(paginationResponse)
                    .verifyComplete();
        }
    }
    
    @Test
    void createDocumentReference_ShouldCreateAndReturnDocumentReference() {
        // Arrange
        when(mapper.toEntity(any(DocumentReferenceDTO.class))).thenReturn(documentReference);
        when(repository.save(any(DocumentReference.class))).thenReturn(Mono.just(documentReference));
        when(mapper.toDTO(any(DocumentReference.class))).thenReturn(documentReferenceDTO);

        // Act & Assert
        StepVerifier.create(service.createDocumentReference(documentReferenceDTO))
                .expectNext(documentReferenceDTO)
                .verifyComplete();

        // Verify
        verify(mapper).toEntity(documentReferenceDTO);
        verify(repository).save(documentReference);
        verify(mapper).toDTO(documentReference);
    }

    @Test
    void updateDocumentReference_WhenDocumentReferenceExists_ShouldUpdateAndReturnDocumentReference() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(documentReference));
        when(mapper.toEntity(any(DocumentReferenceDTO.class))).thenReturn(documentReference);
        when(repository.save(any(DocumentReference.class))).thenReturn(Mono.just(documentReference));
        when(mapper.toDTO(any(DocumentReference.class))).thenReturn(documentReferenceDTO);

        // Act & Assert
        StepVerifier.create(service.updateDocumentReference(1L, documentReferenceDTO))
                .expectNext(documentReferenceDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toEntity(documentReferenceDTO);
        verify(repository).save(documentReference);
        verify(mapper).toDTO(documentReference);
    }

    @Test
    void updateDocumentReference_WhenDocumentReferenceDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateDocumentReference(1L, documentReferenceDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Document Reference not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toEntity(any(DocumentReferenceDTO.class));
        verify(repository, never()).save(any(DocumentReference.class));
        verify(mapper, never()).toDTO(any(DocumentReference.class));
    }

    @Test
    void deleteDocumentReference_WhenDocumentReferenceExists_ShouldDeleteDocumentReference() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(documentReference));
        when(repository.deleteById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteDocumentReference(1L))
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteDocumentReference_WhenDocumentReferenceDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteDocumentReference(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Document Reference not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void getDocumentReferenceById_WhenDocumentReferenceExists_ShouldReturnDocumentReference() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(documentReference));
        when(mapper.toDTO(any(DocumentReference.class))).thenReturn(documentReferenceDTO);

        // Act & Assert
        StepVerifier.create(service.getDocumentReferenceById(1L))
                .expectNext(documentReferenceDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toDTO(documentReference);
    }

    @Test
    void getDocumentReferenceById_WhenDocumentReferenceDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getDocumentReferenceById(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Document Reference not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toDTO(any(DocumentReference.class));
    }
}