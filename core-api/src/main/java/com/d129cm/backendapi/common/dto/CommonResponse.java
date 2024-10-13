package com.d129cm.backendapi.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommonResponse<T>(int status, String message, T data) {

    public static <T> CommonResponse<T> success() {
        return new CommonResponse<>(HttpStatus.OK.value(), "성공", null);
    }

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(HttpStatus.OK.value(), "성공", data);
    }

    public static <T> CommonResponse<T> failure(HttpStatusCode status, String message) {
        return new CommonResponse<>(status.value(), message, null);
    }
}
