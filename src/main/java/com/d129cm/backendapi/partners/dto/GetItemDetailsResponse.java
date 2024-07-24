package com.d129cm.backendapi.partners.dto;

import com.d129cm.backendapi.item.domain.Item;
import lombok.Builder;

import java.util.List;

@Builder
public record GetItemDetailsResponse(
        Long itemId,
        String itemName,
        Integer price,
        String itemImage,
        String itemDescription,
        List<GetItemOptionDetailsResponse> itemOptions
) {
    public static GetItemDetailsResponse of(Item item) {
        List<GetItemOptionDetailsResponse> options = item.getItemOptions()
                .stream()
                .map(GetItemOptionDetailsResponse::of)
                .toList();

        return GetItemDetailsResponse.builder()
                .itemId(item.getId())
                .itemName(item.getName())
                .price(item.getPrice())
                .itemImage(item.getImage())
                .itemDescription(item.getDescription())
                .itemOptions(options)
                .build();
    }
}
