package com.d129cm.backendapi.item.dto;

import com.d129cm.backendapi.item.domain.Item;

import java.util.List;

public record ItemCreateRequest(
        String itemName,
        Integer price,
        List<ItemOptionCreateRequest> itemOptions,
        String itemImage,
        String itemDescription
) {
    public Item toItemEntity() {
        return Item.builder()
                .name(itemName)
                .price(price)
                .image(itemImage)
                .description(itemDescription)
                .build();
    }
}
