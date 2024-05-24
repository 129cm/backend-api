package com.d129cm.backendapi.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommonResponse<T>(int status, String message, T data) {

    public static <T> CommonResponse<T> success(HttpStatus status, T data) {
        return new CommonResponse<>(status.value(), "성공", data);
    }

    public static <T> CommonResponse<T> failure(HttpStatus status, String message) {
        return new CommonResponse<>(status.value(), message, null);
    }
}
