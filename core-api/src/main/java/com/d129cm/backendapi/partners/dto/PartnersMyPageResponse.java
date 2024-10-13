package com.d129cm.backendapi.partners.dto;

import com.d129cm.backendapi.brand.dto.BrandResponse;

public record PartnersMyPageResponse(
        String email,
        String businessNumber,
        BrandResponse brand) {
}
