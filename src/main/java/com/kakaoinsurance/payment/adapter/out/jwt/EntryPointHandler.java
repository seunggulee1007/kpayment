package com.kakaoinsurance.payment.adapter.out.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EntryPointHandler implements AuthenticationEntryPoint {

    static String response401 = "{\"success\":false,\"response\":null,\"message\":\"Unauthorized\",\"status\":401}";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws
        IOException,
        ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("content-type", "application/json");
        response.getWriter().write(response401);
        response.getWriter().flush();
        response.getWriter().close();
    }

}