package com.d129cm.backendapi.partners.dto;

import com.d129cm.backendapi.item.domain.ItemOption;

public record PutItemOptionRequest(
        Long optionId,
        String optionName,
        Integer optionQuantity,
        Integer optionPrice
) {
    public ItemOption toItemOptionEntity() {
        return ItemOption.builder()
                .id(optionId)
                .name(optionName)
                .quantity(optionQuantity)
                .optionPrice(optionPrice)
                .build();
    }
}
