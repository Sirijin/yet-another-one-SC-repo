package com.example.demo.web.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.web.controller.errorHandler.ProcessError.processError;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin")
public class AdminController {

    private final UserService userService;


    @Operation()
    @GetMapping("/users")
    public ResponseEntity<?> getUserList() {
        try {
            return ResponseEntity.ok(userService.getUserList());
        } catch (Exception e) {
            return processError("failed to get all users", e);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable @NotNull @Positive(message = "User ID must be positive") Long id) {
        try {
            return ResponseEntity.ok(userService.getUser(id));
        } catch (Exception e) {
            return processError("failed to get user with id - " + id, e);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        try {
            return ResponseEntity.ok(userService.registerUser(userDto));
        } catch (Exception e) {
            return processError("failed to create user", e);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable @NotNull @Positive(message = "User ID must be positive") Long id,
                                        @RequestBody UserDto userDto) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, userDto));
        } catch (Exception e) {
            return processError("failed to update user with id - " + id, e);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable @NotNull @Positive(message = "User ID must be positive") Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return processError("failed to delete user with id - " + id, e);
        }
    }

    @PatchMapping("/users/editPassword/{id}")
    public ResponseEntity<?> changeUserPassword(@PathVariable @NotNull @Positive(message = "User ID must be positive") Long id,
                                                @RequestBody String newPassword) {
        try {
            return ResponseEntity.ok(userService.updatePassword(id, newPassword));
        } catch (Exception e) {
            return processError("failed to change password for user with id - " + id, e);
        }
    }


}
