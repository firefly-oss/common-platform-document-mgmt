package com.catalis.commons.ecm.core.services.impl;

import com.catalis.commons.ecm.core.config.CmisProperties;
import com.catalis.commons.ecm.core.services.DocumentService;
import com.catalis.commons.ecm.core.services.FolderService;
import com.catalis.commons.ecm.interfaces.dtos.DocumentDTO;
import com.catalis.commons.ecm.interfaces.dtos.FolderDTO;
import com.catalis.commons.ecm.interfaces.dtos.cmis.CmisObjectDTO;
import com.catalis.commons.ecm.interfaces.dtos.cmis.CmisRepositoryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CmisServiceImplTest {

    @Mock
    private DocumentService documentService;

    @Mock
    private FolderService folderService;

    @Mock
    private CmisProperties cmisProperties;

    @Mock
    private CmisProperties.Repository repository;

    @Mock
    private CmisProperties.Capabilities capabilities;

    @InjectMocks
    private CmisServiceImpl cmisService;

    private final String REPO_ID = "test-repo";
    private final String REPO_NAME = "Test Repository";
    private final String REPO_DESC = "Test Repository Description";
    private final String VENDOR_NAME = "Test Vendor";
    private final String PRODUCT_NAME = "Test Product";
    private final String PRODUCT_VERSION = "1.0.0";
    private final String CMIS_VERSION = "1.1";

    @BeforeEach
    void setUp() {
        // Configure mocks with lenient stubbing to avoid UnnecessaryStubbingException
        lenient().when(cmisProperties.getRepository()).thenReturn(repository);
        lenient().when(cmisProperties.getCapabilities()).thenReturn(capabilities);

        // Repository properties
        lenient().when(repository.getId()).thenReturn(REPO_ID);
        lenient().when(repository.getName()).thenReturn(REPO_NAME);
        lenient().when(repository.getDescription()).thenReturn(REPO_DESC);
        lenient().when(repository.getVendorName()).thenReturn(VENDOR_NAME);
        lenient().when(repository.getProductName()).thenReturn(PRODUCT_NAME);
        lenient().when(repository.getProductVersion()).thenReturn(PRODUCT_VERSION);
        lenient().when(repository.getCmisVersion()).thenReturn(CMIS_VERSION);

        // Capabilities
        lenient().when(capabilities.getContentStreamUpdatability()).thenReturn(true);
        lenient().when(capabilities.getChangesCapability()).thenReturn(true);
        lenient().when(capabilities.getRenditionsCapability()).thenReturn(false);
        lenient().when(capabilities.getGetDescendantsSupported()).thenReturn(true);
        lenient().when(capabilities.getGetFolderTreeSupported()).thenReturn(true);
        lenient().when(capabilities.getMultifilingSupported()).thenReturn(false);
        lenient().when(capabilities.getUnfilingSupported()).thenReturn(false);
        lenient().when(capabilities.getVersionSpecificFilingSupported()).thenReturn(false);
        lenient().when(capabilities.getPwcUpdatableSupported()).thenReturn(true);
        lenient().when(capabilities.getPwcSearchableSupported()).thenReturn(true);
        lenient().when(capabilities.getAllVersionsSearchableSupported()).thenReturn(true);
        lenient().when(capabilities.getQuerySupported()).thenReturn(true);
        lenient().when(capabilities.getJoinSupported()).thenReturn(false);
        lenient().when(capabilities.getAclSupported()).thenReturn(true);

        // Default behavior for folderService.getById to avoid NullPointerException
        lenient().when(folderService.getById(anyLong())).thenReturn(Mono.empty());
    }

    @Test
    void getRepositories_shouldReturnRepositoryFromConfiguration() {
        // Test
        Flux<CmisRepositoryDTO> result = cmisService.getRepositories();

        // Verify
        StepVerifier.create(result)
                .expectNextMatches(repo -> 
                    repo.getId().equals(REPO_ID) &&
                    repo.getName().equals(REPO_NAME) &&
                    repo.getDescription().equals(REPO_DESC) &&
                    repo.getVendorName().equals(VENDOR_NAME) &&
                    repo.getProductName().equals(PRODUCT_NAME) &&
                    repo.getProductVersion().equals(PRODUCT_VERSION) &&
                    repo.getCmisVersionSupported().equals(CMIS_VERSION)
                )
                .verifyComplete();
    }

    @Test
    void getRepository_withValidId_shouldReturnRepository() {
        // Test
        Mono<CmisRepositoryDTO> result = cmisService.getRepository(REPO_ID);

        // Verify
        StepVerifier.create(result)
                .expectNextMatches(repo -> repo.getId().equals(REPO_ID))
                .verifyComplete();
    }

    @Test
    void getRepository_withInvalidId_shouldReturnEmpty() {
        // Test
        Mono<CmisRepositoryDTO> result = cmisService.getRepository("invalid-id");

        // Verify
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void getRootFolder_withValidId_shouldReturnRootFolder() {
        // Setup
        FolderDTO rootFolder = FolderDTO.builder()
                .id(1L)
                .name("Root")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(folderService.getById(1L)).thenReturn(Mono.just(rootFolder));

        // Test
        Mono<CmisObjectDTO> result = cmisService.getRootFolder(REPO_ID);

        // Verify
        StepVerifier.create(result)
                .expectNextMatches(obj -> 
                    obj.getId().equals("1") &&
                    obj.getName().equals("Root") &&
                    obj.getBaseTypeId().equals("cmis:folder")
                )
                .verifyComplete();
    }

    @Test
    void getObject_withValidDocumentId_shouldReturnDocument() {
        // Setup
        DocumentDTO document = DocumentDTO.builder()
                .id(123L)
                .name("Test Document")
                .folderId(1L)
                .mimeType("text/plain")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(documentService.getById(123L)).thenReturn(Mono.just(document));

        // Test
        Mono<CmisObjectDTO> result = cmisService.getObject(REPO_ID, "123");

        // Verify
        StepVerifier.create(result)
                .expectNextMatches(obj -> 
                    obj.getId().equals("123") &&
                    obj.getName().equals("Test Document") &&
                    obj.getBaseTypeId().equals("cmis:document")
                )
                .verifyComplete();
    }

    @Test
    void getObject_withInvalidRepositoryId_shouldReturnEmpty() {
        // Test
        Mono<CmisObjectDTO> result = cmisService.getObject("invalid-repo", "123");

        // Verify
        StepVerifier.create(result)
                .verifyComplete();
    }
}
