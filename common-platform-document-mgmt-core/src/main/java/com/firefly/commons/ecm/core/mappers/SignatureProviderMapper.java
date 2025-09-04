/*
 * Copyright 2025 Firefly Software Solutions Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.firefly.commons.ecm.core.mappers;

import com.firefly.commons.ecm.interfaces.dtos.SignatureProviderDTO;
import com.firefly.commons.ecm.models.entities.SignatureProvider;
import org.mapstruct.Mapper;

/**
 * Mapper for converting between SignatureProvider entity and DTO.
 */
@Mapper(componentModel = "spring")
public interface SignatureProviderMapper {
    SignatureProviderDTO toDTO(SignatureProvider entity);
    SignatureProvider toEntity(SignatureProviderDTO dto);
}
