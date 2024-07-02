package com.d129cm.backendapi.item.dto;

import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;

import java.util.List;

public record ItemCreateRequest(
        String itemName,
        Integer price,
        List<ItemOptionCreateRequest> itemOptions,
        String itemImage,
        String itemDescription
) {
    public Item toItemEntity() {
        List<ItemOption> options = itemOptions.stream()
                .map(ItemOptionCreateRequest::toItemOptionEntity)
                .toList();

        return Item.builder()
                .name(itemName)
                .price(price)
                .image(itemImage)
                .description(itemDescription)
                .itemOptions(options)
                .build();
    }
}
