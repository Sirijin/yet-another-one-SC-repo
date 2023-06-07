package com.example.demo.security;

import com.example.demo.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class TokenInfo {

    private Long id;
    private String username;
    private Set<Role> roles;
    private Date startingDate;
    private Date expiresAt;


}
