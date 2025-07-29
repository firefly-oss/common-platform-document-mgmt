package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.DocumentTagMapper;
import com.catalis.commons.ecm.interfaces.dtos.DocumentTagDTO;
import com.catalis.commons.ecm.models.entities.DocumentTag;
import com.catalis.commons.ecm.models.repositories.DocumentTagRepository;
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
class DocumentTagServiceImplTest {

    @Mock
    private DocumentTagRepository repository;

    @Mock
    private DocumentTagMapper mapper;

    @InjectMocks
    private DocumentTagServiceImpl service;

    private DocumentTag documentTag;
    private DocumentTagDTO documentTagDTO;
    private FilterRequest<DocumentTagDTO> filterRequest;
    private PaginationResponse<DocumentTagDTO> paginationResponse;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        // Initialize test data
        now = LocalDateTime.now();
        
        documentTag = new DocumentTag();
        documentTag.setId(1L);
        documentTag.setDocumentId(100L);
        documentTag.setTagId(200L);
        documentTag.setDateCreated(now);
        documentTag.setDateUpdated(now);
        
        documentTagDTO = new DocumentTagDTO();
        documentTagDTO.setId(1L);
        documentTagDTO.setDocumentId(100L);
        documentTagDTO.setTagId(200L);
        documentTagDTO.setDateCreated(now);
        documentTagDTO.setDateUpdated(now);
        
        filterRequest = new FilterRequest<>();
        
        paginationResponse = mock(PaginationResponse.class);
    }

    @Test
    void filterDocumentTags_ShouldReturnFilteredResults() {
        // This test is simplified because FilterUtils is in an external library
        // We're using a mocked static to simulate its behavior
        try (MockedStatic<FilterUtils> filterUtilsMockedStatic = mockStatic(FilterUtils.class)) {
            // Set up the mock to return a Mono with our pagination response
            filterUtilsMockedStatic.when(() -> FilterUtils.createFilter(any(), any()))
                    .thenAnswer((Answer<Object>) invocation -> {
                        // Return an object that will handle the filter method call
                        return new Object() {
                            public Mono<PaginationResponse<DocumentTagDTO>> filter(FilterRequest<DocumentTagDTO> request) {
                                return Mono.just(paginationResponse);
                            }
                        };
                    });
            
            // Act & Assert
            StepVerifier.create(service.filterDocumentTags(filterRequest))
                    .expectNext(paginationResponse)
                    .verifyComplete();
        }
    }
    
    @Test
    void createDocumentTag_ShouldCreateAndReturnDocumentTag() {
        // Arrange
        when(mapper.toEntity(any(DocumentTagDTO.class))).thenReturn(documentTag);
        when(repository.save(any(DocumentTag.class))).thenReturn(Mono.just(documentTag));
        when(mapper.toDTO(any(DocumentTag.class))).thenReturn(documentTagDTO);

        // Act & Assert
        StepVerifier.create(service.createDocumentTag(documentTagDTO))
                .expectNext(documentTagDTO)
                .verifyComplete();

        // Verify
        verify(mapper).toEntity(documentTagDTO);
        verify(repository).save(documentTag);
        verify(mapper).toDTO(documentTag);
    }

    @Test
    void updateDocumentTag_WhenDocumentTagExists_ShouldUpdateAndReturnDocumentTag() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(documentTag));
        when(mapper.toEntity(any(DocumentTagDTO.class))).thenReturn(documentTag);
        when(repository.save(any(DocumentTag.class))).thenReturn(Mono.just(documentTag));
        when(mapper.toDTO(any(DocumentTag.class))).thenReturn(documentTagDTO);

        // Act & Assert
        StepVerifier.create(service.updateDocumentTag(1L, documentTagDTO))
                .expectNext(documentTagDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toEntity(documentTagDTO);
        verify(repository).save(documentTag);
        verify(mapper).toDTO(documentTag);
    }

    @Test
    void updateDocumentTag_WhenDocumentTagDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateDocumentTag(1L, documentTagDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Document Tag not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toEntity(any(DocumentTagDTO.class));
        verify(repository, never()).save(any(DocumentTag.class));
        verify(mapper, never()).toDTO(any(DocumentTag.class));
    }

    @Test
    void deleteDocumentTag_WhenDocumentTagExists_ShouldDeleteDocumentTag() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(documentTag));
        when(repository.deleteById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteDocumentTag(1L))
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteDocumentTag_WhenDocumentTagDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteDocumentTag(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Document Tag not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void getDocumentTagById_WhenDocumentTagExists_ShouldReturnDocumentTag() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(documentTag));
        when(mapper.toDTO(any(DocumentTag.class))).thenReturn(documentTagDTO);

        // Act & Assert
        StepVerifier.create(service.getDocumentTagById(1L))
                .expectNext(documentTagDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toDTO(documentTag);
    }

    @Test
    void getDocumentTagById_WhenDocumentTagDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getDocumentTagById(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Document Tag not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toDTO(any(DocumentTag.class));
    }
}