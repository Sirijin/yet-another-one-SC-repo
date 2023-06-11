package com.example.demo.security;

import com.example.demo.exception.JwtAuthenticationException;
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

    private final JwtUtil jwtUtil;

    private final static String LOGIN_URI = "/auth/login";
    private final static String REGISTER_URI = "/auth/register";
    private final static String TEST_URI = "/test";

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().equals(LOGIN_URI) | request.getRequestURI().equals(REGISTER_URI) | request.getRequestURI().equals(TEST_URI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtUtil.getTokenFromHeader(request);

        try {
            if (token != null && jwtUtil.validateToken(token)) {
                Authentication authentication = jwtUtil.getAuthentication(token);

                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (JwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
            throw new JwtAuthenticationException("Token is expired or invalid");
        }

        filterChain.doFilter(request, response);
    }
}
