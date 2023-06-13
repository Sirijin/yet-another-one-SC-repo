package com.example.demo.web.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import com.example.demo.web.request.AuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.web.controller.errorHandler.ProcessError.processError;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            return ResponseEntity.ok(userService.loginUser(request));
        } catch (Exception e) {
            return processError("failed to login user", e);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        try {
            return ResponseEntity.ok(userService.registerUser(userDto));
        } catch (Exception e) {
            return processError("failed to register user", e);
        }
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        try {
            return ResponseEntity.ok(userService.getUserInfo(authentication));
        } catch (Exception e) {
            return processError("failed to get user info", e);
        }
    }
}
