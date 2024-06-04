package com.d129cm.backendapi.brand.dto;

import com.d129cm.backendapi.brand.domain.Brand;

public record BrandResponse(String brandName, String brandImage, String description) {
    public static BrandResponse of(Brand brand) {
        return new BrandResponse(
                brand.getName(),
                brand.getImage(),
                brand.getDescription()
        );
    }
}
