package com.d129cm.backendapi.member.dto;

public record CartItemUpdateRequest (
        Long itemId,
        Long itemOptionId,
        Integer count
){
}
