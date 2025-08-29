package com.firefly.commons.ecm.interfaces.dtos.cmis;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing CMIS repository capabilities.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "CMIS repository capabilities")
public class CmisRepositoryCapabilitiesDTO {

    @Schema(description = "Supports content stream")
    private Boolean contentStreamUpdatability;

    @Schema(description = "Supports changes")
    private Boolean changesCapability;

    @Schema(description = "Supports renditions")
    private Boolean renditionsCapability;

    @Schema(description = "Supports getting descendants")
    private Boolean getDescendantsSupported;

    @Schema(description = "Supports getting folder tree")
    private Boolean getFolderTreeSupported;

    @Schema(description = "Supports multifiling")
    private Boolean multifilingSupported;

    @Schema(description = "Supports unfiling")
    private Boolean unfilingSupported;

    @Schema(description = "Supports version specific filing")
    private Boolean versionSpecificFilingSupported;

    @Schema(description = "Supports PWC updatable")
    private Boolean pwcUpdatableSupported;

    @Schema(description = "Supports PWC searchable")
    private Boolean pwcSearchableSupported;

    @Schema(description = "Supports all versions searchable")
    private Boolean allVersionsSearchableSupported;

    @Schema(description = "Supports query")
    private Boolean querySupported;

    @Schema(description = "Supports joins")
    private Boolean joinSupported;

    @Schema(description = "Supports ACL")
    private Boolean aclSupported;
}