package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.DocumentStatusMapper;
import com.catalis.commons.ecm.interfaces.dtos.DocumentStatusDTO;
import com.catalis.commons.ecm.models.entities.DocumentStatus;
import com.catalis.commons.ecm.models.repositories.DocumentStatusRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentStatusServiceImplTest {

    @Mock
    private DocumentStatusRepository repository;

    @Mock
    private DocumentStatusMapper mapper;

    @InjectMocks
    private DocumentStatusServiceImpl service;

    private DocumentStatus documentStatus;
    private DocumentStatusDTO documentStatusDTO;
    private FilterRequest<DocumentStatusDTO> filterRequest;
    private PaginationResponse<DocumentStatusDTO> paginationResponse;

    @BeforeEach
    void setUp() {
        // Initialize test data
        documentStatus = new DocumentStatus();
        documentStatus.setId(1L);
        documentStatus.setCode("TEST_STATUS");
        documentStatus.setDescription("Test Status Description");
        
        documentStatusDTO = new DocumentStatusDTO();
        documentStatusDTO.setId(1L);
        documentStatusDTO.setCode("TEST_STATUS");
        documentStatusDTO.setDescription("Test Status Description");
        
        filterRequest = new FilterRequest<>();
        
        paginationResponse = mock(PaginationResponse.class);
    }

    @Test
    void filterDocumentStatuses_ShouldReturnFilteredResults() {
        // This test is simplified because FilterUtils is in an external library
        // We're using a mocked static to simulate its behavior
        try (MockedStatic<FilterUtils> filterUtilsMockedStatic = mockStatic(FilterUtils.class)) {
            // Set up the mock to return a Mono with our pagination response
            filterUtilsMockedStatic.when(() -> FilterUtils.createFilter(any(), any()))
                    .thenAnswer((Answer<Object>) invocation -> {
                        // Return an object that will handle the filter method call
                        return new Object() {
                            public Mono<PaginationResponse<DocumentStatusDTO>> filter(FilterRequest<DocumentStatusDTO> request) {
                                return Mono.just(paginationResponse);
                            }
                        };
                    });
            
            // Act & Assert
            StepVerifier.create(service.filterDocumentStatuses(filterRequest))
                    .expectNext(paginationResponse)
                    .verifyComplete();
        }
    }
    
    @Test
    void createDocumentStatus_ShouldCreateAndReturnDocumentStatus() {
        // Arrange
        when(mapper.toEntity(any(DocumentStatusDTO.class))).thenReturn(documentStatus);
        when(repository.save(any(DocumentStatus.class))).thenReturn(Mono.just(documentStatus));
        when(mapper.toDTO(any(DocumentStatus.class))).thenReturn(documentStatusDTO);

        // Act & Assert
        StepVerifier.create(service.createDocumentStatus(documentStatusDTO))
                .expectNext(documentStatusDTO)
                .verifyComplete();

        // Verify
        verify(mapper).toEntity(documentStatusDTO);
        verify(repository).save(documentStatus);
        verify(mapper).toDTO(documentStatus);
    }

    @Test
    void updateDocumentStatus_WhenDocumentStatusExists_ShouldUpdateAndReturnDocumentStatus() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(documentStatus));
        when(mapper.toEntity(any(DocumentStatusDTO.class))).thenReturn(documentStatus);
        when(repository.save(any(DocumentStatus.class))).thenReturn(Mono.just(documentStatus));
        when(mapper.toDTO(any(DocumentStatus.class))).thenReturn(documentStatusDTO);

        // Act & Assert
        StepVerifier.create(service.updateDocumentStatus(1L, documentStatusDTO))
                .expectNext(documentStatusDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toEntity(documentStatusDTO);
        verify(repository).save(documentStatus);
        verify(mapper).toDTO(documentStatus);
    }

    @Test
    void updateDocumentStatus_WhenDocumentStatusDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateDocumentStatus(1L, documentStatusDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Document Status not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toEntity(any(DocumentStatusDTO.class));
        verify(repository, never()).save(any(DocumentStatus.class));
        verify(mapper, never()).toDTO(any(DocumentStatus.class));
    }

    @Test
    void deleteDocumentStatus_WhenDocumentStatusExists_ShouldDeleteDocumentStatus() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(documentStatus));
        when(repository.deleteById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteDocumentStatus(1L))
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteDocumentStatus_WhenDocumentStatusDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteDocumentStatus(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Document Status not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void getDocumentStatusById_WhenDocumentStatusExists_ShouldReturnDocumentStatus() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(documentStatus));
        when(mapper.toDTO(any(DocumentStatus.class))).thenReturn(documentStatusDTO);

        // Act & Assert
        StepVerifier.create(service.getDocumentStatusById(1L))
                .expectNext(documentStatusDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toDTO(documentStatus);
    }

    @Test
    void getDocumentStatusById_WhenDocumentStatusDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getDocumentStatusById(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Document Status not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toDTO(any(DocumentStatus.class));
    }
}