package com.firefly.commons.ecm.core.extensions.config;

import com.firefly.commons.ecm.interfaces.extensions.SignatureProviderExtension;
import com.firefly.core.plugin.api.PluginManager;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SignaturePluginExtensionPointConfig {

    @Autowired
    private PluginManager pluginManager;

    @PostConstruct
    public void registerSignatureProviderExtension() {
        log.info("Registering signature provider extension point");
        pluginManager.getExtensionRegistry()
                .registerExtensionPoint(
                        "com.firefly.commons.ecm.interfaces.extensions.signature-provider",
                        SignatureProviderExtension.class
                )
                .subscribe();
        log.info("Signature provider extension point registered successfully");
    }

}
