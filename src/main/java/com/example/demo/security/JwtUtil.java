package com.example.demo.security;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.JwtAuthenticationException;
import com.example.demo.repository.RoleRepository;
import com.example.demo.type.RoleSet;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationInMillis}")
    private Long expirationInMillis;

    private static final String HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final RoleRepository roleRepository;

    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(user.getId()));
        claims.put("roles", user.getRoles());
        claims.put("username", user.getUsername());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationInMillis))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts
                    .parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("Token is not valid");
        }
    }

    public Authentication getAuthentication(String token) {
        TokenInfo tokenInfo = getTokenInfo(token);
        Principal principal = new CustomPrincipal(tokenInfo.getId(), tokenInfo.getUsername());
        return new UsernamePasswordAuthenticationToken(principal, "", tokenInfo.getRoles());
    }

    public String getTokenFromHeader(HttpServletRequest request) {
        return request.getHeader(HEADER).substring(BEARER_PREFIX.length());
    }

    private TokenInfo getTokenInfo(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<Map<String, Object>> rolesList = claims.get("roles", List.class);

        Set<Role> roles = rolesList.stream()
                .map(roleMap -> (String) roleMap.get("name")) // Извлекаем значения поля 'name'
                .map(RoleSet::valueOf) // Преобразуем строковое значение в соответствующий элемент enum RoleSet
                .map(roleRepository::findByName) // Ищем роль по значению enum в репозитории
                .filter(Objects::nonNull) // Отфильтровываем найденные роли, исключая null
                .collect(Collectors.toSet()); // Собираем результаты в Set<Role>


        return TokenInfo
                .builder()
                .id(Long.valueOf(claims.getSubject()))
                .username(claims.get("username", String.class))
                .startingDate(claims.getIssuedAt())
                .expiresAt(claims.getExpiration())
                .roles(roles)
                .build();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
