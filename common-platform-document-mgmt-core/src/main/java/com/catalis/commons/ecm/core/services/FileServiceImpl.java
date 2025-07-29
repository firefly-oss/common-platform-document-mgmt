package com.catalis.commons.ecm.core.services;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.commons.ecm.core.mappers.FileMapper;
import com.catalis.commons.ecm.interfaces.dtos.FileDTO;
import com.catalis.commons.ecm.models.entities.File;
import com.catalis.commons.ecm.models.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository repository;

    @Autowired
    private FileMapper mapper;

    @Override
    public Mono<PaginationResponse<FileDTO>> filterFiles(FilterRequest<FileDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        File.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<FileDTO> createFile(FileDTO fileDTO) {
        return Mono.just(fileDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<FileDTO> updateFile(Long fileId, FileDTO fileDTO) {
        return repository.findById(fileId)
                .switchIfEmpty(Mono.error(new RuntimeException("File not found with ID: " + fileId)))
                .flatMap(existingFile -> {
                    File updatedFile = mapper.toEntity(fileDTO);
                    updatedFile.setId(fileId);
                    return repository.save(updatedFile);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteFile(Long fileId) {
        return repository.findById(fileId)
                .switchIfEmpty(Mono.error(new RuntimeException("File not found with ID: " + fileId)))
                .flatMap(file -> repository.deleteById(fileId));
    }

    @Override
    public Mono<FileDTO> getFileById(Long fileId) {
        return repository.findById(fileId)
                .switchIfEmpty(Mono.error(new RuntimeException("File not found with ID: " + fileId)))
                .map(mapper::toDTO);
    }
}