# CMIS Configuration

This package contains configuration classes for the CMIS (Content Management Interoperability Services) implementation.

## CmisProperties

The `CmisProperties` class is a configuration properties class that maps to the `cmis` section in the application.yaml file. It provides a type-safe way to access the CMIS configuration properties.

### Usage

The configuration is automatically loaded from the application.yaml file and can be injected into any Spring component:

```java
@Service
public class MyService {
    private final CmisProperties cmisProperties;

    public MyService(CmisProperties cmisProperties) {
        this.cmisProperties = cmisProperties;
    }

    public void doSomething() {
        String repositoryId = cmisProperties.getRepository().getId();
        // ...
    }
}
```

### Configuration Structure

The configuration is structured as follows:

```yaml
cmis:
  repository:
    id: ecm-repository
    name: ECM Repository
    description: Enterprise Content Management Repository
    vendor-name: Firefly
    product-name: Enterprise Content Management
    product-version: 1.0.0
    cmis-version: 1.1
  capabilities:
    content-stream-updatability: true
    changes-capability: true
    renditions-capability: false
    get-descendants-supported: true
    get-folder-tree-supported: true
    multifiling-supported: false
    unfiling-supported: false
    version-specific-filing-supported: false
    pwc-updatable-supported: true
    pwc-searchable-supported: true
    all-versions-searchable-supported: true
    query-supported: true
    join-supported: false
    acl-supported: true
```

## Future Improvements

1. Add validation for the configuration properties to ensure that required properties are provided and have valid values.
2. Add support for multiple repositories by making the `repository` property a list.
3. Add support for more CMIS capabilities as needed.
4. Consider adding a configuration for the root folder ID, which is currently hardcoded as "1" in the CmisServiceImpl.