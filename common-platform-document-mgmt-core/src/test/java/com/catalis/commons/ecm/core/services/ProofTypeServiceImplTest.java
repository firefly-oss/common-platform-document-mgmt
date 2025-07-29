package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.ProofTypeMapper;
import com.catalis.commons.ecm.interfaces.dtos.ProofTypeDTO;
import com.catalis.commons.ecm.models.entities.ProofType;
import com.catalis.commons.ecm.models.repositories.ProofTypeRepository;
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
class ProofTypeServiceImplTest {

    @Mock
    private ProofTypeRepository repository;

    @Mock
    private ProofTypeMapper mapper;

    @InjectMocks
    private ProofTypeServiceImpl service;

    private ProofType proofType;
    private ProofTypeDTO proofTypeDTO;
    private FilterRequest<ProofTypeDTO> filterRequest;
    private PaginationResponse<ProofTypeDTO> paginationResponse;

    @BeforeEach
    void setUp() {
        // Initialize test data
        proofType = new ProofType();
        proofType.setId(1L);
        proofType.setCode("TEST_PROOF");
        proofType.setDescription("Test Proof Type");
        
        proofTypeDTO = new ProofTypeDTO();
        proofTypeDTO.setId(1L);
        proofTypeDTO.setCode("TEST_PROOF");
        proofTypeDTO.setDescription("Test Proof Type");
        
        filterRequest = new FilterRequest<>();
        
        paginationResponse = mock(PaginationResponse.class);
    }

    @Test
    void filterProofTypes_ShouldReturnFilteredResults() {
        // This test is simplified because FilterUtils is in an external library
        // We're using a mocked static to simulate its behavior
        try (MockedStatic<FilterUtils> filterUtilsMockedStatic = mockStatic(FilterUtils.class)) {
            // Set up the mock to return a Mono with our pagination response
            filterUtilsMockedStatic.when(() -> FilterUtils.createFilter(any(), any()))
                    .thenAnswer((Answer<Object>) invocation -> {
                        // Return an object that will handle the filter method call
                        return new Object() {
                            public Mono<PaginationResponse<ProofTypeDTO>> filter(FilterRequest<ProofTypeDTO> request) {
                                return Mono.just(paginationResponse);
                            }
                        };
                    });
            
            // Act & Assert
            StepVerifier.create(service.filterProofTypes(filterRequest))
                    .expectNext(paginationResponse)
                    .verifyComplete();
        }
    }
    
    @Test
    void createProofType_ShouldCreateAndReturnProofType() {
        // Arrange
        when(mapper.toEntity(any(ProofTypeDTO.class))).thenReturn(proofType);
        when(repository.save(any(ProofType.class))).thenReturn(Mono.just(proofType));
        when(mapper.toDTO(any(ProofType.class))).thenReturn(proofTypeDTO);

        // Act & Assert
        StepVerifier.create(service.createProofType(proofTypeDTO))
                .expectNext(proofTypeDTO)
                .verifyComplete();

        // Verify
        verify(mapper).toEntity(proofTypeDTO);
        verify(repository).save(proofType);
        verify(mapper).toDTO(proofType);
    }

    @Test
    void updateProofType_WhenProofTypeExists_ShouldUpdateAndReturnProofType() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(proofType));
        when(mapper.toEntity(any(ProofTypeDTO.class))).thenReturn(proofType);
        when(repository.save(any(ProofType.class))).thenReturn(Mono.just(proofType));
        when(mapper.toDTO(any(ProofType.class))).thenReturn(proofTypeDTO);

        // Act & Assert
        StepVerifier.create(service.updateProofType(1L, proofTypeDTO))
                .expectNext(proofTypeDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toEntity(proofTypeDTO);
        verify(repository).save(proofType);
        verify(mapper).toDTO(proofType);
    }

    @Test
    void updateProofType_WhenProofTypeDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateProofType(1L, proofTypeDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Proof Type not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toEntity(any(ProofTypeDTO.class));
        verify(repository, never()).save(any(ProofType.class));
        verify(mapper, never()).toDTO(any(ProofType.class));
    }

    @Test
    void deleteProofType_WhenProofTypeExists_ShouldDeleteProofType() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(proofType));
        when(repository.deleteById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteProofType(1L))
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteProofType_WhenProofTypeDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteProofType(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Proof Type not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void getProofTypeById_WhenProofTypeExists_ShouldReturnProofType() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(proofType));
        when(mapper.toDTO(any(ProofType.class))).thenReturn(proofTypeDTO);

        // Act & Assert
        StepVerifier.create(service.getProofTypeById(1L))
                .expectNext(proofTypeDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toDTO(proofType);
    }

    @Test
    void getProofTypeById_WhenProofTypeDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getProofTypeById(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Proof Type not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toDTO(any(ProofType.class));
    }
}