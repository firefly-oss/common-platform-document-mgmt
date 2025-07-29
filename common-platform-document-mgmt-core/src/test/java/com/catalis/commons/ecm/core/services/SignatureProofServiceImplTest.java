package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.SignatureProofMapper;
import com.catalis.commons.ecm.interfaces.dtos.SignatureProofDTO;
import com.catalis.commons.ecm.models.entities.SignatureProof;
import com.catalis.commons.ecm.models.repositories.SignatureProofRepository;
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
class SignatureProofServiceImplTest {

    @Mock
    private SignatureProofRepository repository;

    @Mock
    private SignatureProofMapper mapper;

    @InjectMocks
    private SignatureProofServiceImpl service;

    private SignatureProof signatureProof;
    private SignatureProofDTO signatureProofDTO;
    private FilterRequest<SignatureProofDTO> filterRequest;
    private PaginationResponse<SignatureProofDTO> paginationResponse;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        // Initialize test data
        now = LocalDateTime.now();
        
        signatureProof = new SignatureProof();
        signatureProof.setId(1L);
        signatureProof.setSignatureRequestId(100L);
        signatureProof.setProofUrl("https://example.com/proof/123");
        signatureProof.setProofTypeId(10L);
        signatureProof.setProofDate(now);
        signatureProof.setDateCreated(now);
        signatureProof.setDateUpdated(now);
        signatureProof.setCreatedBy("test-user");
        signatureProof.setUpdatedBy("test-user");
        
        signatureProofDTO = new SignatureProofDTO();
        signatureProofDTO.setId(1L);
        signatureProofDTO.setSignatureRequestId(100L);
        signatureProofDTO.setProofUrl("https://example.com/proof/123");
        signatureProofDTO.setProofTypeId(10L);
        signatureProofDTO.setProofDate(now);
        signatureProofDTO.setDateCreated(now);
        signatureProofDTO.setDateUpdated(now);
        signatureProofDTO.setCreatedBy("test-user");
        signatureProofDTO.setUpdatedBy("test-user");
        
        filterRequest = new FilterRequest<>();
        
        paginationResponse = mock(PaginationResponse.class);
    }

    @Test
    void filterSignatureProofs_ShouldReturnFilteredResults() {
        // This test is simplified because FilterUtils is in an external library
        // We're using a mocked static to simulate its behavior
        try (MockedStatic<FilterUtils> filterUtilsMockedStatic = mockStatic(FilterUtils.class)) {
            // Set up the mock to return a Mono with our pagination response
            filterUtilsMockedStatic.when(() -> FilterUtils.createFilter(any(), any()))
                    .thenAnswer((Answer<Object>) invocation -> {
                        // Return an object that will handle the filter method call
                        return new Object() {
                            public Mono<PaginationResponse<SignatureProofDTO>> filter(FilterRequest<SignatureProofDTO> request) {
                                return Mono.just(paginationResponse);
                            }
                        };
                    });
            
            // Act & Assert
            StepVerifier.create(service.filterSignatureProofs(filterRequest))
                    .expectNext(paginationResponse)
                    .verifyComplete();
        }
    }
    
    @Test
    void createSignatureProof_ShouldCreateAndReturnSignatureProof() {
        // Arrange
        when(mapper.toEntity(any(SignatureProofDTO.class))).thenReturn(signatureProof);
        when(repository.save(any(SignatureProof.class))).thenReturn(Mono.just(signatureProof));
        when(mapper.toDTO(any(SignatureProof.class))).thenReturn(signatureProofDTO);

        // Act & Assert
        StepVerifier.create(service.createSignatureProof(signatureProofDTO))
                .expectNext(signatureProofDTO)
                .verifyComplete();

        // Verify
        verify(mapper).toEntity(signatureProofDTO);
        verify(repository).save(signatureProof);
        verify(mapper).toDTO(signatureProof);
    }

    @Test
    void updateSignatureProof_WhenSignatureProofExists_ShouldUpdateAndReturnSignatureProof() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(signatureProof));
        when(mapper.toEntity(any(SignatureProofDTO.class))).thenReturn(signatureProof);
        when(repository.save(any(SignatureProof.class))).thenReturn(Mono.just(signatureProof));
        when(mapper.toDTO(any(SignatureProof.class))).thenReturn(signatureProofDTO);

        // Act & Assert
        StepVerifier.create(service.updateSignatureProof(1L, signatureProofDTO))
                .expectNext(signatureProofDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toEntity(signatureProofDTO);
        verify(repository).save(signatureProof);
        verify(mapper).toDTO(signatureProof);
    }

    @Test
    void updateSignatureProof_WhenSignatureProofDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateSignatureProof(1L, signatureProofDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Signature Proof not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toEntity(any(SignatureProofDTO.class));
        verify(repository, never()).save(any(SignatureProof.class));
        verify(mapper, never()).toDTO(any(SignatureProof.class));
    }

    @Test
    void deleteSignatureProof_WhenSignatureProofExists_ShouldDeleteSignatureProof() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(signatureProof));
        when(repository.deleteById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteSignatureProof(1L))
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteSignatureProof_WhenSignatureProofDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteSignatureProof(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Signature Proof not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void getSignatureProofById_WhenSignatureProofExists_ShouldReturnSignatureProof() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(signatureProof));
        when(mapper.toDTO(any(SignatureProof.class))).thenReturn(signatureProofDTO);

        // Act & Assert
        StepVerifier.create(service.getSignatureProofById(1L))
                .expectNext(signatureProofDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toDTO(signatureProof);
    }

    @Test
    void getSignatureProofById_WhenSignatureProofDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getSignatureProofById(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Signature Proof not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toDTO(any(SignatureProof.class));
    }
}