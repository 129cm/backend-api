package com.d129cm.backendapi.common.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {
    private static final String DUPLICATED_VALUE = "중복된 %s: '%s'은 중복된 값 입니다.";
    private static final String EXCEED_MAX_RETRIES = "%s 실패: 최대 재시도 횟수를 초과했습니다.";

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public static ConflictException duplicatedValue(String fieldName, String fieldValue) {
        return new ConflictException(String.format(DUPLICATED_VALUE, fieldName, fieldValue));
    }

    public static ConflictException exceedMaxRetries(String workName) {
        return new ConflictException(String.format(EXCEED_MAX_RETRIES, workName));
    }

}
