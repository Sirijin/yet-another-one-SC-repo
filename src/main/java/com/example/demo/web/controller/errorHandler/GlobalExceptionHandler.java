package com.example.demo.web.controller.errorHandler;

import com.example.demo.dto.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        logger.error("Unable to process with error - {}", ex.getMessage());
        logger.debug("Class - {}, message - {}, cause - {},  stack trace - {}", ex.getClass(), ex.getMessage(), ex.getCause(), ex.getStackTrace());
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(new ErrorDto("File size exceeded the maximum limit", ex.getMessage(), Instant.now()));
    }
}
