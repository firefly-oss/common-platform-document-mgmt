package com.catalis.commons.ecm.models.repositories;

import com.catalis.commons.ecm.models.entities.NotificationLog;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationLogRepository extends BaseRepository<NotificationLog, Long> {
}