package com.d129cm.backendapi.member.dto;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;

import java.util.List;

public record ItemForMemberResponse(
        Long itemId,
        String itemName,
        Integer itemPrice,
        String itemImage,
        Long brandId,
        String brandName,
        List<ItemOptionResponse> itemOptionResponse
) {
    public static ItemForMemberResponse of(Brand brand, Item item, List<ItemOption> itemOptions) {
        List<ItemOptionResponse> itemOptionResponse = itemOptions.stream()
                .map(itemOption -> new ItemOptionResponse(itemOption.getId(), itemOption.getName())).toList();
        return new ItemForMemberResponse(
                item.getId(),
                item.getName(),
                item.getPrice(),
                item.getImage(),
                brand.getId(),
                brand.getName(),
                itemOptionResponse);
    }
}
