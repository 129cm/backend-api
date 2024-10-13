package com.d129cm.backendapi.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {
    private static final String EXCEED_LIMIT = "적용할 수 있는 수량 %s(을)를 초과하였습니다";
    private static final String NEGATIVE_QUANTITY = "수량은 0 이하일 수 없습니다. 최소 1 이상의 값을 입력해 주세요.";
    private static final String WRONG_ORDER = "주문 정보가 상이합니다. %s(을)를 확인하세요.";

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public static BadRequestException relationAlreadyExist(String fieldName) {
        String stringFormat = String.format("이미 %s 연관관계가 존재합니다.", fieldName);

        return new BadRequestException(stringFormat);
    }
    public static BadRequestException exceedQuantityLimit(int number) {
        return new BadRequestException(String.format(EXCEED_LIMIT, number));
    }

    public static BadRequestException negativeQuantityLimit() {
        return new BadRequestException(String.format(NEGATIVE_QUANTITY));
    }

    public static BadRequestException wrongOrder(String element) {
        return new BadRequestException(String.format(WRONG_ORDER, element));
    }
}