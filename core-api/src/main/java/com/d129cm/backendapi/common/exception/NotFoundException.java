package com.d129cm.backendapi.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    static private final String ENTITY_NOT_FOUND = "정보를 찾을 수 없습니다.";

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public static NotFoundException entityNotFound() {
        return new NotFoundException(String.format(ENTITY_NOT_FOUND));
    }
}
