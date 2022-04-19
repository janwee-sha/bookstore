package com.janwee.bookstore.gatewayserver.domain;

import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Filters {
    public static final String CORRELATION_ID = "book-store-correlation-id";
    public static final String AUTH_TOKEN = "book-store-auth-token";
    public static final String USER_ID = "book-store-user-id";
    public static final String ORG_ID = "book-store-org-id";

    public String correlationId() {
        RequestContext ctx = RequestContext.getCurrentContext();

        if (ctx.getRequest().getHeader(CORRELATION_ID) != null) {
            return ctx.getRequest().getHeader(CORRELATION_ID);
        } else {
            return ctx.getZuulRequestHeaders().get(CORRELATION_ID);
        }
    }

    public boolean isCorrelationIdPresent() {
        return correlationId() != null;
    }

    public void setNewCorrelationId() {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader(CORRELATION_ID, newCorrelationId());
    }

    private String newCorrelationId() {
        return UUID.randomUUID().toString();
    }

    public final String getOrgId() {
        RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx.getRequest().getHeader(ORG_ID) != null) {
            return ctx.getRequest().getHeader(ORG_ID);
        } else {
            return ctx.getZuulRequestHeaders().get(ORG_ID);
        }
    }

    public void setOrgId(String orgId) {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader(ORG_ID, orgId);
    }

    public final String getUserId() {
        RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx.getRequest().getHeader(USER_ID) != null) {
            return ctx.getRequest().getHeader(USER_ID);
        } else {
            return ctx.getZuulRequestHeaders().get(USER_ID);
        }
    }

    public void setUserId(String userId) {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader(USER_ID, userId);
    }

    public final String getAuthToken() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.getRequest().getHeader(AUTH_TOKEN);
    }

    public String getServiceId() {
        RequestContext ctx = RequestContext.getCurrentContext();

        //We might not have a service id if we are using a static, non-eureka route.
        if (ctx.get("serviceId") == null) return "";
        return ctx.get("serviceId").toString();
    }
}
