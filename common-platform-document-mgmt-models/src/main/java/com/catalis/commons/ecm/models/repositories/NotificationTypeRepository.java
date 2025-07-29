package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.entities.NotificationType;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTypeRepository extends BaseRepository<NotificationType, Long> {
}