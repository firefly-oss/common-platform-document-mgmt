package com.catalis.commons.ecm.interfaces.dtos.cmis;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing a CMIS repository.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "CMIS repository representation")
public class CmisRepositoryDTO {

    @Schema(description = "Repository ID")
    private String id;

    @Schema(description = "Repository name")
    private String name;

    @Schema(description = "Repository description")
    private String description;

    @Schema(description = "Repository vendor name")
    private String vendorName;

    @Schema(description = "Repository product name")
    private String productName;

    @Schema(description = "Repository product version")
    private String productVersion;

    @Schema(description = "Root folder ID")
    private String rootFolderId;

    @Schema(description = "Repository capabilities")
    private CmisRepositoryCapabilitiesDTO capabilities;

    @Schema(description = "CMIS version supported")
    private String cmisVersionSupported;
}