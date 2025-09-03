package com.janwee.bookstore.authorization.foundation.security;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomizedExpiredSessionStrategy implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "This session has been expired (possibly due to multiple concurrent logins being attempted as the same user).");
        ResponseEntity<Object> responseEntity = ResponseEntity
                .status(HttpStatus.UNAUTHORIZED.value())
                .body(result);

        HttpServletResponse response = event.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(JSON.toJSONString(responseEntity));
    }
}
