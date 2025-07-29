package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.DocumentMapper;
import com.catalis.commons.ecm.interfaces.dtos.DocumentDTO;
import com.catalis.commons.ecm.models.entities.Document;
import com.catalis.commons.ecm.models.repositories.DocumentRepository;
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
class DocumentServiceImplTest {

    @Mock
    private DocumentRepository repository;

    @Mock
    private DocumentMapper mapper;

    @InjectMocks
    private DocumentServiceImpl service;

    private Document document;
    private DocumentDTO documentDTO;
    private FilterRequest<DocumentDTO> filterRequest;
    private PaginationResponse<DocumentDTO> paginationResponse;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        // Initialize test data
        now = LocalDateTime.now();
        
        document = new Document();
        document.setId(1L);
        // Set other properties as needed based on the Document entity structure
        
        documentDTO = new DocumentDTO();
        documentDTO.setId(1L);
        // Set other properties as needed based on the DocumentDTO structure
        
        filterRequest = new FilterRequest<>();
        
        paginationResponse = mock(PaginationResponse.class);
    }

    @Test
    void filterDocuments_ShouldReturnFilteredResults() {
        // This test is simplified because FilterUtils is in an external library
        // We're using a mocked static to simulate its behavior
        try (MockedStatic<FilterUtils> filterUtilsMockedStatic = mockStatic(FilterUtils.class)) {
            // Set up the mock to return a Mono with our pagination response
            filterUtilsMockedStatic.when(() -> FilterUtils.createFilter(any(), any()))
                    .thenAnswer((Answer<Object>) invocation -> {
                        // Return an object that will handle the filter method call
                        return new Object() {
                            public Mono<PaginationResponse<DocumentDTO>> filter(FilterRequest<DocumentDTO> request) {
                                return Mono.just(paginationResponse);
                            }
                        };
                    });
            
            // Act & Assert
            StepVerifier.create(service.filterDocuments(filterRequest))
                    .expectNext(paginationResponse)
                    .verifyComplete();
        }
    }
    
    @Test
    void createDocument_ShouldCreateAndReturnDocument() {
        // Arrange
        when(mapper.toEntity(any(DocumentDTO.class))).thenReturn(document);
        when(repository.save(any(Document.class))).thenReturn(Mono.just(document));
        when(mapper.toDTO(any(Document.class))).thenReturn(documentDTO);

        // Act & Assert
        StepVerifier.create(service.createDocument(documentDTO))
                .expectNext(documentDTO)
                .verifyComplete();

        // Verify
        verify(mapper).toEntity(documentDTO);
        verify(repository).save(document);
        verify(mapper).toDTO(document);
    }

    @Test
    void updateDocument_WhenDocumentExists_ShouldUpdateAndReturnDocument() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(document));
        when(mapper.toEntity(any(DocumentDTO.class))).thenReturn(document);
        when(repository.save(any(Document.class))).thenReturn(Mono.just(document));
        when(mapper.toDTO(any(Document.class))).thenReturn(documentDTO);

        // Act & Assert
        StepVerifier.create(service.updateDocument(1L, documentDTO))
                .expectNext(documentDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toEntity(documentDTO);
        verify(repository).save(document);
        verify(mapper).toDTO(document);
    }

    @Test
    void updateDocument_WhenDocumentDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateDocument(1L, documentDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Document not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toEntity(any(DocumentDTO.class));
        verify(repository, never()).save(any(Document.class));
        verify(mapper, never()).toDTO(any(Document.class));
    }

    @Test
    void deleteDocument_WhenDocumentExists_ShouldDeleteDocument() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(document));
        when(repository.deleteById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteDocument(1L))
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteDocument_WhenDocumentDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteDocument(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Document not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void getDocumentById_WhenDocumentExists_ShouldReturnDocument() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(document));
        when(mapper.toDTO(any(Document.class))).thenReturn(documentDTO);

        // Act & Assert
        StepVerifier.create(service.getDocumentById(1L))
                .expectNext(documentDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toDTO(document);
    }

    @Test
    void getDocumentById_WhenDocumentDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getDocumentById(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Document not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toDTO(any(Document.class));
    }
}