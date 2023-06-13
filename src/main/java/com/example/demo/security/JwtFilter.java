package com.example.demo.security;

import com.example.demo.exception.JwtAuthenticationException;
import com.example.demo.type.AllowedUri;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().equals(AllowedUri.LOGIN.getUri())
                || request.getRequestURI().equals(AllowedUri.REGISTER.getUri())
                || request.getRequestURI().equals(AllowedUri.SWAGGER.getUri())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtService.getTokenFromHeader(request);

        try {
            if (jwtService.validateToken(token)) {
                Authentication authentication = jwtService.getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (JwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
            throw e;
        }

        filterChain.doFilter(request, response);
    }
}
