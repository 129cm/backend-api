package com.d129cm.backendapi.common.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends BaseException{
    public AuthenticationException(String token) {
        super(String.format("토큰 : '%s'는 유효하지 않습니다.", token), HttpStatus.UNAUTHORIZED);
    }
}
