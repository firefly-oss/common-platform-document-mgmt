package com.firefly.commons.ecm.core.services.impl;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.filters.FilterUtils;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.commons.ecm.core.mappers.FolderMapper;
import com.firefly.commons.ecm.core.services.FolderService;
import com.firefly.commons.ecm.interfaces.dtos.FolderDTO;
import com.firefly.commons.ecm.models.entities.Folder;
import com.firefly.commons.ecm.models.repositories.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import java.util.UUID;
/**
 * Implementation of the FolderService interface.
 */
@Service
@Transactional
public class FolderServiceImpl implements FolderService {

    @Autowired
    private FolderRepository repository;

    @Autowired
    private FolderMapper mapper;

    @Override
    public Mono<FolderDTO> getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<FolderDTO>> filter(FilterRequest<FolderDTO> filterRequest) {
        return FilterUtils.createFilter(
                Folder.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<FolderDTO> update(FolderDTO folder) {
        if (folder.getId() == null) {
            return Mono.error(new IllegalArgumentException("ID cannot be null for update operation"));
        }

        return repository.findById(folder.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Folder not found with ID: " + folder.getId())))
                .flatMap(existingEntity -> {
                    Folder entityToUpdate = mapper.toEntity(folder);
                    // Preserve created info
                    entityToUpdate.setCreatedAt(existingEntity.getCreatedAt());
                    entityToUpdate.setCreatedBy(existingEntity.getCreatedBy());
                    return repository.save(entityToUpdate);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<FolderDTO> create(FolderDTO folder) {
        // Ensure ID is null for create operation
        folder.setId(null);

        Folder entity = mapper.toEntity(folder);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Folder not found with ID: " + id)))
                .flatMap(entity -> repository.delete(entity));
    }
}
