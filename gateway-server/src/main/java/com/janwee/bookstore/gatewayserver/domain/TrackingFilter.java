package com.janwee.bookstore.gatewayserver.domain;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

//一个前置过滤器实例，确保从Zuul种流出的每个请求都具有相关的关联ID，用于跟踪一个调用经过一系列微服务调用发生的事件链
@Component
public class TrackingFilter extends ZuulFilter {
    private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);

    @Override
    public String filterType() {
        return FilterType.PRE_FILTER.name();
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        if (isCorrelationIdPresent()) {
            logger.debug("book-store-correlation-id found: {}.", correlationId());
        } else {
            setCorrelationId(newCorrelationId());
            logger.debug("book-store-correlation-id generated: {}.", correlationId());
        }
        return null;
    }

    private HttpServletRequest currentRequest() {
//        return RequestContext.getCurrentContext().getRequest();
        return Optional.ofNullable(
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .orElseThrow(() -> new RuntimeException("Getting request context failed"));
    }

    private String correlationId() {
        return currentRequest().getHeader("book-store-correlation-id");
    }

    private boolean isCorrelationIdPresent() {
        return correlationId() != null;
    }

    private String newCorrelationId() {
        return UUID.randomUUID().toString();
    }

    private void setCorrelationId(String correlationId) {
        currentRequest().setAttribute("book-store-correlation-id", correlationId);
    }
}
