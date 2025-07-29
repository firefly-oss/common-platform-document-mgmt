package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.ReferenceTypeMapper;
import com.catalis.commons.ecm.interfaces.dtos.ReferenceTypeDTO;
import com.catalis.commons.ecm.models.entities.ReferenceType;
import com.catalis.commons.ecm.models.repositories.ReferenceTypeRepository;
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
class ReferenceTypeServiceImplTest {

    @Mock
    private ReferenceTypeRepository repository;

    @Mock
    private ReferenceTypeMapper mapper;

    @InjectMocks
    private ReferenceTypeServiceImpl service;

    private ReferenceType referenceType;
    private ReferenceTypeDTO referenceTypeDTO;
    private FilterRequest<ReferenceTypeDTO> filterRequest;
    private PaginationResponse<ReferenceTypeDTO> paginationResponse;

    @BeforeEach
    void setUp() {
        // Initialize test data
        referenceType = new ReferenceType();
        referenceType.setId(1L);
        referenceType.setCode("TEST_REFERENCE");
        referenceType.setDescription("Test Reference Type");
        
        referenceTypeDTO = new ReferenceTypeDTO();
        referenceTypeDTO.setId(1L);
        referenceTypeDTO.setCode("TEST_REFERENCE");
        referenceTypeDTO.setDescription("Test Reference Type");
        
        filterRequest = new FilterRequest<>();
        
        paginationResponse = mock(PaginationResponse.class);
    }

    @Test
    void filterReferenceTypes_ShouldReturnFilteredResults() {
        // This test is simplified because FilterUtils is in an external library
        // We're using a mocked static to simulate its behavior
        try (MockedStatic<FilterUtils> filterUtilsMockedStatic = mockStatic(FilterUtils.class)) {
            // Set up the mock to return a Mono with our pagination response
            filterUtilsMockedStatic.when(() -> FilterUtils.createFilter(any(), any()))
                    .thenAnswer((Answer<Object>) invocation -> {
                        // Return an object that will handle the filter method call
                        return new Object() {
                            public Mono<PaginationResponse<ReferenceTypeDTO>> filter(FilterRequest<ReferenceTypeDTO> request) {
                                return Mono.just(paginationResponse);
                            }
                        };
                    });
            
            // Act & Assert
            StepVerifier.create(service.filterReferenceTypes(filterRequest))
                    .expectNext(paginationResponse)
                    .verifyComplete();
        }
    }
    
    @Test
    void createReferenceType_ShouldCreateAndReturnReferenceType() {
        // Arrange
        when(mapper.toEntity(any(ReferenceTypeDTO.class))).thenReturn(referenceType);
        when(repository.save(any(ReferenceType.class))).thenReturn(Mono.just(referenceType));
        when(mapper.toDTO(any(ReferenceType.class))).thenReturn(referenceTypeDTO);

        // Act & Assert
        StepVerifier.create(service.createReferenceType(referenceTypeDTO))
                .expectNext(referenceTypeDTO)
                .verifyComplete();

        // Verify
        verify(mapper).toEntity(referenceTypeDTO);
        verify(repository).save(referenceType);
        verify(mapper).toDTO(referenceType);
    }

    @Test
    void updateReferenceType_WhenReferenceTypeExists_ShouldUpdateAndReturnReferenceType() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(referenceType));
        when(mapper.toEntity(any(ReferenceTypeDTO.class))).thenReturn(referenceType);
        when(repository.save(any(ReferenceType.class))).thenReturn(Mono.just(referenceType));
        when(mapper.toDTO(any(ReferenceType.class))).thenReturn(referenceTypeDTO);

        // Act & Assert
        StepVerifier.create(service.updateReferenceType(1L, referenceTypeDTO))
                .expectNext(referenceTypeDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toEntity(referenceTypeDTO);
        verify(repository).save(referenceType);
        verify(mapper).toDTO(referenceType);
    }

    @Test
    void updateReferenceType_WhenReferenceTypeDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateReferenceType(1L, referenceTypeDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Reference Type not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toEntity(any(ReferenceTypeDTO.class));
        verify(repository, never()).save(any(ReferenceType.class));
        verify(mapper, never()).toDTO(any(ReferenceType.class));
    }

    @Test
    void deleteReferenceType_WhenReferenceTypeExists_ShouldDeleteReferenceType() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(referenceType));
        when(repository.deleteById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteReferenceType(1L))
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteReferenceType_WhenReferenceTypeDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteReferenceType(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Reference Type not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void getReferenceTypeById_WhenReferenceTypeExists_ShouldReturnReferenceType() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(referenceType));
        when(mapper.toDTO(any(ReferenceType.class))).thenReturn(referenceTypeDTO);

        // Act & Assert
        StepVerifier.create(service.getReferenceTypeById(1L))
                .expectNext(referenceTypeDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toDTO(referenceType);
    }

    @Test
    void getReferenceTypeById_WhenReferenceTypeDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getReferenceTypeById(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Reference Type not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toDTO(any(ReferenceType.class));
    }
}