package com.d129cm.backendapi.partners.dto;

import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;

import java.util.List;

public record PutItemDetailsRequest(
        String itemName,
        Integer price,
        String itemImage,
        String itemDescription,
        List<PutItemOptionRequest> itemOptions
) {
    public Item toItemEntity() {

        Item item = Item.builder()
                .name(itemName)
                .price(price)
                .image(itemImage)
                .description(itemDescription)
                .build();

        List<ItemOption> options = itemOptions.stream().map(PutItemOptionRequest::toItemOptionEntity).toList();
        options.forEach(item::addItemOption);

        return item;
    }
}
