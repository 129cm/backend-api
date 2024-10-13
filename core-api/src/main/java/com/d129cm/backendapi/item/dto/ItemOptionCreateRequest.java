package com.d129cm.backendapi.item.dto;

import com.d129cm.backendapi.item.domain.ItemOption;

public record ItemOptionCreateRequest(
        String optionName,
        Integer optionQuantity,
        Integer optionPrice
) {
    public ItemOption toItemOptionEntity() {
        return ItemOption.builder()
                .name(optionName)
                .quantity(optionQuantity)
                .optionPrice(optionPrice)
                .build();
    }
}
