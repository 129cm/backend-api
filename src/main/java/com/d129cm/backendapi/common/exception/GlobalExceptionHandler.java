package com.d129cm.backendapi.common.exception;

import com.d129cm.backendapi.common.dto.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<CommonResponse<Void>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        log.info("source = {} {}, message = {}",
                request.getMethod(), request.getRequestURI(), ex.getMessage());

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        String errorMessage = fieldErrors.stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(", "));

        return buildResponseEntity(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(BaseException.class)
    public final ResponseEntity<CommonResponse<Void>> handle129cmException(BaseException e, HttpServletRequest request) {
        log.warn("source = {} {}, message = {}",
                request.getMethod(), request.getRequestURI(), e.getMessage());
        return buildResponseEntity(e.getStatus(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<CommonResponse<Void>> handleDefaultExceptions(Exception e, HttpServletRequest request) {
        log.error("source = {} {}, message = {}",
                request.getMethod(), request.getRequestURI(), e.getMessage(), e);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");
    }

    private static ResponseEntity<CommonResponse<Void>> buildResponseEntity(HttpStatusCode statusCode, String message) {
        return ResponseEntity
                .status(statusCode)
                .body(CommonResponse.failure(statusCode, message));
    }
}