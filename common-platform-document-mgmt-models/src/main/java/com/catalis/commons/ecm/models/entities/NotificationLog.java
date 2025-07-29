package com.catalis.commons.ecm.models.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table("notification_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationLog {

    @Id
    private Long id;

    @Column("document_id")
    private Long documentId;

    @Column("signature_request_id")
    private Long signatureRequestId;

    @Column("notification_type_id")
    private Long notificationTypeId;

    @Column("recipient")
    private String recipient;

    @Column("message")
    private String message;

    @Column("sent_at")
    private LocalDateTime sentAt;

    @Column("success")
    private Boolean success;

    @Column("external_id")
    private String externalId;

    @CreatedDate
    @Column("date_created")
    private LocalDateTime dateCreated;

    @LastModifiedDate
    @Column("date_updated")
    private LocalDateTime dateUpdated;
}