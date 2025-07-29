package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.DocumentTypeMapper;
import com.catalis.commons.ecm.interfaces.dtos.DocumentTypeDTO;
import com.catalis.commons.ecm.models.entities.DocumentType;
import com.catalis.commons.ecm.models.repositories.DocumentTypeRepository;
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
class DocumentTypeServiceImplTest {

    @Mock
    private DocumentTypeRepository repository;

    @Mock
    private DocumentTypeMapper mapper;

    @InjectMocks
    private DocumentTypeServiceImpl service;

    private DocumentType documentType;
    private DocumentTypeDTO documentTypeDTO;
    private FilterRequest<DocumentTypeDTO> filterRequest;
    private PaginationResponse<DocumentTypeDTO> paginationResponse;

    @BeforeEach
    void setUp() {
        // Initialize test data
        documentType = new DocumentType();
        documentType.setId(1L);
        documentType.setCode("TEST_TYPE");
        documentType.setDescription("Test Type Description");
        
        documentTypeDTO = new DocumentTypeDTO();
        documentTypeDTO.setId(1L);
        documentTypeDTO.setCode("TEST_TYPE");
        documentTypeDTO.setDescription("Test Type Description");
        
        filterRequest = new FilterRequest<>();
        
        paginationResponse = mock(PaginationResponse.class);
    }

    @Test
    void filterDocumentTypes_ShouldReturnFilteredResults() {
        // This test is simplified because FilterUtils is in an external library
        // We're using a mocked static to simulate its behavior
        try (MockedStatic<FilterUtils> filterUtilsMockedStatic = mockStatic(FilterUtils.class)) {
            // Set up the mock to return a Mono with our pagination response
            filterUtilsMockedStatic.when(() -> FilterUtils.createFilter(any(), any()))
                    .thenAnswer((Answer<Object>) invocation -> {
                        // Return an object that will handle the filter method call
                        return new Object() {
                            public Mono<PaginationResponse<DocumentTypeDTO>> filter(FilterRequest<DocumentTypeDTO> request) {
                                return Mono.just(paginationResponse);
                            }
                        };
                    });
            
            // Act & Assert
            StepVerifier.create(service.filterDocumentTypes(filterRequest))
                    .expectNext(paginationResponse)
                    .verifyComplete();
        }
    }
    
    @Test
    void createDocumentType_ShouldCreateAndReturnDocumentType() {
        // Arrange
        when(mapper.toEntity(any(DocumentTypeDTO.class))).thenReturn(documentType);
        when(repository.save(any(DocumentType.class))).thenReturn(Mono.just(documentType));
        when(mapper.toDTO(any(DocumentType.class))).thenReturn(documentTypeDTO);

        // Act & Assert
        StepVerifier.create(service.createDocumentType(documentTypeDTO))
                .expectNext(documentTypeDTO)
                .verifyComplete();

        // Verify
        verify(mapper).toEntity(documentTypeDTO);
        verify(repository).save(documentType);
        verify(mapper).toDTO(documentType);
    }

    @Test
    void updateDocumentType_WhenDocumentTypeExists_ShouldUpdateAndReturnDocumentType() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(documentType));
        when(mapper.toEntity(any(DocumentTypeDTO.class))).thenReturn(documentType);
        when(repository.save(any(DocumentType.class))).thenReturn(Mono.just(documentType));
        when(mapper.toDTO(any(DocumentType.class))).thenReturn(documentTypeDTO);

        // Act & Assert
        StepVerifier.create(service.updateDocumentType(1L, documentTypeDTO))
                .expectNext(documentTypeDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toEntity(documentTypeDTO);
        verify(repository).save(documentType);
        verify(mapper).toDTO(documentType);
    }

    @Test
    void updateDocumentType_WhenDocumentTypeDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateDocumentType(1L, documentTypeDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Document Type not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toEntity(any(DocumentTypeDTO.class));
        verify(repository, never()).save(any(DocumentType.class));
        verify(mapper, never()).toDTO(any(DocumentType.class));
    }

    @Test
    void deleteDocumentType_WhenDocumentTypeExists_ShouldDeleteDocumentType() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(documentType));
        when(repository.deleteById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteDocumentType(1L))
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteDocumentType_WhenDocumentTypeDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteDocumentType(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Document Type not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void getDocumentTypeById_WhenDocumentTypeExists_ShouldReturnDocumentType() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(documentType));
        when(mapper.toDTO(any(DocumentType.class))).thenReturn(documentTypeDTO);

        // Act & Assert
        StepVerifier.create(service.getDocumentTypeById(1L))
                .expectNext(documentTypeDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toDTO(documentType);
    }

    @Test
    void getDocumentTypeById_WhenDocumentTypeDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getDocumentTypeById(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Document Type not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toDTO(any(DocumentType.class));
    }
}