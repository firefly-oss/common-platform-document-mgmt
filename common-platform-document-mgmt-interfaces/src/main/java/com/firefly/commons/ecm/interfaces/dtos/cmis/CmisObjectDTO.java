package com.firefly.commons.ecm.interfaces.dtos.cmis;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object representing a CMIS object.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "CMIS object representation")
public class CmisObjectDTO {

    @Schema(description = "Object ID")
    private String id;

    @Schema(description = "Object name")
    private String name;

    @Schema(description = "Object base type ID (e.g., cmis:document, cmis:folder)")
    private String baseTypeId;

    @Schema(description = "Object type ID")
    private String objectTypeId;

    @Schema(description = "Creation date")
    private LocalDateTime creationDate;

    @Schema(description = "Last modification date")
    private LocalDateTime lastModificationDate;

    @Schema(description = "Created by")
    private String createdBy;

    @Schema(description = "Last modified by")
    private String lastModifiedBy;

    @Schema(description = "Path")
    private String path;

    @Schema(description = "Parent ID")
    private String parentId;

    @Schema(description = "Content stream ID")
    private String contentStreamId;

    @Schema(description = "Content stream file name")
    private String contentStreamFileName;

    @Schema(description = "Content stream MIME type")
    private String contentStreamMimeType;

    @Schema(description = "Content stream length")
    private Long contentStreamLength;

    @Schema(description = "Version label")
    private String versionLabel;

    @Schema(description = "Is latest version")
    private Boolean isLatestVersion;

    @Schema(description = "Is major version")
    private Boolean isMajorVersion;

    @Schema(description = "Version series ID")
    private String versionSeriesId;

    @Schema(description = "Checkin comment")
    private String checkinComment;

    @Schema(description = "Properties")
    @Builder.Default
    private Map<String, Object> properties = new HashMap<>();

    @Schema(description = "Children")
    private List<CmisObjectDTO> children;
}