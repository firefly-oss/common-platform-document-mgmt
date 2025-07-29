package com.catalis.commons.ecm.storage.grpc.config;

import net.devh.boot.grpc.server.security.authentication.BasicGrpcAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for gRPC server.
 * 
 * Note: For gRPC-Web support, it is recommended to use a proxy like Envoy.
 * See: https://grpc.io/docs/platforms/web/basics/
 */
@Configuration
public class GrpcConfig {

    @Value("${grpc.server.security.enabled:false}")
    private boolean securityEnabled;

    /**
     * Configures the gRPC server.
     *
     * @return the gRPC server configurer
     */
    @Bean
    public GrpcServerConfigurer grpcServerConfigurer() {
        return serverBuilder -> {
            serverBuilder.maxInboundMessageSize(10 * 1024 * 1024); // 10MB
        };
    }

    /**
     * Configures gRPC authentication if security is enabled.
     *
     * @return the gRPC authentication reader
     */
    @Bean
    public GrpcAuthenticationReader grpcAuthenticationReader() {
        if (securityEnabled) {
            return new BasicGrpcAuthenticationReader();
        }
        return null;
    }
}