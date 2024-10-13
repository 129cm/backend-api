package com.d129cm.backendapi.brand.dto;

import com.d129cm.backendapi.brand.domain.Brand;

public record BrandCreateRequest(
        String brandName,
        String brandDescription,
        String brandImage
) {
    public Brand toBrandEntity() {
        return Brand.builder()
                .name(brandName)
                .description(brandDescription)
                .image(brandImage)
                .build();
    }
}
