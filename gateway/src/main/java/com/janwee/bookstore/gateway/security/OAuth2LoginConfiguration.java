package com.janwee.bookstore.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class OAuth2LoginConfiguration {
    @Bean
    @Order(2)
    public SecurityFilterChain oauth2LoginSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/", "/login/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(login ->
                        login.redirectionEndpoint(redirection ->
                                        redirection.baseUri("/authorized"))
                                .defaultSuccessUrl("/", true));
        return http.build();
    }
}
