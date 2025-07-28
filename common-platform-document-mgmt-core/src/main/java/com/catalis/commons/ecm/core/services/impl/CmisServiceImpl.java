package com.catalis.commons.ecm.core.services.impl;

import com.catalis.commons.ecm.core.config.CmisProperties;
import com.catalis.commons.ecm.core.services.DocumentService;
import com.catalis.commons.ecm.core.services.FolderService;
import com.catalis.commons.ecm.interfaces.dtos.DocumentDTO;
import com.catalis.commons.ecm.interfaces.dtos.FolderDTO;
import com.catalis.commons.ecm.interfaces.dtos.cmis.CmisObjectDTO;
import com.catalis.commons.ecm.interfaces.dtos.cmis.CmisRepositoryCapabilitiesDTO;
import com.catalis.commons.ecm.interfaces.dtos.cmis.CmisRepositoryDTO;
import com.catalis.commons.ecm.core.services.CmisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the CMIS service that adapts the ECM document and folder services to CMIS.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CmisServiceImpl implements CmisService {

    private final DocumentService documentService;
    private final FolderService folderService;
    private final CmisProperties cmisProperties;

    @Override
    public Flux<CmisRepositoryDTO> getRepositories() {
        return Mono.just(createRepository()).flux();
    }

    @Override
    public Mono<CmisRepositoryDTO> getRepository(String repositoryId) {
        if (cmisProperties.getRepository().getId().equals(repositoryId)) {
            return Mono.just(createRepository());
        }
        return Mono.empty();
    }

    @Override
    public Mono<CmisObjectDTO> getRootFolder(String repositoryId) {
        if (!cmisProperties.getRepository().getId().equals(repositoryId)) {
            return Mono.empty();
        }

        // Assuming the root folder has ID 1
        return folderService.getById(1L)
                .map(this::convertFolderToCmisObject);
    }

    @Override
    public Mono<CmisObjectDTO> getObject(String repositoryId, String objectId) {
        if (!cmisProperties.getRepository().getId().equals(repositoryId)) {
            return Mono.empty();
        }

        Long id = Long.parseLong(objectId);

        // Try to get as document first
        return documentService.getById(id)
                .map(this::convertDocumentToCmisObject)
                .switchIfEmpty(
                    // If not found as document, try as folder
                    folderService.getById(id)
                            .map(this::convertFolderToCmisObject)
                );
    }

    @Override
    public Mono<CmisObjectDTO> getObjectByPath(String repositoryId, String path) {
        if (!cmisProperties.getRepository().getId().equals(repositoryId)) {
            return Mono.empty();
        }

        // This is a simplified implementation since we don't have a getByPath method
        // In a real implementation, we would need to parse the path and find the object
        log.info("Getting object by path: {}", path);
        return Mono.empty();
    }

    @Override
    public Flux<CmisObjectDTO> getChildren(String repositoryId, String folderId) {
        if (!cmisProperties.getRepository().getId().equals(repositoryId)) {
            return Flux.empty();
        }

        Long folderIdLong = Long.parseLong(folderId);

        // Since we don't have direct access to get children by folder ID,
        // we'll just log the request and return empty for this simplified implementation
        log.info("Getting children for folder: {}", folderIdLong);

        // In a real implementation, we would use the filter method with appropriate parameters
        // to get subfolders and documents in the folder
        return Flux.empty();
    }

    @Override
    public Mono<CmisObjectDTO> createDocument(String repositoryId, String folderId, String name, 
                                             String contentType, byte[] content, Map<String, Object> properties) {
        if (!cmisProperties.getRepository().getId().equals(repositoryId)) {
            return Mono.empty();
        }

        DocumentDTO documentDTO = DocumentDTO.builder()
                .name(name)
                .mimeType(contentType)
                .folderId(Long.parseLong(folderId))
                .build();

        // Add any additional properties from the CMIS request
        if (properties != null && properties.containsKey("description")) {
            documentDTO.setDescription(properties.get("description").toString());
        }

        return documentService.create(documentDTO)
                .map(this::convertDocumentToCmisObject);
    }

    @Override
    public Mono<CmisObjectDTO> createFolder(String repositoryId, String parentFolderId, String name, 
                                           Map<String, Object> properties) {
        if (!cmisProperties.getRepository().getId().equals(repositoryId)) {
            return Mono.empty();
        }

        FolderDTO folderDTO = FolderDTO.builder()
                .name(name)
                .parentFolderId(Long.parseLong(parentFolderId))
                .build();

        // Add any additional properties from the CMIS request
        if (properties != null && properties.containsKey("description")) {
            folderDTO.setDescription(properties.get("description").toString());
        }

        return folderService.create(folderDTO)
                .map(this::convertFolderToCmisObject);
    }

    @Override
    public Mono<CmisObjectDTO> updateProperties(String repositoryId, String objectId, Map<String, Object> properties) {
        if (!cmisProperties.getRepository().getId().equals(repositoryId)) {
            return Mono.empty();
        }

        Long id = Long.parseLong(objectId);

        // Try to update as document first
        return documentService.getById(id)
                .flatMap(doc -> {
                    if (properties.containsKey("name")) {
                        doc.setName(properties.get("name").toString());
                    }
                    if (properties.containsKey("description")) {
                        doc.setDescription(properties.get("description").toString());
                    }

                    return documentService.update(doc)
                            .map(this::convertDocumentToCmisObject);
                })
                .switchIfEmpty(
                    // If not found as document, try as folder
                    folderService.getById(id)
                            .flatMap(folder -> {
                                if (properties.containsKey("name")) {
                                    folder.setName(properties.get("name").toString());
                                }
                                if (properties.containsKey("description")) {
                                    folder.setDescription(properties.get("description").toString());
                                }

                                return folderService.update(folder)
                                        .map(this::convertFolderToCmisObject);
                            })
                );
    }

    @Override
    public Mono<Void> deleteObject(String repositoryId, String objectId, boolean allVersions) {
        if (!cmisProperties.getRepository().getId().equals(repositoryId)) {
            return Mono.empty();
        }

        Long id = Long.parseLong(objectId);

        // Try to delete as document first
        return documentService.getById(id)
                .flatMap(doc -> documentService.delete(id))
                .switchIfEmpty(
                    // If not found as document, try as folder
                    folderService.getById(id)
                            .flatMap(folder -> folderService.delete(id))
                );
    }

    @Override
    public Mono<byte[]> getContentStream(String repositoryId, String objectId) {
        if (!cmisProperties.getRepository().getId().equals(repositoryId)) {
            return Mono.empty();
        }

        // This is a simplified implementation since we don't have a getContent method
        // In a real implementation, we would need to get the content from a storage service
        log.info("Getting content stream for object: {}", objectId);
        return Mono.empty();
    }

    @Override
    public Mono<CmisObjectDTO> setContentStream(String repositoryId, String objectId, String contentType, 
                                               byte[] content, boolean overwrite) {
        if (!cmisProperties.getRepository().getId().equals(repositoryId)) {
            return Mono.empty();
        }

        // This is a simplified implementation since we don't have a setContent method
        // In a real implementation, we would need to set the content in a storage service
        log.info("Setting content stream for object: {}", objectId);
        return Mono.empty();
    }

    @Override
    public Flux<CmisObjectDTO> query(String repositoryId, String statement, boolean searchAllVersions) {
        if (!cmisProperties.getRepository().getId().equals(repositoryId)) {
            return Flux.empty();
        }

        // This is a simplified implementation since we don't have a query method
        // In a real implementation, we would need to parse the query and execute it
        log.info("Executing query: {}", statement);
        return Flux.empty();
    }

    @Override
    public Mono<CmisObjectDTO> checkOut(String repositoryId, String objectId) {
        if (!cmisProperties.getRepository().getId().equals(repositoryId)) {
            return Mono.empty();
        }

        // This is a simplified implementation since we don't have a checkOut method
        // In a real implementation, we would need to check out the document
        log.info("Checking out object: {}", objectId);
        return Mono.empty();
    }

    @Override
    public Mono<Void> cancelCheckOut(String repositoryId, String objectId) {
        if (!cmisProperties.getRepository().getId().equals(repositoryId)) {
            return Mono.empty();
        }

        // This is a simplified implementation since we don't have a cancelCheckOut method
        // In a real implementation, we would need to cancel the check out
        log.info("Canceling check out for object: {}", objectId);
        return Mono.empty();
    }

    @Override
    public Mono<CmisObjectDTO> checkIn(String repositoryId, String objectId, boolean major, 
                                      Map<String, Object> properties, String contentType, 
                                      byte[] content, String comment) {
        if (!cmisProperties.getRepository().getId().equals(repositoryId)) {
            return Mono.empty();
        }

        // This is a simplified implementation since we don't have a checkIn method
        // In a real implementation, we would need to check in the document
        log.info("Checking in object: {}", objectId);
        return Mono.empty();
    }

    @Override
    public Flux<CmisObjectDTO> getAllVersions(String repositoryId, String objectId) {
        if (!cmisProperties.getRepository().getId().equals(repositoryId)) {
            return Flux.empty();
        }

        // This is a simplified implementation since we don't have a getAllVersions method
        // In a real implementation, we would need to get all versions of the document
        log.info("Getting all versions for object: {}", objectId);
        return Flux.empty();
    }

    // Helper methods

    private CmisRepositoryDTO createRepository() {
        CmisRepositoryCapabilitiesDTO capabilities = CmisRepositoryCapabilitiesDTO.builder()
                .contentStreamUpdatability(cmisProperties.getCapabilities().getContentStreamUpdatability())
                .changesCapability(cmisProperties.getCapabilities().getChangesCapability())
                .renditionsCapability(cmisProperties.getCapabilities().getRenditionsCapability())
                .getDescendantsSupported(cmisProperties.getCapabilities().getGetDescendantsSupported())
                .getFolderTreeSupported(cmisProperties.getCapabilities().getGetFolderTreeSupported())
                .multifilingSupported(cmisProperties.getCapabilities().getMultifilingSupported())
                .unfilingSupported(cmisProperties.getCapabilities().getUnfilingSupported())
                .versionSpecificFilingSupported(cmisProperties.getCapabilities().getVersionSpecificFilingSupported())
                .pwcUpdatableSupported(cmisProperties.getCapabilities().getPwcUpdatableSupported())
                .pwcSearchableSupported(cmisProperties.getCapabilities().getPwcSearchableSupported())
                .allVersionsSearchableSupported(cmisProperties.getCapabilities().getAllVersionsSearchableSupported())
                .querySupported(cmisProperties.getCapabilities().getQuerySupported())
                .joinSupported(cmisProperties.getCapabilities().getJoinSupported())
                .aclSupported(cmisProperties.getCapabilities().getAclSupported())
                .build();

        return CmisRepositoryDTO.builder()
                .id(cmisProperties.getRepository().getId())
                .name(cmisProperties.getRepository().getName())
                .description(cmisProperties.getRepository().getDescription())
                .vendorName(cmisProperties.getRepository().getVendorName())
                .productName(cmisProperties.getRepository().getProductName())
                .productVersion(cmisProperties.getRepository().getProductVersion())
                .cmisVersionSupported(cmisProperties.getRepository().getCmisVersion())
                .capabilities(capabilities)
                .rootFolderId("1") // Assuming the root folder has ID 1
                .build();
    }

    private CmisObjectDTO convertDocumentToCmisObject(DocumentDTO document) {
        Map<String, Object> properties = new HashMap<>();

        return CmisObjectDTO.builder()
                .id(document.getId().toString())
                .name(document.getName())
                .baseTypeId("cmis:document")
                .objectTypeId("cmis:document")
                .creationDate(document.getCreatedAt())
                .lastModificationDate(document.getUpdatedAt())
                .createdBy(document.getCreatedBy())
                .lastModifiedBy(document.getUpdatedBy())
                .parentId(document.getFolderId().toString())
                .contentStreamFileName(document.getName())
                .contentStreamMimeType(document.getMimeType())
                .properties(properties)
                .build();
    }

    private CmisObjectDTO convertFolderToCmisObject(FolderDTO folder) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("description", folder.getDescription());

        return CmisObjectDTO.builder()
                .id(folder.getId().toString())
                .name(folder.getName())
                .baseTypeId("cmis:folder")
                .objectTypeId("cmis:folder")
                .creationDate(folder.getCreatedAt())
                .lastModificationDate(folder.getUpdatedAt())
                .createdBy(folder.getCreatedBy())
                .lastModifiedBy(folder.getUpdatedBy())
                .parentId(folder.getParentFolderId() != null ? folder.getParentFolderId().toString() : null)
                .properties(properties)
                .build();
    }
}