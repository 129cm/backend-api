package com.d129cm.backendapi.member.dto;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.item.domain.Item;

import java.util.List;

public record BrandsForMemberResponse (
        String brandName,
        String brandImage,
        String brandDescription,
        List<ItemResponse> itemResponse
) {
    public static BrandsForMemberResponse of(Brand brand, List<Item> items) {
        List<ItemResponse> itemResponse = items.stream()
                .map(item -> new ItemResponse(
                        item.getId(),
                        item.getName(),
                        item.getPrice(),
                        item.getImage()
                )).toList();
        return new BrandsForMemberResponse(
                brand.getName(),
                brand.getImage(),
                brand.getDescription(),
                itemResponse
        );
    }
}
