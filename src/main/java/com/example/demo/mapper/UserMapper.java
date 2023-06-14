package com.example.demo.mapper;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.RoleService;
import com.example.demo.type.RoleSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final RoleService roleService;

    public UserDto map(User user) {
        Set<RoleSet> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        return UserDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(roles)
                .build();
    }

    public User map(UserDto userDto, String password) {
        Set<Role> roles;
        if (userDto.getRoles() == null) {
            roles = new HashSet<>();
            roles.add(roleService.findByName(RoleSet.USER_ROLE));
        } else {
            roles = userDto.getRoles().stream().map(roleService::findByName).collect(Collectors.toSet());
        }
        return User.builder()
                .username(userDto.getUsername())
                .password(password)
                .roles(roles)
                .build();
    }
}
