package com.firefly.commons.ecm.web;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication(
        scanBasePackages = {
                "com.firefly.core.ecm",      // Scan lib-ecm-core components including EcmPortProvider
                "com.firefly.commons.ecm",
                "com.firefly.common.web"     // Scan common web library configurations
        }
)
@EnableWebFlux
@EnableR2dbcRepositories(
        basePackages = "com.firefly.commons.ecm.models.repositories"
)
@EnableR2dbcAuditing
@ConfigurationPropertiesScan
@OpenAPIDefinition(
        info = @Info(
                title = "${spring.application.name}",
                version = "${spring.application.version}",
                description = "${spring.application.description}",
                contact = @Contact(
                        name = "${spring.application.team.name}",
                        email = "${spring.application.team.email}"
                )
        ),
        servers = {
                @Server(
                        url = "https://api.getfirefly.io/ecm",
                        description = "Production Environment"
                ),
                @Server(
                        url = "/",
                        description = "Local Development Environment"
                )
        }
)
public class EnterpriseContentManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnterpriseContentManagementApplication.class, args);
    }
}