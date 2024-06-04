package com.d129cm.backendapi.common.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {
    private static final String DUPLICATED_VALUE = "중복된 %s: '%s'은 중복된 값 입니다.";

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public static ConflictException duplicatedValue(String fieldName, String fieldValue) {
        return new ConflictException(String.format(DUPLICATED_VALUE, fieldName, fieldValue));
    }

}
