package com.janwee.bookstore.foundation.autoconfigure;

import com.janwee.bookstore.foundation.exception.GlobalExceptionHandler;
import com.janwee.bookstore.foundation.logging.LoggingAspect;
import com.janwee.bookstore.foundation.security.JwtAuthorityMappingConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.web.servlet.LocaleResolver;

import static org.assertj.core.api.Assertions.assertThat;

class FoundationAutoConfigurationTest {
    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(FoundationAutoConfiguration.class));

    @Test
    void registersFoundationWebInfrastructure() {
        contextRunner.run(context -> assertThat(context)
                .hasSingleBean(GlobalExceptionHandler.class)
                .hasSingleBean(LocaleResolver.class)
                .hasSingleBean(LoggingAspect.class)
                .hasSingleBean(JwtAuthorityMappingConfiguration.class));
    }
}
