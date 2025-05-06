package com.catalis.commons.ecm.core.extensions;

import com.catalis.commons.ecm.interfaces.extensions.SignatureProviderExtension;
import com.catalis.core.plugin.api.PluginManager;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SignaturePluginExtensionPointConfig {

    @Autowired
    private PluginManager pluginManager;

    @PostConstruct
    public void registerSignatureProviderExtension() {
        pluginManager.getExtensionRegistry()
                .registerExtensionPoint(
                        "com.catalis.commons.ecm.interfaces.extensions.signature-provider",
                        SignatureProviderExtension.class
                )
                .subscribe();
    }
}