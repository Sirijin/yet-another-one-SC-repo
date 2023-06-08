package com.example.demo.web.controller.errorHandler;

import com.example.demo.dto.ErrorDto;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;

import java.time.Instant;
import java.util.List;

public final class ProcessError {

    private static final Logger logger = LoggerFactory.getLogger(ProcessError.class);

    public static ResponseEntity<?> processError(String error, Throwable th) {
        logger.error("Unable to process with error - {}", error);
        logger.debug("Class - {}, message - {}, cause - {},  stack trace - {}", th.getClass(), th.getMessage(), th.getCause(), th.getStackTrace());
        return ResponseEntity.badRequest().body(new ErrorDto(error, ExceptionUtils.getMessage(th), Instant.now()));
    }

    public static ResponseEntity<?> processError(String error, List<ObjectError> errors) {
        logger.error("Unable to process with error - {}", error);
        return ResponseEntity.badRequest().body(new ErrorDto(error, errors.toString(), Instant.now()));
    }
}
