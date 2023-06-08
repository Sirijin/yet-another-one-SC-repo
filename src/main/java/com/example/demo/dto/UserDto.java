package com.example.demo.dto;

import com.example.demo.type.RoleSet;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto {

    private String username;
    private String password;
    private Set<RoleSet> roles;
}
