package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.NotificationLogMapper;
import com.catalis.commons.ecm.interfaces.dtos.NotificationLogDTO;
import com.catalis.commons.ecm.models.entities.NotificationLog;
import com.catalis.commons.ecm.models.repositories.NotificationLogRepository;
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
class NotificationLogServiceImplTest {

    @Mock
    private NotificationLogRepository repository;

    @Mock
    private NotificationLogMapper mapper;

    @InjectMocks
    private NotificationLogServiceImpl service;

    private NotificationLog notificationLog;
    private NotificationLogDTO notificationLogDTO;
    private FilterRequest<NotificationLogDTO> filterRequest;
    private PaginationResponse<NotificationLogDTO> paginationResponse;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        // Initialize test data
        now = LocalDateTime.now();
        
        notificationLog = new NotificationLog();
        notificationLog.setId(1L);
        notificationLog.setDocumentId(100L);
        notificationLog.setSignatureRequestId(200L);
        notificationLog.setNotificationTypeId(10L);
        notificationLog.setRecipient("test@example.com");
        notificationLog.setMessage("Test notification message");
        notificationLog.setSentAt(now);
        notificationLog.setSuccess(true);
        notificationLog.setExternalId("ext-123");
        notificationLog.setDateCreated(now);
        notificationLog.setDateUpdated(now);
        
        notificationLogDTO = new NotificationLogDTO();
        notificationLogDTO.setId(1L);
        notificationLogDTO.setDocumentId(100L);
        notificationLogDTO.setSignatureRequestId(200L);
        notificationLogDTO.setNotificationTypeId(10L);
        notificationLogDTO.setRecipient("test@example.com");
        notificationLogDTO.setMessage("Test notification message");
        notificationLogDTO.setSentAt(now);
        notificationLogDTO.setSuccess(true);
        notificationLogDTO.setExternalId("ext-123");
        notificationLogDTO.setDateCreated(now);
        notificationLogDTO.setDateUpdated(now);
        
        filterRequest = new FilterRequest<>();
        
        paginationResponse = mock(PaginationResponse.class);
    }

    @Test
    void filterNotificationLogs_ShouldReturnFilteredResults() {
        // This test is simplified because FilterUtils is in an external library
        // We're using a mocked static to simulate its behavior
        try (MockedStatic<FilterUtils> filterUtilsMockedStatic = mockStatic(FilterUtils.class)) {
            // Set up the mock to return a Mono with our pagination response
            filterUtilsMockedStatic.when(() -> FilterUtils.createFilter(any(), any()))
                    .thenAnswer((Answer<Object>) invocation -> {
                        // Return an object that will handle the filter method call
                        return new Object() {
                            public Mono<PaginationResponse<NotificationLogDTO>> filter(FilterRequest<NotificationLogDTO> request) {
                                return Mono.just(paginationResponse);
                            }
                        };
                    });
            
            // Act & Assert
            StepVerifier.create(service.filterNotificationLogs(filterRequest))
                    .expectNext(paginationResponse)
                    .verifyComplete();
        }
    }
    
    @Test
    void createNotificationLog_ShouldCreateAndReturnNotificationLog() {
        // Arrange
        when(mapper.toEntity(any(NotificationLogDTO.class))).thenReturn(notificationLog);
        when(repository.save(any(NotificationLog.class))).thenReturn(Mono.just(notificationLog));
        when(mapper.toDTO(any(NotificationLog.class))).thenReturn(notificationLogDTO);

        // Act & Assert
        StepVerifier.create(service.createNotificationLog(notificationLogDTO))
                .expectNext(notificationLogDTO)
                .verifyComplete();

        // Verify
        verify(mapper).toEntity(notificationLogDTO);
        verify(repository).save(notificationLog);
        verify(mapper).toDTO(notificationLog);
    }

    @Test
    void updateNotificationLog_WhenNotificationLogExists_ShouldUpdateAndReturnNotificationLog() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(notificationLog));
        when(mapper.toEntity(any(NotificationLogDTO.class))).thenReturn(notificationLog);
        when(repository.save(any(NotificationLog.class))).thenReturn(Mono.just(notificationLog));
        when(mapper.toDTO(any(NotificationLog.class))).thenReturn(notificationLogDTO);

        // Act & Assert
        StepVerifier.create(service.updateNotificationLog(1L, notificationLogDTO))
                .expectNext(notificationLogDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toEntity(notificationLogDTO);
        verify(repository).save(notificationLog);
        verify(mapper).toDTO(notificationLog);
    }

    @Test
    void updateNotificationLog_WhenNotificationLogDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateNotificationLog(1L, notificationLogDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Notification Log not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toEntity(any(NotificationLogDTO.class));
        verify(repository, never()).save(any(NotificationLog.class));
        verify(mapper, never()).toDTO(any(NotificationLog.class));
    }

    @Test
    void deleteNotificationLog_WhenNotificationLogExists_ShouldDeleteNotificationLog() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(notificationLog));
        when(repository.deleteById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteNotificationLog(1L))
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteNotificationLog_WhenNotificationLogDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteNotificationLog(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Notification Log not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void getNotificationLogById_WhenNotificationLogExists_ShouldReturnNotificationLog() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(notificationLog));
        when(mapper.toDTO(any(NotificationLog.class))).thenReturn(notificationLogDTO);

        // Act & Assert
        StepVerifier.create(service.getNotificationLogById(1L))
                .expectNext(notificationLogDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toDTO(notificationLog);
    }

    @Test
    void getNotificationLogById_WhenNotificationLogDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getNotificationLogById(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Notification Log not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toDTO(any(NotificationLog.class));
    }
}