package com.br.courses.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Handler para erros de autenticação JWT
 * Retorna uma resposta JSON quando a autenticação falha
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                        HttpServletResponse httpServletResponse,
                        AuthenticationException e) throws IOException, ServletException {

        log.error("Respondendo com erro de autenticação não autorizada. Mensagem: {}", e.getLocalizedMessage());

        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String json = String.format(
            "{\"error\": \"Unauthorized\", \"message\": \"Token JWT ausente ou inválido\", \"status\": 401}"
        );
        httpServletResponse.getWriter().write(json);
    }
}

