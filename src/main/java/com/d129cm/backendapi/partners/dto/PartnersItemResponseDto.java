package com.d129cm.backendapi.partners.dto;

import com.d129cm.backendapi.item.domain.Item;
import lombok.Builder;

import java.util.List;

@Builder
public record PartnersItemResponseDto(
        Long itemId,
        String itemName,
        Integer price,
        String itemImage,
        String itemDescription,
        List<PartnersItemOptionResponse> itemOptions
) {
    public static PartnersItemResponseDto of(Item item) {
        List<PartnersItemOptionResponse> options = item.getItemOptions()
                .stream()
                .map(PartnersItemOptionResponse::of)
                .toList();

        return PartnersItemResponseDto.builder()
                .itemId(item.getId())
                .itemName(item.getName())
                .price(item.getPrice())
                .itemImage(item.getImage())
                .itemDescription(item.getDescription())
                .itemOptions(options)
                .build();
    }
}
