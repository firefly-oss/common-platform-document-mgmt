package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.NotificationTypeMapper;
import com.catalis.commons.ecm.interfaces.dtos.NotificationTypeDTO;
import com.catalis.commons.ecm.models.entities.NotificationType;
import com.catalis.commons.ecm.models.repositories.NotificationTypeRepository;
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

import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationTypeServiceImplTest {

    @Mock
    private NotificationTypeRepository repository;

    @Mock
    private NotificationTypeMapper mapper;

    @InjectMocks
    private NotificationTypeServiceImpl service;

    private NotificationType notificationType;
    private NotificationTypeDTO notificationTypeDTO;
    private FilterRequest<NotificationTypeDTO> filterRequest;
    private PaginationResponse<NotificationTypeDTO> paginationResponse;

    @BeforeEach
    void setUp() {
        // Initialize test data
        notificationType = new NotificationType();
        notificationType.setId(1L);
        notificationType.setCode("TEST_CODE");
        notificationType.setDescription("Test Description");

        notificationTypeDTO = new NotificationTypeDTO();
        notificationTypeDTO.setId(1L);
        notificationTypeDTO.setCode("TEST_CODE");
        notificationTypeDTO.setDescription("Test Description");

        filterRequest = new FilterRequest<>();
        
        paginationResponse = mock(PaginationResponse.class);
    }

    @Test
    void filterNotificationTypes_ShouldReturnFilteredResults() {
        // This test is simplified because FilterUtils is in an external library
        // We're using a mocked static to simulate its behavior
        try (MockedStatic<FilterUtils> filterUtilsMockedStatic = mockStatic(FilterUtils.class)) {
            // Set up the mock to return a Mono with our pagination response
            filterUtilsMockedStatic.when(() -> FilterUtils.createFilter(any(), any()))
                    .thenAnswer((Answer<Object>) invocation -> {
                        // Return an object that will handle the filter method call
                        return new Object() {
                            public Mono<PaginationResponse<NotificationTypeDTO>> filter(FilterRequest<NotificationTypeDTO> request) {
                                return Mono.just(paginationResponse);
                            }
                        };
                    });
            
            // Act & Assert
            StepVerifier.create(service.filterNotificationTypes(filterRequest))
                    .expectNext(paginationResponse)
                    .verifyComplete();
        }
    }
    
    @Test
    void createNotificationType_ShouldCreateAndReturnNotificationType() {
        // Arrange
        when(mapper.toEntity(any(NotificationTypeDTO.class))).thenReturn(notificationType);
        when(repository.save(any(NotificationType.class))).thenReturn(Mono.just(notificationType));
        when(mapper.toDTO(any(NotificationType.class))).thenReturn(notificationTypeDTO);

        // Act & Assert
        StepVerifier.create(service.createNotificationType(notificationTypeDTO))
                .expectNext(notificationTypeDTO)
                .verifyComplete();

        // Verify
        verify(mapper).toEntity(notificationTypeDTO);
        verify(repository).save(notificationType);
        verify(mapper).toDTO(notificationType);
    }

    @Test
    void updateNotificationType_WhenNotificationTypeExists_ShouldUpdateAndReturnNotificationType() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(notificationType));
        when(mapper.toEntity(any(NotificationTypeDTO.class))).thenReturn(notificationType);
        when(repository.save(any(NotificationType.class))).thenReturn(Mono.just(notificationType));
        when(mapper.toDTO(any(NotificationType.class))).thenReturn(notificationTypeDTO);

        // Act & Assert
        StepVerifier.create(service.updateNotificationType(1L, notificationTypeDTO))
                .expectNext(notificationTypeDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toEntity(notificationTypeDTO);
        verify(repository).save(notificationType);
        verify(mapper).toDTO(notificationType);
    }

    @Test
    void updateNotificationType_WhenNotificationTypeDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateNotificationType(1L, notificationTypeDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Notification Type not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toEntity(any(NotificationTypeDTO.class));
        verify(repository, never()).save(any(NotificationType.class));
        verify(mapper, never()).toDTO(any(NotificationType.class));
    }

    @Test
    void deleteNotificationType_WhenNotificationTypeExists_ShouldDeleteNotificationType() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(notificationType));
        when(repository.deleteById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteNotificationType(1L))
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteNotificationType_WhenNotificationTypeDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteNotificationType(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Notification Type not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void getNotificationTypeById_WhenNotificationTypeExists_ShouldReturnNotificationType() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.just(notificationType));
        when(mapper.toDTO(any(NotificationType.class))).thenReturn(notificationTypeDTO);

        // Act & Assert
        StepVerifier.create(service.getNotificationTypeById(1L))
                .expectNext(notificationTypeDTO)
                .verifyComplete();

        // Verify
        verify(repository).findById(1L);
        verify(mapper).toDTO(notificationType);
    }

    @Test
    void getNotificationTypeById_WhenNotificationTypeDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getNotificationTypeById(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Notification Type not found with ID: 1"))
                .verify();

        // Verify
        verify(repository).findById(1L);
        verify(mapper, never()).toDTO(any(NotificationType.class));
    }
}