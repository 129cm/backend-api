package com.d129cm.backendapi.common.exception;

import com.d129cm.backendapi.common.dto.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    public final ResponseEntity<?> handleConflictException(ConflictException e, HttpServletRequest request) {
        log.warn("source = {} {}, message = {}",
                request.getMethod(), request.getRequestURI(), e.getMessage());
        return ResponseEntity
                .status(e.getStatus())
                .body(CommonResponse.failure(e.getStatus(), e.getMessage()));
    }

    @ExceptionHandler(BaseException.class)
    public final ResponseEntity<?> handleFinderException(BaseException e, HttpServletRequest request) {
        log.warn("source = {} {}, message = {}",
                request.getMethod(), request.getRequestURI(), e.getMessage());
        return ResponseEntity
                .status(e.getStatus())
                .body(CommonResponse.failure(e.getStatus(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<CommonResponse<Void>> handleDefaultExceptions(Exception e, HttpServletRequest request) {
        log.error("source = {} {}, message = {}",
                request.getMethod(), request.getRequestURI(), e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"));
    }

}