package com.firefly.commons.ecm.core.extensions.config;

import com.firefly.commons.ecm.interfaces.extensions.DocumentStorageExtension;
import com.firefly.core.plugin.api.PluginManager;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DocumentStoragePluginExtensionPointConfig {

    @Autowired
    private PluginManager pluginManager;

    @PostConstruct
    public void registerStorageProviderExtension() {
        log.info("Registering storage provider extension point");
        pluginManager.getExtensionRegistry()
                .registerExtensionPoint(
                        "com.firefly.commons.ecm.interfaces.extensions.storage-provider",
                        DocumentStorageExtension.class
                )
                .subscribe();
        log.info("Storage provider extension point registered successfully");
    }
}
