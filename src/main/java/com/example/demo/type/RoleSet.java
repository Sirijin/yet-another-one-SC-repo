package com.example.demo.type;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@AllArgsConstructor
public enum RoleSet {
    USER_ROLE("USER_ROLE"),
    ADMIN_ROLE("ADMIN_ROLE");

    private final String name;

    public SimpleGrantedAuthority getAuthority() {
        return new SimpleGrantedAuthority(name);
    }
}
