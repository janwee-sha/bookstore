package com.janwee.bookstore.foundation.autoconfigure;

import com.janwee.bookstore.foundation.security.JwtAuthorityMappingConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtAuthorityMappingConfigurationUnitTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(JwtAuthorityMappingConfiguration.class));

    @Test
    void shouldMapJwtScopesToDomainAuthoritiesWithoutScopePrefix() {
        JwtAuthenticationConverter converter = new JwtAuthorityMappingConfiguration()
                .jwtAuthenticationConverter();
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("scope", List.of("user:read", "user:write"))
                .build();

        List<String> authorities = converter.convert(jwt).getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertEquals(List.of("user:read", "user:write"), authorities);
    }

    @Test
    void shouldRegisterJwtAuthenticationConverterWhenResourceServerClassesExist() {
        contextRunner.run(context -> assertThat(context)
                .hasSingleBean(JwtAuthenticationConverter.class));
    }

    @Test
    void shouldNotReplaceCustomJwtAuthenticationConverter() {
        contextRunner
                .withUserConfiguration(CustomJwtAuthenticationConverterConfiguration.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(JwtAuthenticationConverter.class)
                        .getBean(JwtAuthenticationConverter.class)
                        .isSameAs(context.getBean("customJwtAuthenticationConverter")));
    }

    @Configuration
    static class CustomJwtAuthenticationConverterConfiguration {

        @Bean
        JwtAuthenticationConverter customJwtAuthenticationConverter() {
            return new JwtAuthenticationConverter();
        }
    }
}