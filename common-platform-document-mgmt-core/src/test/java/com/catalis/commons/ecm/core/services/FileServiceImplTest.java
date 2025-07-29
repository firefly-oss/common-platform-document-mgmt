package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.FileMapper;
import com.catalis.commons.ecm.interfaces.dtos.FileDTO;
import com.catalis.commons.ecm.models.entities.File;
import com.catalis.commons.ecm.models.repositories.FileRepository;
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
class FileServiceImplTest {

    @Mock
    private FileRepository repository;

    @Mock
    private FileMapper mapper;

    @InjectMocks
    private FileServiceImpl service;

    private File file;
    private FileDTO fileDTO;
    private FilterRequest<FileDTO> filterRequest;
    private PaginationResponse<FileDTO> paginationResponse;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        // Initialize test data
        now = LocalDateTime.now();
        
        file = new File();
        file.setId(1L);
        file.setDocumentId(100L);
        file.setFileName("test-file.pdf");
        file.setFileType("application/pdf");
        file.setBlobStorageUrl("https://storage.example.com/test-file.pdf");
        file.setUploadDate(now);
        file.setUploadedBy("test-user");
        file.setDateCreated(now);
        file.setDateUpdated(now);
        
        fileDTO = new FileDTO();
        fileDTO.setId(1L);
        fileDTO.setDocumentId(100L);
        fileDTO.setFileName("test-file.pdf");
        fileDTO.setFileType("application/pdf");
        fileDTO.setBlobStorageUrl("https://storage.example.com/test-file.pdf");
        fileDTO.setUploadDate(now);
        fileDTO.setUploadedBy("test-user");
        fileDTO.setDateCreated(now);
        fileDTO.setDateUpdated(now);
        
        filterRequest = new FilterRequest<>();
        
        paginationResponse = mock(PaginationResponse.class);
    }

    @Test
    void filterFiles_ShouldReturnFilteredResults() {
        // This test is simplified because FilterUtils is in an external library
        // We're using a mocked static to simulate its behavior
        try (MockedStatic<FilterUtils> filterUtilsMockedStatic = mockStatic(FilterUtils.class)) {
            // Set up the mock to return a Mono with our pagination response
            filterUtilsMockedStatic.when(() -> FilterUtils.createFilter(any(), any()))
                    .thenAnswer((Answer<Object>) invocation -> {
                        // Return an object that will handle the filter method call
                        return new Object() {
                            public Mono<PaginationResponse<FileDTO>> filter(FilterRequest<FileDTO> request) {
                                return Mono.just(paginationResponse);
                            }
                        };
                    });
            
            // Act & Assert
            StepVerifier.create(service.filterFiles(filterRequest))
                    .expectNext(paginationResponse)
                    .verifyComplete();
        }
    }
    
    @Test
    void createFile_ShouldCreateAndReturnFile() {
        // Arrange
        when(mapper.toEntity(any(FileDTO.class))).thenReturn(file);
        when(repository.save(any(File.class))).thenReturn(Mono.just(file));
        when(mapper.toDTO(any(File.class))).thenReturn(fileDTO);

        // Act & Assert
        StepVerifier.create(service.createFile(fileDTO))
                .expectNext(fileDTO)
                .verifyComplete();

        // Verify
        verify(mapper).toEntity(fileDTO);
        verify(repository).save(file);
        verify(mapper).toDTO(file);
    }

    @Test
    void updateFile_WhenFileExists_ShouldUpdateAndReturnFile() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(file));
        when(mapper.toEntity(any(FileDTO.class))).thenReturn(file);
        when(repository.save(any(File.class))).thenReturn(Mono.just(file));
        when(mapper.toDTO(any(File.class))).thenReturn(fileDTO);

        // Act & Assert
        StepVerifier.create(service.updateFile(1L, fileDTO))
                .expectNext(fileDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toEntity(fileDTO);
        verify(repository).save(file);
        verify(mapper).toDTO(file);
    }

    @Test
    void updateFile_WhenFileDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateFile(1L, fileDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("File not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toEntity(any(FileDTO.class));
        verify(repository, never()).save(any(File.class));
        verify(mapper, never()).toDTO(any(File.class));
    }

    @Test
    void deleteFile_WhenFileExists_ShouldDeleteFile() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(file));
        when(repository.deleteById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteFile(1L))
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteFile_WhenFileDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteFile(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("File not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void getFileById_WhenFileExists_ShouldReturnFile() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(file));
        when(mapper.toDTO(any(File.class))).thenReturn(fileDTO);

        // Act & Assert
        StepVerifier.create(service.getFileById(1L))
                .expectNext(fileDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toDTO(file);
    }

    @Test
    void getFileById_WhenFileDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getFileById(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("File not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toDTO(any(File.class));
    }
}