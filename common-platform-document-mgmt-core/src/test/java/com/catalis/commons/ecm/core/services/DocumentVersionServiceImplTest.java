package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.DocumentVersionMapper;
import com.catalis.commons.ecm.interfaces.dtos.DocumentVersionDTO;
import com.catalis.commons.ecm.models.entities.DocumentVersion;
import com.catalis.commons.ecm.models.repositories.DocumentVersionRepository;
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
class DocumentVersionServiceImplTest {

    @Mock
    private DocumentVersionRepository repository;

    @Mock
    private DocumentVersionMapper mapper;

    @InjectMocks
    private DocumentVersionServiceImpl service;

    private DocumentVersion documentVersion;
    private DocumentVersionDTO documentVersionDTO;
    private FilterRequest<DocumentVersionDTO> filterRequest;
    private PaginationResponse<DocumentVersionDTO> paginationResponse;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        // Initialize test data
        now = LocalDateTime.now();
        
        documentVersion = new DocumentVersion();
        documentVersion.setId(1L);
        documentVersion.setDocumentId(100L);
        documentVersion.setVersionNumber(1L);
        documentVersion.setChangeSummary("Initial version");
        documentVersion.setFileName("test-document.pdf");
        documentVersion.setBlobStorageUrl("https://storage.example.com/test-document.pdf");
        documentVersion.setEffectiveDate(now);
        documentVersion.setDateCreated(now);
        documentVersion.setDateUpdated(now);
        documentVersion.setCreatedBy("test-user");
        documentVersion.setUpdatedBy("test-user");
        
        documentVersionDTO = new DocumentVersionDTO();
        documentVersionDTO.setId(1L);
        documentVersionDTO.setDocumentId(100L);
        documentVersionDTO.setVersionNumber(1L);
        documentVersionDTO.setChangeSummary("Initial version");
        documentVersionDTO.setFileName("test-document.pdf");
        documentVersionDTO.setBlobStorageUrl("https://storage.example.com/test-document.pdf");
        documentVersionDTO.setEffectiveDate(now);
        documentVersionDTO.setDateCreated(now);
        documentVersionDTO.setDateUpdated(now);
        documentVersionDTO.setCreatedBy("test-user");
        documentVersionDTO.setUpdatedBy("test-user");
        
        filterRequest = new FilterRequest<>();
        
        paginationResponse = mock(PaginationResponse.class);
    }

    @Test
    void filterDocumentVersions_ShouldReturnFilteredResults() {
        // This test is simplified because FilterUtils is in an external library
        // We're using a mocked static to simulate its behavior
        try (MockedStatic<FilterUtils> filterUtilsMockedStatic = mockStatic(FilterUtils.class)) {
            // Set up the mock to return a Mono with our pagination response
            filterUtilsMockedStatic.when(() -> FilterUtils.createFilter(any(), any()))
                    .thenAnswer((Answer<Object>) invocation -> {
                        // Return an object that will handle the filter method call
                        return new Object() {
                            public Mono<PaginationResponse<DocumentVersionDTO>> filter(FilterRequest<DocumentVersionDTO> request) {
                                return Mono.just(paginationResponse);
                            }
                        };
                    });
            
            // Act & Assert
            StepVerifier.create(service.filterDocumentVersions(filterRequest))
                    .expectNext(paginationResponse)
                    .verifyComplete();
        }
    }
    
    @Test
    void createDocumentVersion_ShouldCreateAndReturnDocumentVersion() {
        // Arrange
        when(mapper.toEntity(any(DocumentVersionDTO.class))).thenReturn(documentVersion);
        when(repository.save(any(DocumentVersion.class))).thenReturn(Mono.just(documentVersion));
        when(mapper.toDTO(any(DocumentVersion.class))).thenReturn(documentVersionDTO);

        // Act & Assert
        StepVerifier.create(service.createDocumentVersion(documentVersionDTO))
                .expectNext(documentVersionDTO)
                .verifyComplete();

        // Verify
        verify(mapper).toEntity(documentVersionDTO);
        verify(repository).save(documentVersion);
        verify(mapper).toDTO(documentVersion);
    }

    @Test
    void updateDocumentVersion_WhenDocumentVersionExists_ShouldUpdateAndReturnDocumentVersion() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(documentVersion));
        when(mapper.toEntity(any(DocumentVersionDTO.class))).thenReturn(documentVersion);
        when(repository.save(any(DocumentVersion.class))).thenReturn(Mono.just(documentVersion));
        when(mapper.toDTO(any(DocumentVersion.class))).thenReturn(documentVersionDTO);

        // Act & Assert
        StepVerifier.create(service.updateDocumentVersion(1L, documentVersionDTO))
                .expectNext(documentVersionDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toEntity(documentVersionDTO);
        verify(repository).save(documentVersion);
        verify(mapper).toDTO(documentVersion);
    }

    @Test
    void updateDocumentVersion_WhenDocumentVersionDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateDocumentVersion(1L, documentVersionDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Document Version not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toEntity(any(DocumentVersionDTO.class));
        verify(repository, never()).save(any(DocumentVersion.class));
        verify(mapper, never()).toDTO(any(DocumentVersion.class));
    }

    @Test
    void deleteDocumentVersion_WhenDocumentVersionExists_ShouldDeleteDocumentVersion() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(documentVersion));
        when(repository.deleteById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteDocumentVersion(1L))
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteDocumentVersion_WhenDocumentVersionDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteDocumentVersion(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Document Version not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void getDocumentVersionById_WhenDocumentVersionExists_ShouldReturnDocumentVersion() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(documentVersion));
        when(mapper.toDTO(any(DocumentVersion.class))).thenReturn(documentVersionDTO);

        // Act & Assert
        StepVerifier.create(service.getDocumentVersionById(1L))
                .expectNext(documentVersionDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toDTO(documentVersion);
    }

    @Test
    void getDocumentVersionById_WhenDocumentVersionDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getDocumentVersionById(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Document Version not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toDTO(any(DocumentVersion.class));
    }
}