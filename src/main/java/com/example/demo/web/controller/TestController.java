package com.example.demo.web.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.type.RoleSet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/admin")
    public String admin() {
        return "test admin";
    }

    @GetMapping("/user")
    public String user() {
        return "test user";
    }

    @PostMapping()
    public ResponseEntity<?> checkRoles(@RequestBody UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername()).orElse(new User());

        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Set<Role> roles = new HashSet<>();

        for (RoleSet name : userDto.getRoles()) {
            Role role = roleRepository.findByName(name);
            roles.add(role);
        }
        user.setRoles(roles);
        return ResponseEntity.ok(user);
    }
}
