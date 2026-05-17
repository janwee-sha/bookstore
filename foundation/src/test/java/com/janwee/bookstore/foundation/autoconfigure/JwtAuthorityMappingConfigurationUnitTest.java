package com.janwee.bookstore.foundation.autoconfigure;

import com.janwee.bookstore.foundation.security.JwtAuthorityMappingConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtAuthorityMappingConfigurationUnitTest {

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
}
