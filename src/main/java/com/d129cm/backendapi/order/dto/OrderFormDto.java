package com.d129cm.backendapi.order.dto;

public record OrderFormDto (
        Long itemId,
        Long itemOptionId,
        Integer count
){

}
