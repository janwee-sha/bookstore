package com.janwee.bookstore.foundation.autoconfigure;

import com.janwee.bookstore.foundation.exception.GlobalExceptionHandler;
import com.janwee.bookstore.foundation.locale.LocaleConfig;
import com.janwee.bookstore.foundation.logging.LoggingAspect;
import com.janwee.bookstore.foundation.security.JwtAuthorityMappingConfiguration;
import com.janwee.bookstore.foundation.serialization.ObjectMapperConfigurer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import({
        GlobalExceptionHandler.class,
        LocaleConfig.class,
        LoggingAspect.class,
        ObjectMapperConfigurer.class,
        JwtAuthorityMappingConfiguration.class
})
public class FoundationAutoConfiguration {
}
