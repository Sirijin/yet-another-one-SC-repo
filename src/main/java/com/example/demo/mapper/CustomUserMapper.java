package com.example.demo.mapper;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.type.RoleSet;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomUserMapper {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserDto map(User user) {
        Set<RoleSet> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        return UserDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(roles)
                .build();
    }

    public User map(UserDto userDto) {
        Set<Role> roles = userDto.getRoles().stream().map(roleRepository::findByName).collect(Collectors.toSet());
        return User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .roles(roles)
                .build();
    }
}
