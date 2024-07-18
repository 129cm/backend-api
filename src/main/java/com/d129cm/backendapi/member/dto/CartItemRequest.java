package com.d129cm.backendapi.member.dto;

public record CartItemRequest (
        Long itemId,
        Long itemOptionId,
        Integer count
){

}
