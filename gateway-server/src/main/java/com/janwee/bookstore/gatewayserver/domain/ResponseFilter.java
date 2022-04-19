package com.janwee.bookstore.gatewayserver.domain;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//一个后置过滤器实例，确保从Zuul种流出的每个请求都具有相关的关联ID，用于跟踪一个调用经过一系列微服务调用发生的事件链
@Component
public class ResponseFilter extends ZuulFilter {
    private static final Logger logger = LoggerFactory.getLogger(ResponseFilter.class);

    private static final int FILTER_ORDER = 1;

    private static final boolean SHOULD_FILTER = true;

    private static final FilterType FILTER_TYPE = FilterType.POST;

    private final Filters filters;

    @Autowired
    public ResponseFilter(Filters filters) {
        this.filters = filters;
    }

    @Override
    public String filterType() {
        return FILTER_TYPE.code();
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();

        logger.info("------------------Adding the correlation id to the outbound headers: {}------------------", filters.correlationId());
        ctx.getResponse().addHeader(Filters.CORRELATION_ID, filters.correlationId());

        logger.info("------------------Completing outgoing request for {}------------------",
                ctx.getRequest().getRequestURI());

        return null;
    }
}
