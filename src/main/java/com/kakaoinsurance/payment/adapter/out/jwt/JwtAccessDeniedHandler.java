package com.kakaoinsurance.payment.adapter.out.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    static String forbiddenResult = "{\"success\":false,\"response\":null,\"message\":\"Forbidden\",\"status\":403}";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws
        IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setHeader("content-type", "application/json");
        response.getWriter().write(forbiddenResult);
        response.getWriter().flush();
        response.getWriter().close();
    }

}