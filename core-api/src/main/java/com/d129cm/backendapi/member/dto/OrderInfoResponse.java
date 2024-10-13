package com.d129cm.backendapi.member.dto;

public record OrderInfoResponse (
        String userName,
        String email,
        Integer totalPrice
) {

}
