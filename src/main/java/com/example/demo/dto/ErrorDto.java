package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ErrorDto {

    private String message;
    private String errorMessage;
    private Instant timestamp;
}
