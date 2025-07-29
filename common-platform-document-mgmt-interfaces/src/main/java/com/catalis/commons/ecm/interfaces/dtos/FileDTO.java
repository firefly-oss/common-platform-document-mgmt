package com.catalis.commons.ecm.interfaces.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for File entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {
    private Long id;
    private Long documentId;
    private String fileName;
    private String fileType;
    private String blobStorageUrl;
    private LocalDateTime uploadDate;
    private String uploadedBy;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
}