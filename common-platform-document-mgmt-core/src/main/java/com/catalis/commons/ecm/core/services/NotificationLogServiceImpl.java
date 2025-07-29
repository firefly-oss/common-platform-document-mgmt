package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.NotificationLogMapper;
import com.catalis.commons.ecm.interfaces.dtos.NotificationLogDTO;
import com.catalis.commons.ecm.models.entities.NotificationLog;
import com.catalis.commons.ecm.models.repositories.NotificationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class NotificationLogServiceImpl implements NotificationLogService {

    @Autowired
    private NotificationLogRepository repository;

    @Autowired
    private NotificationLogMapper mapper;

    @Override
    public Mono<PaginationResponse<NotificationLogDTO>> filterNotificationLogs(FilterRequest<NotificationLogDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        NotificationLog.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<NotificationLogDTO> createNotificationLog(NotificationLogDTO notificationLogDTO) {
        return Mono.just(notificationLogDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<NotificationLogDTO> updateNotificationLog(Long notificationLogId, NotificationLogDTO notificationLogDTO) {
        return repository.findById(notificationLogId)
                .switchIfEmpty(Mono.error(new RuntimeException("Notification Log not found with ID: " + notificationLogId)))
                .flatMap(existingNotificationLog -> {
                    NotificationLog updatedNotificationLog = mapper.toEntity(notificationLogDTO);
                    updatedNotificationLog.setId(notificationLogId);
                    return repository.save(updatedNotificationLog);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteNotificationLog(Long notificationLogId) {
        return repository.findById(notificationLogId)
                .switchIfEmpty(Mono.error(new RuntimeException("Notification Log not found with ID: " + notificationLogId)))
                .flatMap(notificationLog -> repository.deleteById(notificationLogId));
    }

    @Override
    public Mono<NotificationLogDTO> getNotificationLogById(Long notificationLogId) {
        return repository.findById(notificationLogId)
                .switchIfEmpty(Mono.error(new RuntimeException("Notification Log not found with ID: " + notificationLogId)))
                .map(mapper::toDTO);
    }
}