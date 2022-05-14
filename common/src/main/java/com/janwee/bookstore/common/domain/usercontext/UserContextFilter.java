package com.janwee.bookstore.common.domain.usercontext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class UserContextFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(UserContextFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest castedRequest = (HttpServletRequest) request;

        UserContextHolder.getContext().setCorrelationId(castedRequest.getHeader(UserContext.CORRELATION_ID));
        UserContextHolder.getContext().setUserId(castedRequest.getHeader(UserContext.USER_ID));
        UserContextHolder.getContext().setAuthToken(castedRequest.getHeader(UserContext.AUTH_TOKEN));
        UserContextHolder.getContext().setOrgId(castedRequest.getHeader(UserContext.ORG_ID));

        logger.debug("License Service Incoming Correlation id: {}", UserContextHolder.getContext().getCorrelationId());

        chain.doFilter(castedRequest, response);
    }
}
