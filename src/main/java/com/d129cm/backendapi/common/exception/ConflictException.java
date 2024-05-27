package com.d129cm.backendapi.common.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {
    public ConflictException(String fieldName, String fieldValue) {
        super(String.format("중복된 %s: '%s'은 중복된 값 입니다.", fieldName, fieldValue), HttpStatus.CONFLICT);
    }
}
