package com.firefly.commons.ecm.web.controllers;

import com.firefly.commons.ecm.interfaces.dtos.cmis.CmisObjectDTO;
import com.firefly.commons.ecm.interfaces.dtos.cmis.CmisRepositoryDTO;
import com.firefly.commons.ecm.core.services.CmisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

/**
 * Tests for the CMIS Controller.
 */
@ExtendWith(MockitoExtension.class)
public class CmisControllerTest {

    @Mock
    private CmisService cmisService;

    @InjectMocks
    private CmisController cmisController;

    private CmisRepositoryDTO repository;
    private CmisObjectDTO rootFolder;

    @BeforeEach
    public void setup() {
        // Setup test data
        repository = CmisRepositoryDTO.builder()
                .id("ecm-repository")
                .name("ECM Repository")
                .description("Enterprise Content Management Repository")
                .rootFolderId("1")
                .build();

        rootFolder = CmisObjectDTO.builder()
                .id("1")
                .name("Root")
                .baseTypeId("cmis:folder")
                .objectTypeId("cmis:folder")
                .properties(new HashMap<>())
                .build();

        // Setup mock responses with lenient stubbing to avoid UnnecessaryStubbingException
        lenient().when(cmisService.getRepositories()).thenReturn(Flux.just(repository));
        lenient().when(cmisService.getRepository(anyString())).thenReturn(Mono.just(repository));
        lenient().when(cmisService.getRootFolder(anyString())).thenReturn(Mono.just(rootFolder));
    }

    @Test
    public void testGetRepositories() {
        // Test
        Flux<CmisRepositoryDTO> result = cmisController.getRepositories();

        // Verify
        StepVerifier.create(result)
                .expectNext(repository)
                .verifyComplete();
    }

    @Test
    public void testGetRepository() {
        // Test
        Mono<CmisRepositoryDTO> result = cmisController.getRepository("ecm-repository");

        // Verify
        StepVerifier.create(result)
                .expectNext(repository)
                .verifyComplete();
    }

    @Test
    public void testGetRepositoryNotFound() {
        // Setup
        when(cmisService.getRepository("non-existent")).thenReturn(Mono.empty());

        // Test
        Mono<CmisRepositoryDTO> result = cmisController.getRepository("non-existent");

        // Verify
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void testGetRootFolder() {
        // Test
        Mono<CmisObjectDTO> result = cmisController.getRootFolder("ecm-repository");

        // Verify
        StepVerifier.create(result)
                .expectNext(rootFolder)
                .verifyComplete();
    }

    @Test
    public void testGetRootFolderNotFound() {
        // Setup
        when(cmisService.getRootFolder("non-existent")).thenReturn(Mono.empty());

        // Test
        Mono<CmisObjectDTO> result = cmisController.getRootFolder("non-existent");

        // Verify
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }

    // Additional tests for other endpoints can be added here
}
