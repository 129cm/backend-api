package com.d129cm.backendapi.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {
    private BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public static BadRequestException relationAlreadyExist(String fieldName) {
        String stringFormat = String.format("이미 %s 연관관계가 존재합니다.", fieldName);

        return new BadRequestException(stringFormat);
    }
}