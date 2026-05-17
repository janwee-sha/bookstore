package com.janwee.bookstore.authorization.apidoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class OpenApiConfiguration {
    private static final String BEARER_AUTH_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI authorizationOpenApi() {
        SecurityScheme bearerAuthScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH_SCHEME, bearerAuthScheme));
    }

    @Bean
    public OperationCustomizer bearerAuthOperationCustomizer() {
        return (operation, handlerMethod) -> {
            if (requiresAuthorization(handlerMethod)) {
                operation.addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH_SCHEME));
            }
            return operation;
        };
    }

    private boolean requiresAuthorization(HandlerMethod handlerMethod) {
        return handlerMethod.hasMethodAnnotation(PreAuthorize.class)
                || handlerMethod.getBeanType().isAnnotationPresent(PreAuthorize.class);
    }
}
