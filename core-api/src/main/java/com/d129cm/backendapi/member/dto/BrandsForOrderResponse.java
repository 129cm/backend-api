package com.d129cm.backendapi.member.dto;

import com.d129cm.backendapi.brand.domain.Brand;

import java.util.List;

public record BrandsForOrderResponse(
        Long brandId,
        String brandName,
        List<ItemWithOptionForOrderResponse> itemResponse
) {
    public static BrandsForOrderResponse of(Brand brand, List<ItemWithOptionForOrderResponse> responses) {
        return new BrandsForOrderResponse(
                brand.getId(),
                brand.getName(),
                responses
        );
    }
}
