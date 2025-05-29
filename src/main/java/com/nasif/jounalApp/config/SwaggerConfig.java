package com.nasif.jounalApp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                new Info().title("Journal Application APIs")
                        .version("1.0")
                        .description("by Nasif Khan\n" +
                                "Order for easy use of the APIs:\n\n" +
                                "1. Public API\n" +
                                "2. User API\n" +
                                "3. Journal API\n" +
                                "4. Admin API\n\n" +
                                "Only public API can be accessed without authentication.\n" +
                                "Other APIs requires authentication via JWT token. You can get the token by logging in using the '/public/login' endpoint. \n\n" +
                                "Admin API can be accessed only for user with \"ADMIN\" role. Not in free trial.\n\n" +
                                "Note: We are currently working on re-ordering of the APIs"))
                .servers(
                        List.of(
                                new Server().url("http://localhost:8080").description("Local"),
                                new Server().url("http://localhost:8081").description("Production")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes(
                        "bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                ));
    }
}
