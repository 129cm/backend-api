package com.d129cm.backendapi.member.dto;

public record ItemWithOptionForOrderResponse(
        Long itemId,
        String itenName,
        Integer itemPrice,
        String itemImage,
        Long itemOptionId,
        String itemOptionName,
        Integer itemOptionPrice
) {
}
