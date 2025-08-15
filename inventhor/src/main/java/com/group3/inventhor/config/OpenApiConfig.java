package com.group3.inventhor.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tatiana Fløisbonn
 *
 * This configuration class sets up OpenAPI documentation for the Inventhor API.
 * It defines the API title, version, and security scheme for JWT authentication.
 * It uses the OpenAPI 3.0 specification to document the API endpoints.
 * The security scheme is defined as a bearer token, which is commonly used for APIs that require authentication.
 * The OpenAPI documentation can be accessed via the Swagger UI.
 * The API documentation will be available at the `/v3/api-docs` endpoint.
 * The Swagger UI can be accessed at `/swagger-ui/index.html`.
 * This configuration is essential for providing clear and interactive API documentation,
 * which helps developers understand how to use the API effectively.
 * It also facilitates testing and integration with other systems.
 *
 * @Configuration indicates that this class contains Spring configuration.
 */
@Configuration
public class OpenApiConfig {

    /**
     * @Bean annotation indicates that this method produces a bean to be managed by the Spring container.
     * @return An OpenAPI instance configured with API information and security settings.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info().title("Inventhor API").version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}