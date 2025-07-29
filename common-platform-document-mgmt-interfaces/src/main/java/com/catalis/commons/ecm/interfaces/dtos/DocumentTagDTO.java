package com.catalis.commons.ecm.interfaces.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for DocumentTag entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentTagDTO {
    private Long id;
    private Long documentId;
    private Long tagId;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
}