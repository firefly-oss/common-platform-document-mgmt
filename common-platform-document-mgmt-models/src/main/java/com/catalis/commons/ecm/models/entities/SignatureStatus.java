package com.catalis.commons.ecm.models.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("signature_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignatureStatus {

    @Id
    private Long id;

    @Column("code")
    private String code;

    @Column("description")
    private String description;
}