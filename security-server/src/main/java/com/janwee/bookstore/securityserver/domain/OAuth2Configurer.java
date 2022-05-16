package com.janwee.bookstore.securityserver.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

@Configuration
public class OAuth2Configurer extends AuthorizationServerConfigurerAdapter {
    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;

    @Autowired
    public OAuth2Configurer(AuthenticationManager authManager, UserDetailsService userDetailsService) {
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
    }

    //定义了哪些客户端将注册到服务
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("book-server")
                .secret("janwee")
                .authorizedGrantTypes("refresh_token", "password", "client_credentials")
                .scopes("webclient");

    }

    //告诉Spring使用Spring提供的默认验证管理器和用户详细信息服务
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authManager)
                .userDetailsService(userDetailsService);
    }
}
