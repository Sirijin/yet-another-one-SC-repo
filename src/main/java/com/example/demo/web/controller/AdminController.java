package com.example.demo.web.controller;

import com.example.demo.dto.ErrorDto;
import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import com.example.demo.web.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.web.controller.errorHandler.ProcessError.processError;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {


    private final UserService userService;

    @Operation(summary = "Get the list of User", description = "Get the list of User", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
    })
    @GetMapping("/users")
    public ResponseEntity<?> getUserList() {
        try {
            return ResponseEntity.ok(userService.getUserList());
        } catch (Exception e) {
            return processError("failed to get all users", e);
        }
    }

    @Operation(summary = "Get the User by id", description = "Get the User by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable @NotNull @Positive(message = "User ID must be positive") Long id) {
        try {
            return ResponseEntity.ok(userService.getUser(id));
        } catch (Exception e) {
            return processError("failed to get user with id - " + id, e);
        }
    }

    @Operation(summary = "Save the User", description = "Save the User", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
    })
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        try {
            return ResponseEntity.ok(userService.registerUser(userDto));
        } catch (Exception e) {
            return processError("failed to create user", e);
        }
    }

    @Operation(summary = "Update the User", description = "Update the User", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
    })
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable @NotNull @Positive(message = "User ID must be positive") Long id,
                                        @RequestBody UserDto userDto) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, userDto));
        } catch (Exception e) {
            return processError("failed to update user with id - " + id, e);
        }
    }

    @Operation(summary = "Delete the User", description = "Delete the User", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
    })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable @NotNull @Positive(message = "User ID must be positive") Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return processError("failed to delete user with id - " + id, e);
        }
    }

    @Operation(summary = "Update User's password", description = "Update User's password", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
    })
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
