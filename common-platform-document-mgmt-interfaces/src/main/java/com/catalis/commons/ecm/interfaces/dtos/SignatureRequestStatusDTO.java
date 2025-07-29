package com.catalis.commons.ecm.interfaces.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for SignatureRequestStatus entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignatureRequestStatusDTO {
    private Long id;
    private String code;
    private String description;
}