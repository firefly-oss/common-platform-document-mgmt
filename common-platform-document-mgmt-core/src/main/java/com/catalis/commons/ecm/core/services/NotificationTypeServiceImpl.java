package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.NotificationTypeMapper;
import com.catalis.commons.ecm.interfaces.dtos.NotificationTypeDTO;
import com.catalis.commons.ecm.models.entities.NotificationType;
import com.catalis.commons.ecm.models.repositories.NotificationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class NotificationTypeServiceImpl implements NotificationTypeService {

    @Autowired
    private NotificationTypeRepository repository;

    @Autowired
    private NotificationTypeMapper mapper;

    @Override
    public Mono<PaginationResponse<NotificationTypeDTO>> filterNotificationTypes(FilterRequest<NotificationTypeDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        NotificationType.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<NotificationTypeDTO> createNotificationType(NotificationTypeDTO notificationTypeDTO) {
        return Mono.just(notificationTypeDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<NotificationTypeDTO> updateNotificationType(Long notificationTypeId, NotificationTypeDTO notificationTypeDTO) {
        return repository.findById(notificationTypeId)
                .switchIfEmpty(Mono.error(new RuntimeException("Notification Type not found with ID: " + notificationTypeId)))
                .flatMap(existingNotificationType -> {
                    NotificationType updatedNotificationType = mapper.toEntity(notificationTypeDTO);
                    updatedNotificationType.setId(notificationTypeId);
                    return repository.save(updatedNotificationType);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteNotificationType(Long notificationTypeId) {
        return repository.findById(notificationTypeId)
                .switchIfEmpty(Mono.error(new RuntimeException("Notification Type not found with ID: " + notificationTypeId)))
                .flatMap(notificationType -> repository.deleteById(notificationTypeId));
    }

    @Override
    public Mono<NotificationTypeDTO> getNotificationTypeById(Long notificationTypeId) {
        return repository.findById(notificationTypeId)
                .switchIfEmpty(Mono.error(new RuntimeException("Notification Type not found with ID: " + notificationTypeId)))
                .map(mapper::toDTO);
    }
}