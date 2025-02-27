package com.d129cm.backendapi.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class BaseException extends RuntimeException {
    private final HttpStatusCode status;

    public BaseException(String message, HttpStatusCode status) {
        super(message);
        this.status = status;
    }
}
