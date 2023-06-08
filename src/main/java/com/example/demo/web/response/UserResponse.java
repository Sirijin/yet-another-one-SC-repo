package com.example.demo.web.response;

import com.example.demo.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserResponse {
    private List<UserDto> users;
    private long total;
}
