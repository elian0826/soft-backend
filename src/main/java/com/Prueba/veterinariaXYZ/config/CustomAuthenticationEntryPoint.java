package com.Prueba.veterinariaXYZ.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        String acceptHeader = request.getHeader("Accept");
        String uri = request.getRequestURI();
        boolean isAjax = acceptHeader != null && acceptHeader.contains("application/json");
        boolean isSwagger = uri.contains("/swagger-ui") || uri.contains("/v3/api-docs");

        if (isAjax && !isSwagger) {
            // No enviar WWW-Authenticate, solo 401 y un mensaje JSON
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Credenciales inválidas\"}");
        } else {
            // Comportamiento por defecto: enviar WWW-Authenticate (para Swagger y otros)
            response.setHeader("WWW-Authenticate", "Basic realm=\"Realm\"");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication required.");
        }
    }
}