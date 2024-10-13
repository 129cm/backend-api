package com.d129cm.backendapi.member.dto;

import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;

public record ItemWithOptionForOrderResponse(
        Long itemId,
        String itemName,
        Integer itemPrice,
        String itemImage,
        Long itemOptionId,
        String itemOptionName,
        Integer itemOptionPrice,
        Integer count
) {
    public static ItemWithOptionForOrderResponse of(Item item, ItemOption itemOption, int count) {
        return new ItemWithOptionForOrderResponse(
                item.getId(),
                item.getName(),
                item.getPrice(),
                item.getImage(),
                itemOption.getId(),
                itemOption.getName(),
                itemOption.getOptionPrice(),
                count
        );
    }
}
